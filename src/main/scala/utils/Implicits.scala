package utils

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import models.{Actor, Movie, MovieInfo}

object Implicits {
  implicit val actorDecoder: Decoder[Actor] = deriveDecoder[Actor]
  implicit val movieInfoDecoder: Decoder[MovieInfo] = deriveDecoder[MovieInfo]
  implicit val movieDecoder: Decoder[Movie] = deriveDecoder[Movie]
}
