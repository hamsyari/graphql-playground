package models

import enumeratum._

sealed abstract class Language(val languageId: Int) extends EnumEntry with Ordered[Language] {
  def compare(that: Language): Int = this.languageId - that.languageId
}

object Language extends CirceEnum[Language] with Enum[Language] {
  val values = findValues

  case object Unknown extends Language(-1)

  case object English extends Language(1)

  case object Mandarin extends Language(2)

  case object Hindi extends Language(3)

  case object Spanish extends Language(4)

  case object Arabic extends Language(5)

  case object French extends Language(6)

  case object Russian extends Language(7)

  case object Portuguese extends Language(8)

  case object Korean extends Language(9)

  case object Japanese extends Language(10)


  def find(languageId: Int): Language =
    values.find(_.languageId == languageId).getOrElse(Language.Unknown)
}