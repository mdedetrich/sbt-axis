name := "sbt-axis"

organization := "org.mdedetrich"

version := "0.1.0"

scalaVersion := "2.10.5"

sbtPlugin := true

CrossBuilding.crossSbtVersions := Seq("0.13.1","0.11.3", "0.11.2")

libraryDependencies ++= Seq(
  "axis" % "axis" % "1.4",
  "axis" % "axis-saaj" % "1.4",
  "axis" % "axis-wsdl4j" % "1.5.1",
  "javax.activation" % "activation" % "1.1.1",
  "javax.mail" % "mail" % "1.4",
  "commons-logging" % "commons-logging" % "1.0.4",
  "commons-discovery" % "commons-discovery" % "0.2"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

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