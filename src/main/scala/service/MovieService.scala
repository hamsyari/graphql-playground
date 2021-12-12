package service

import com.typesafe.scalalogging.LazyLogging
import models._
import repo.MovieSqlRepository
import utils.Implicits._
import io.circe.parser.decode

import scala.io.Source

class MovieServiceImpl(movieSqlRepository: MovieSqlRepository, useDbConnection: Boolean) extends LazyLogging{
  def fetchAllData(): Seq[Movie] = {
    if (useDbConnection) {
      movieSqlRepository.fetchAllData()
    } else {
      val movieSource = Source.fromResource("movieData.json")
      val movieList = decode[Seq[Movie]](movieSource.mkString) match {
        case Right(value) => value
        case Left(error) =>
          logger.error(s"Unable to decode file content", error)
          Seq.empty
      }
      movieSource.close()
      movieList
    }
  }

  def findMovie(id: Int): Option[Movie] = {
    fetchAllData().find(_.movieInfo.id == id)
  }
}

object MovieService {
  def apply(useDbConnection: Boolean): MovieServiceImpl = {
    val movieSqlRepository = new MovieSqlRepository()
    new MovieServiceImpl(movieSqlRepository, useDbConnection)
  }
}