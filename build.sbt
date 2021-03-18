import Dependencies._

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "jacob"
ThisBuild / organizationName := "jacob"

lazy val root = (project in file("."))
  .settings(
    name := "scion2e",
    libraryDependencies ++= List(
      "org.typelevel" %%% "mouse" % mouseV,
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomV,

      "com.lihaoyi" %%% "utest" % utestV % Test,
    ),
    scalaJSUseMainModuleInitializer := true,
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    testFrameworks += new TestFramework("utest.runner.Framework"),
  )

enablePlugins(ScalaJSPlugin)

