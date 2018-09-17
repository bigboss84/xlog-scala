name := "xlog-scala"

version := "1.1"

scalaVersion := "2.12.6"

lazy val root = (project in file("."))
  .dependsOn(namedParam)

lazy val namedParam =
  RootProject(uri("git://github.com/bigboss84/xenum-scala.git#tags/v1.1"))

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
