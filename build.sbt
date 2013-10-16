import android.Keys._

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.2.0")

lazy val hello = (project in file(".")).
  settings(android.Plugin.buildSettings: _*).
  settings(androidBuild: _*).
  settings(
    name := "hello_droid",
    platformTarget in Android := "android-18",
    proguardOptions in Android ++= Seq("-dontwarn sun.misc.Unsafe",
      """-keep class akka.** {
        |  public <methods>;
        |}""".stripMargin)
  )
