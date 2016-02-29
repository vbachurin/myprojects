lazy val root = (project in file(".")).
  settings(
    name := "flights",
    version := "1.0",
    scalaVersion := "2.11.7"
  )
      libraryDependencies ++= Seq(
       "junit" % "junit" % "4.8.1" % "test"
    )
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test

  
