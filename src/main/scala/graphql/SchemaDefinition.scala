package graphql

import models.{Actor, Genre, Language, Movie}
import sangria.schema._
import service.MovieServiceImpl

object SchemaDefinition {
  val GenreEnum: EnumType[Genre] = EnumType(
    "Genre",
    Some("Genre of Movie"),
    Genre.values.map(genre => EnumValue(genre.toString, value = genre)).toList
  )

  val LanguageEnum: EnumType[Language] = EnumType(
    "Language",
    Some("Language of Movie"),
    Language.values.map(language => EnumValue(language.toString, value = language)).toList
  )

  val ActorType: ObjectType[Unit, Actor] = ObjectType(
    "Actor",
    "Actor Information",
    fields[Unit, Actor](
      Field("name", StringType, resolve = _.value.name)
    )
  )

  val MovieType: ObjectType[Unit, Movie] = ObjectType(
    "Movie",
    "Movie Information",
    fields[Unit, Movie](
      Field("title", StringType, resolve = _.value.movieInfo.title),
      Field("synopsis", StringType, resolve = _.value.movieInfo.synopsis),
      Field("genre", GenreEnum, resolve = _.value.movieInfo.genre),
      Field("durationMins", IntType, resolve = _.value.movieInfo.durationMins),
      Field("releaseDate", StringType, resolve = _.value.movieInfo.releaseDate),
      Field("language", LanguageEnum, resolve = _.value.movieInfo.language),
      Field("cast", ListType(ActorType), resolve = _.value.cast)
    )
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

  val moviesSchema: Schema[MovieServiceImpl, Unit] = Schema(QueryType)
}
