import Dependencies._

name := "graphql-playground"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies ++= Seq(
  "ch.qos.logback"             % "logback-classic" % logbackVersion,
  "com.typesafe"               % "config"          % typesafeConfigVersion,
  "com.typesafe.scala-logging" %% "scala-logging"  % scalaLoggingVersion,

  "com.beachape" %% "enumeratum"       % enumeratumVersion,
  "com.beachape" %% "enumeratum-circe" % enumeratumVersion,
  "io.circe"     %% "circe-parser"     % circeParserVersion,

  "mysql"          % "mysql-connector-java" % mssqlConnectorJavaVersion,
  "org.postgresql" % "postgresql"           % postgresqlVersion,

  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  "org.sangria-graphql" %% "sangria"                 % sangriaGraphqlVersion,
  "org.sangria-graphql" %% "sangria-circe"           % sangriaCirceVersion,
  "org.sangria-graphql" %% "sangria-akka-http-core"  % sangriaAkkaHttpVersion,
  "org.sangria-graphql" %% "sangria-akka-http-circe" % sangriaAkkaHttpVersion,
)