ThisBuild / scalastyleConfig := file("./scalastyle-config.xml")
Global / excludeLintKeys += ThisBuild / scalastyleConfig
