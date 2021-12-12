package utils

import java.sql.ResultSet

object ResultSetUtils {
  def toIterator[T](resultSet: ResultSet)(f: ResultSet => T): Iterator[T] = {
    new Iterator[T] {
      def hasNext: Boolean = resultSet.next()
      def next(): T = f(resultSet)
    }
  }
}
