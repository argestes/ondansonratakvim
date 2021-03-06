name := "ondansonratakvim"

version := "0.9.1"

lazy val `ondansonratakvim` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws )

libraryDependencies += "joda-time" % "joda-time" % "2.7"

libraryDependencies += "com.google.apis" % "google-api-services-calendar" % "v3-rev99-1.19.0"

libraryDependencies += filters

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
