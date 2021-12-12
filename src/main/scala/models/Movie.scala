package models

case class Movie(
  movieInfo: MovieInfo,
  cast: Seq[Actor]
)