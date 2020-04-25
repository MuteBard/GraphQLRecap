name := "udemy-akka-http"

version := "0.1"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.1"

val akkaHttpVersion = "10.1.11"
val scalaTestVersion = "3.1.0"
val sangriaVersion = "2.0.0-RC1"

libraryDependencies ++= Seq(
  // akka streams
//  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
//  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
//  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  // testing
//  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
//  "org.scalatest" %% "scalatest" % scalaTestVersion,

  //GraphQL
  "dev.zio" %% "zio" % "1.0.0-RC18-2",
  "com.github.ghostdogpr" %% "caliban" % "0.7.5",
  "com.github.ghostdogpr" %% "caliban-akka-http" % "0.7.5",
  "de.heikoseeberger"     %% "akka-http-circe" % "1.31.0"


  
  // JWT
//  "com.pauldijou" %% "jwt-spray-json" % "2.1.0"

)
