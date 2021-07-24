val axis2Version = "1.6.2"

lazy val root = project.in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-axis",
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.2.8" // set minimum sbt version
      }
    },
    organization := "org.mdedetrich",
    version := "0.2.0",
    libraryDependencies ++= Seq(
      "org.apache.axis2" % "axis2-kernel" % axis2Version,
      "org.apache.axis2" % "axis2-java2wsdl" % axis2Version,
      "org.apache.axis2" % "axis2-adb" % axis2Version,
      "org.apache.axis2" % "axis2-jaxbri" % axis2Version,
      "org.apache.axis2" % "axis2-adb-codegen" % axis2Version,
      "org.apache.axis2" % "axis2-codegen" % axis2Version,
      "org.apache.axis2" % "axis2-xmlbeans" % axis2Version,
      "commons-logging" % "commons-logging" % "1.0.4",
      "commons-discovery" % "commons-discovery" % "0.2"
    ),
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    Test / publishArtifact := false,
    pomIncludeRepository := { _ => false },
    pomExtra := <url>https://github.com/mdedetrich/sbt-axis</url>
      <licenses>
        <license>
          <name>MIT</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:mdedetrich/sbt-axis.git</url>
        <connection>scm:git:git@github.com:mdedetrich/sbt-axis.git</connection>
      </scm>
      <developers>
        <developer>
          <id>mdedetrich</id>
          <name>Matthew de Detrich</name>
          <email>mdedetrich@gmail.com</email>
        </developer>
      </developers>
  )
