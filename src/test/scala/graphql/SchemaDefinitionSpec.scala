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
              title
              genre
              language
            }
          }
               """

      executeQuery(query) should be (parse(
        """
          |{
          | "data": {
          |   "movies": [
          |     { "title": "Spider-Man: No Way Home", "genre": "Action", "language": "English" },
          |     { "title": "Get Out", "genre": "Thriller", "language": "English" },
          |     { "title": "Shang-Chi and The Legend of The Ten Rings", "genre": "Action", "language": "English" },
          |     { "title": "Train to Busan", "genre": "Action", "language": "Korean" }
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
              title
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
          |     "title": "Shang-Chi and The Legend of The Ten Rings",
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
