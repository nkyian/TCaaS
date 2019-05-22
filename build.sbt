name := "TCaaS"

version := "0.0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.23", // Akka Streams
  "com.typesafe.akka" %% "akka-actor" % "2.5.23", // Akka Actors
  "com.typesafe.akka" %% "akka-persistence" % "2.5.23", // Akka persistence
  "com.typesafe.akka" %% "akka-http"   % "10.1.8", // Akka HTTP
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8", // LevelDB support
  "org.pircbotx" % "pircbotx" % "2.1", // IRC/IRCv2 wrapper
  "org.slf4j" % "slf4j-simple" % "1.7.26",
  "org.slf4j" % "slf4j-api" % "1.7.26"
) 