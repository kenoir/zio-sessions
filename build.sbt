name := "zio-sessions"

version := "0.1"

scalaVersion := "2.13.8"

idePackagePrefix := Some("example.zio")

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.0-RC5",
  "com.gu" %% "content-api-client-default" % "17.25.1"
)
