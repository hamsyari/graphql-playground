package repo

trait SqlRepository[T] {
  def fetchAllData(): Seq[T]
}