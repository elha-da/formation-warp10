import java.nio.file.{Files, Paths, StandardCopyOption}

name := "warp10"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "kneelnrise" %% "warp10" % "1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.9"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9"
libraryDependencies += "ch.megard" %% "akka-http-cors" % "0.1.10"

lazy val copyHtml = TaskKey[Unit]("copyHtml", "Copy html files into front directory")

copyHtml := {
  Paths.get("src/main/html").toFile.listFiles()
    .foreach(file => Files.copy(file.toPath, Paths.get("src/main/resources/front", file.name), StandardCopyOption.REPLACE_EXISTING))
}

lazy val copyCss = TaskKey[Unit]("copyCss", "Copy css files into front directory")

copyCss := {
  Paths.get("src/main/css").toFile.listFiles()
    .foreach(file => Files.copy(file.toPath, Paths.get("src/main/resources/front", file.name), StandardCopyOption.REPLACE_EXISTING))
}

lazy val installClient = TaskKey[Unit]("installClient", "Install all elements for client part")

installClient := {
  "npm i".!
}

lazy val buildClient = TaskKey[Unit]("buildClient", "Build client part")

buildClient := {
  "npm run build".!
}

compile in Compile <<= (compile in Compile).dependsOn(buildClient)