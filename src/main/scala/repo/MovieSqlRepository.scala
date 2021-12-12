package repo

import com.typesafe.scalalogging.LazyLogging
import models._
import utils.{DBConnection, ResultSetUtils}

import scala.util.{Failure, Success, Try}

class MovieSqlRepository extends SqlRepository[Movie] with LazyLogging {
  def fetchAllData(): Seq[Movie] = {
    val movieInfo = fetchMovies()
    movieInfo.map( info => {
      Movie(
        movieInfo = info,
        cast = fetchMovieCast(info.id)
      )
    })
  }

  private def fetchMovies(): Seq[MovieInfo] = {
    val query = "SELECT * FROM movie"
    Try(DBConnection.executeQuery(query)) match {
      case Success(rs) =>
        ResultSetUtils.toIterator(rs) {
          row =>
            MovieInfo(
              row.getInt("id"),
              row.getString("title"),
              row.getString("release_date"),
              row.getInt("duration_mins"),
              Genre.find(row.getInt("genre_id")),
              row.getString("synopsis"),
              Language.find(row.getInt("language_id"))
            )
        }.toSeq
      case Failure(exception) =>
        logger.error(s"Error occurred when running query $query", exception)
        Seq.empty
    }
  }

  private def fetchMovieCast(movieId: Int): Seq[Actor] = {
    val query =
      s"""
        | SELECT *
        | FROM movie_cast
        |   INNER JOIN actor ON movie_cast.actor_id = actor.id
        | WHERE movie_cast.movie_id = ${movieId}
        |""".stripMargin

    Try(DBConnection.executeQuery(query)) match {
      case Success(rs) =>
        ResultSetUtils.toIterator(rs) {
          row =>
            Actor(
              row.getInt("id"),
              row.getString("name")
            )
        }.toSeq
      case Failure(exception) =>
        logger.error(s"Error occurred when running query $query", exception)
        Seq.empty
    }
  }
}