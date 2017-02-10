name := "OOOServer"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= {
    val akkaV       = "2.4.16"
    val akkaHttpV   = "10.0.1"
    val scalaTestV  = "3.0.1"
    Seq(
           "com.typesafe.akka" %% "akka-actor" % akkaV,
           "com.typesafe.akka" %% "akka-stream" % akkaV,
           "com.typesafe.akka" %% "akka-testkit" % akkaV,
           "com.typesafe.akka" %% "akka-http" % akkaHttpV,
           "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
           "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
           "org.scalatest"     %% "scalatest" % scalaTestV % "test",
           "com.typesafe.play" % "play-json_2.12" % "2.6.0-M1",
           "ch.qos.logback" % "logback-classic" % "1.1.10",
           "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"
       )
}

resolvers ++= Seq(
                     "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
                     "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
                     "Typesafe simple" at "http://repo.typesafe.com/typesafe/simple/maven-releases/"
                 )