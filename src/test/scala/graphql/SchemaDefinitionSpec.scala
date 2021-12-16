package graphql

import sangria.ast.Document
import sangria.macros._
import sangria.execution.Executor
import sangria.marshalling.circe._
import io.circe._
import io.circe.parser._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import SchemaDefinition.MoviesSchema
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import sangria.marshalling.circe.CirceResultMarshaller.Node
import service.MovieService

class SchemaDefinitionSpec extends AnyWordSpec with Matchers {
  "Schema Definition" should {
    "return all movie details correctly" in {
      val query =
        graphql"""
          query {
            movies {
              movieInfo {
                title
                genre
                durationInMinutes
              }
            }
          }
               """

      executeQuery(query) should be (parse(
        """
          |{
          | "data": {
          |   "movies": [
          |     { "movieInfo": { "title": "Spider-Man: No Way Home", "genre": "Action", "durationInMinutes": 148 } },
          |     { "movieInfo": { "title": "Get Out", "genre": "Horror", "durationInMinutes": 104 } },
          |     { "movieInfo": { "title": "Shang-Chi and The Legend of The Ten Rings", "genre": "Action", "durationInMinutes": 132 } },
          |     { "movieInfo": { "title": "Train to Busan", "genre": "Action", "durationInMinutes": 118 } }
          |   ]
          | }
          |}
          |""".stripMargin
      ).getOrElse(fail("Failed to parse expected JSON.")))
    }

    "return movie details based on id correctly" in {
      val query =
        graphql"""
          query($$id: Int!) {
            movie(id: $$id) {
              movieInfo {
                title
              }
              cast {
                name
              }
            }
          }
               """

      executeQuery(query, Json.obj("id" -> Json.fromInt(3))) should be (parse(
        """
          |{
          | "data": {
          |   "movie": {
          |     "movieInfo": {
          |       "title": "Shang-Chi and The Legend of The Ten Rings"
          |     },
          |     "cast": [
          |       { "name": "Simu Liu" },
          |       { "name": "Awkwafina" },
          |       { "name": "Meng er Zhang" },
          |       { "name": "Tony Leung" },
          |       { "name": "Michelle Yeoh" }
          |    ]
          |   }
          | }
          |}
          |""".stripMargin
      ).getOrElse(fail("Failed to parse expected JSON.")))
    }
  }

  private def executeQuery(query: Document, variables: Json = Json.obj()): Node = {
    val resultF = Executor.execute(
      MoviesSchema,
      query,
      variables = variables,
      userContext = MovieService(useDbConnection = false)
    )

    Await.result(resultF, 5.seconds)
  }
}
