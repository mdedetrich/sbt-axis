name := "sbt-axis"

organization := "com.mdedetrich"

version := "0.1.0"

scalaVersion := "2.10.3"

sbtPlugin := true

CrossBuilding.crossSbtVersions := Seq("0.11.3", "0.11.3", "0.13.1")

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

publishArtifact in Test := false

publishTo <<= isSnapshot(
  if (_) Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/") 
  else   Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"))