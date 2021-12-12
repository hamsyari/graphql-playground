import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import graphql.SchemaDefinition
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.http.akka.circe.CirceHttpSupport
import sangria.marshalling.circe._

import scala.util.{Failure, Success}
import utils.DBConnection
import service.MovieService

object Playground extends App with CirceHttpSupport with LazyLogging {
  implicit val system: ActorSystem = ActorSystem("sangria-server")
  import system.dispatcher

  val appConfig = ConfigFactory.load()
  val useDbConnection = appConfig.getBoolean("useDbConnection")

  if (useDbConnection) {
    DBConnection.initialise(appConfig)
  }

  val route: Route =
    path("graphql") {
      graphQLPlayground ~
        prepareGraphQLRequest {
          case Success(req) =>
            val graphQLResponse = Executor.execute(
              SchemaDefinition.MoviesSchema,
              req.query,
              userContext = MovieService(useDbConnection),
              variables = req.variables
            ).map(OK -> _)
              .recover {
                case error: QueryAnalysisError => BadRequest -> error.resolveError
                case error: ErrorWithResolver => InternalServerError -> error.resolveError
              }
            complete(graphQLResponse)
          case Failure(preparationError) => complete(BadRequest, formatError(preparationError))
        }
    }

  val PORT = sys.props.get("http.port").fold(8080)(_.toInt)
  val INTERFACE = "0.0.0.0"
  Http().newServerAt(INTERFACE, PORT).bind(route)

  logger.info(s"Ready to use: http://${INTERFACE}:${PORT}/graphql!")
}
