package service

import models.{Actor, Genre, Language, Movie, MovieInfo}
import org.mockito.Mockito._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import repo.MovieSqlRepository

class MovieServiceSpec extends AnyWordSpec with Matchers {

  val testMovies = Seq(
    Movie(
      MovieInfo(
        id = 1,
        title = "Movie Title",
        releaseDate = "2021-12-01",
        durationMins = 120,
        genre = Genre.Romance,
        synopsis = "Movie Synopsis",
        language = Language.English
      ),
      Seq(
        Actor(
          id = 1,
          name = "Actor Name"
        )
      )
    )
  )

  "MovieService" should {
    "fetch data from DB correctly" in {
      val movieSqlRepoMock = mock[MovieSqlRepository]
      when(movieSqlRepoMock.fetchAllData()).thenReturn(testMovies)

      val movieService = new MovieServiceImpl(movieSqlRepoMock, useDbConnection = true)
      movieService.fetchAllData().map(_.movieInfo.title) shouldEqual Seq("Movie Title")
    }

    "fetch data from file correctly" in {
      val movieSqlRepoMock = mock[MovieSqlRepository]

      val movieService = new MovieServiceImpl(movieSqlRepoMock, useDbConnection = false)
      movieService.fetchAllData().map(_.movieInfo.title) shouldEqual
        Seq("Spider-Man: No Way Home", "Get Out", "Shang-Chi and The Legend of The Ten Rings", "Train to Busan")
    }

    "find data correctly" in {
      val movieSqlRepoMock = mock[MovieSqlRepository]
      when(movieSqlRepoMock.fetchAllData()).thenReturn(testMovies)

      val movieService = new MovieServiceImpl(movieSqlRepoMock, useDbConnection = false)
      movieService.findMovie(3).map(_.movieInfo.title) shouldEqual Some("Shang-Chi and The Legend of The Ten Rings")
    }
  }

}
