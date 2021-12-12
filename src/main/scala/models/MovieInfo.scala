package models

case class MovieInfo(
  id: Int,
  title: String,
  releaseDate: String,
  durationMins: Int,
  genre: Genre,
  synopsis: String,
  language: Language
)