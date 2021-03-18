// All the good scalac settings
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.17")

// Linters.
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.13")
addSbtPlugin("org.wartremover" % "sbt-wartremover-contrib" % "1.3.11")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.1.0")
// A better maintained fork of scalastyle.
addSbtPlugin("com.beautiful-scala" %% "sbt-scalastyle" % "1.4.0")

// Quick app restarts.
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// Automatically check for dependency updates.
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.2")

// ScalaJS, duh
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.5.0")
// Brings DOM stuff to NodeJs
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.0.0"
