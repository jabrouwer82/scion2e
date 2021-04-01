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
val newtypeV = "0.4.4"
val outwatchV = "8b875c4c09"
// val colibriV = "6000107"

lazy val copyFastOptJS = TaskKey[Unit]("copyFastOptJS", "Copy javascript files to target directory")

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
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++= List(
      "io.circe" %%% "circe-core" % circeV,
      "com.softwaremill.sttp.tapir" %%% "tapir-json-circe" % tapirV,
      "com.softwaremill.sttp.tapir" %% "tapir-newtype" % tapirV,
      "com.chuusai" %%% "shapeless" % shapelessV,
      "io.estatico" %% "newtype" % newtypeV,
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
    // cleanFiles ++= List(
    //   (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
    //   (ThisBuild / baseDirectory).value / "static" / "js" / "client.js.map",
    // ),
    // (Compile / fastOptJS / artifactPath) := (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
    // (Compile / fullOptJS / artifactPath) := (ThisBuild / baseDirectory).value / "static" / "js" / "client.js",
    libraryDependencies ++= List(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV,
      "org.typelevel" %%% "cats-effect" % catsV,
      "co.fs2" %%% "fs2-core" % fs2V,
      "io.circe" %%% "circe-generic" % circeV,
      "io.circe" %%% "circe-parser" % circeV,
      "com.softwaremill.sttp.tapir" %%% "tapir-sttp-client" % tapirV,
      "com.softwaremill.sttp.client3" %%% "cats" % sttpClientV,
      "com.github.outwatch.outwatch" %%% "outwatch" % outwatchV,
      "com.github.outwatch.outwatch" %%% "outwatch-monix" % outwatchV,
      // "com.github.cornerman.colibri" %%% "colibri-monix" % colibriV,
    ),
    scalaJSUseMainModuleInitializer := true,
    useYarn := true,
    requireJsDomEnv in Test := true,
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),

    // Dev server stuff from outwatch g8 seed.
    addCommandAlias("dev", "; compile; fastOptJS::startWebpackDevServer; devwatch; fastOptJS::stopWebpackDevServer"),
    addCommandAlias("devwatch", "~; fastOptJS; copyFastOptJS"),
    webpack / version := "4.43.0", 
    startWebpackDevServer /version := "3.11.0",
    webpackDevServerExtraArgs := Seq("--progress", "--color"),
    webpackDevServerPort := 8293,
    fastOptJS / webpackConfigFile := Some(baseDirectory.value / "webpack.config.dev.js"),

    fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly(), // https://scalacenter.github.io/scalajs-bundler/cookbook.html#performance

    // when running the "dev" alias, after every fastOptJS compile all artifacts are copied into
    // a folder which is served and watched by the webpack devserver.
    // this is a workaround for: https://github.com/scalacenter/scalajs-bundler/issues/180
    copyFastOptJS := {
      val inDir = (crossTarget in (Compile, fastOptJS)).value
      val outDir = (crossTarget in (Compile, fastOptJS)).value / "dev"
      val files = Seq(name.value.toLowerCase + "-fastopt-loader.js", name.value.toLowerCase + "-fastopt.js") map { p => (inDir / p, outDir / p) }
      IO.copy(files, overwrite = true, preserveLastModified = true, preserveExecutable = true)
    },

  )
  .dependsOn(common.js)

