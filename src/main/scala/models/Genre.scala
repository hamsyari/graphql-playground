package models

import enumeratum._

sealed abstract class Genre(val genreId: Int) extends EnumEntry with Ordered[Genre] {
  def compare(that: Genre): Int = this.genreId - that.genreId
}

object Genre extends CirceEnum[Genre] with Enum[Genre] {
  val values = findValues

  case object Unknown extends Genre(-1)

  case object Action extends Genre(1)

  case object Romance extends Genre(2)

  case object Drama extends Genre(3)

  case object Thriller extends Genre(4)

  case object Comedy extends Genre(5)

  case object Animation extends Genre(6)

  def find(genreId: Int): Genre =
    values.find(_.genreId == genreId).getOrElse(Genre.Unknown)
}