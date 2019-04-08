scalaVersion := "2.12.8"


name := "fp_in_scala_epfl"
version := "1.0"


libraryDependencies += "junit" % "junit" % "4.10" % Test

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.3")