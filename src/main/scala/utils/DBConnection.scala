package utils

import com.typesafe.config.Config

import java.sql.{Connection, DriverManager, ResultSet}

object DBConnection {
  private var connection: Connection = _

  def initialise(config: Config): Unit = {
    val dbConfig = config.getConfig("sql")

    connection = DriverManager.getConnection(
      getConnectionUrl(dbConfig),
      dbConfig.getString("username"),
      dbConfig.getString("password")
    )
  }

  private def getConnectionUrl(config: Config): String = {
    val fullHost = s"${config.getString("host")}:${config.getInt("port")}"
    val database = config.getString("database")

    val connectionUrl = s"jdbc:postgresql://${fullHost}/${database}"
    connectionUrl
  }

  def executeQuery(query: String): ResultSet = {
    val statement = connection.createStatement
    statement.executeQuery(query)
  }

  def close(): Unit = {
    connection.close()
  }
}
