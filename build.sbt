name := """twigly_analytics"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.typesafe.play.modules" %% "play-modules-redis" % "2.5.0",
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"
)
