lazy val root = (project in file(".")).
  settings(
    name := "flights",
    version := "1.0",
    scalaVersion := "2.11.7"
  )
      libraryDependencies ++= Seq(
       "junit" % "junit" % "4.8.1" % "test"
    )
    
libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

  
