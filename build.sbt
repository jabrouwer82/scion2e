ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "jacob"
ThisBuild / organizationName := "jacob"

val mouseV = "1.0.0"
val utestV = "0.7.4"
val scalaJsDomV = "1.1.0"
val http4sV = "0.21.20"
val circeV = "0.13.0"
val slf4jV = "1.7.30"
val kindProjectorV = "0.11.3"
val catsV = "2.3.3"
val fs2V = "2.5.3"
val munitV = "0.7.22"
val munitCatsEffectV = "0.13.1"
val tapirV = "0.17.19"
val shapelessV = "2.3.3"
val sttpClientV = "3.1.9"
val laminarMdV = "0.1.0"
val laminarV = "0.12.1"
val outwatchV = "0.9.1"

lazy val root = (project in file("."))
  .aggregate(common.jvm, common.js, server, client)
  .settings(
    Compile / run := Def
      .sequential(
        client / Compile / fastOptJS,
        (server / Compile / run).toTask("")
      )
      .value
  )

lazy val common = crossProject(JVMPlatform, JSPlatform)
  .in(file("common"))
  .settings(
    libraryDependencies ++= List(
      "io.circe" %%% "circe-core" % circeV,
      "com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % tapirV,
      "com.chuusai" %%% "shapeless" % shapelessV,
    ),
  )

lazy val server = (project in file("server"))
  .settings(
    addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full),
    libraryDependencies ++= List(
      "org.http4s" %% "http4s-blaze-server" % http4sV,
      "org.http4s" %% "http4s-dsl" % http4sV,
      "org.http4s" %% "http4s-circe" % http4sV,
      "org.typelevel" %% "mouse" % mouseV,
      "org.slf4j" % "slf4j-simple" % slf4jV,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirV,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirV,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirV,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirV,
    ),
    testFrameworks += new TestFramework("munit.Framework"),
  )
  .dependsOn(common.jvm)

lazy val client = (project in file("client"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    resolvers += "jitpack" at "https://jitpack.io",
    // scalacOptions ++= compileOptions.filterNot(_ == "-Ywarn-unused:params").filterNot(_ == "-Ywarn-unused:privates"),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full),
    cleanFiles ++= List(
      (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
      (ThisBuild / baseDirectory).value / "static" / "js" / "client.js.map",
    ),
    (Compile / fastOptJS / artifactPath) := (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
    (Compile / fullOptJS / artifactPath) := (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
    libraryDependencies ++= List(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV,
      "org.typelevel" %%% "cats-effect" % catsV,
      "co.fs2" %%% "fs2-core" % fs2V,
      "io.circe" %%% "circe-generic" % circeV,
      "io.circe" %%% "circe-parser" % circeV,
      "com.softwaremill.sttp.tapir" %%% "tapir-sttp-client" % tapirV,
      "com.softwaremill.sttp.client3" %%% "cats" % sttpClientV,
      "com.github.outwatch.outwatch" %%% "outwatch" % outwatchV,
    ),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
  )
  .dependsOn(common.js)


