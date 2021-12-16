package graphql

import models._
import sangria.macros.derive._
import sangria.schema._
import service.MovieServiceImpl

object SchemaDefinition {
  implicit val GenreEnum: EnumType[Genre] = deriveEnumType[Genre](
    EnumTypeName("Genre"),
    EnumTypeDescription("Genre of Movie"),
    RenameValue("Thriller", "Horror")
  )

  implicit val LanguageEnum: EnumType[Language] = deriveEnumType[Language](
    EnumTypeName("Language"),
    EnumTypeDescription("Language of Movie")
  )

  implicit val ActorType: ObjectType[Unit, Actor] = deriveObjectType[Unit, Actor](
    ObjectTypeName("Actor"),
    ObjectTypeDescription("Actor Information"),
    AddFields(
      Field("randomNum", FloatType, resolve = _ => Math.random())
    )
  )

  implicit val MovieInfoType: ObjectType[Unit, MovieInfo] = deriveObjectType[Unit, MovieInfo](
    ObjectTypeName("MovieInfo"),
    ObjectTypeDescription("Movie Information"),
    RenameField("durationMins", "durationInMinutes")
  )

  val MovieType: ObjectType[Unit, Movie] = deriveObjectType[Unit, Movie](
    ObjectTypeName("Movie"),
    ObjectTypeDescription("Movie & Cast Information")
  )

  val Id: Argument[Int] = Argument("id", IntType)

  val QueryType: ObjectType[MovieServiceImpl, Unit] = ObjectType("Query", fields[MovieServiceImpl, Unit](
    Field("movie", OptionType(MovieType),
      description = Some("Returns movie with specified id"),
      arguments = Id :: Nil,
      resolve = c => c.ctx.findMovie(c.arg(Id))),
    Field("movies", ListType(MovieType),
      description = Some("Returns list of movies"),
      resolve = _.ctx.fetchAllData())
  ))

  val MoviesSchema: Schema[MovieServiceImpl, Unit] = Schema(QueryType)
}
