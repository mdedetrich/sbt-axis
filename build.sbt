name := "sbt-axis"

organization := "com.github.sortega"

version := "0.0.2"

scalaVersion := "2.10.3"

sbtPlugin := true

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.11.3", "0.11.3", "0.13.1")

libraryDependencies ++= Seq(
  "org.apache.axis2" % "axis2-kernel" % "1.6.2",
  "org.apache.axis2" % "axis2-java2wsdl" % "1.6.2",
  "org.apache.axis2" % "axis2-adb" % "1.6.2",
  "org.apache.axis2" % "axis2-adb-codegen" % "1.6.2",
  "org.apache.axis2" % "axis2-codegen" % "1.6.2",
  "org.apache.axis2" % "axis2-xmlbeans" % "1.6.2",
  "commons-logging" % "commons-logging" % "1.0.4",
  "commons-discovery" % "commons-discovery" % "0.2")

publishMavenStyle := true

publishArtifact in Test := false

publishTo <<= isSnapshot(
  if (_) Some("snapshots" at "http://bitmarket.no-ip.biz:8086/nexus/content/repositories/snapshots")
  else   Some("releases" at "http://bitmarket.no-ip.biz:8086/nexus/content/repositories/releases"))

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

pomExtra := {
    <inceptionYear>2012</inceptionYear>
    <url>http://github.com/sortega/sbt-axis</url>
    <licenses>
      <license>
        <name>MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:sortega/sbt-axis.git</url>
      <connection>scm:git:git@github.com:sortega/sbt-axis</connection>
    </scm>
    <developers>
      <developer>
        <id>mdr</id>
        <name>Matt Russell</name>
        <url>https://github.com/mdr/</url>
      </developer>
      <developer>
        <id>sortega</id>
        <name>Sebastian Ortega</name>
        <url>https://github.com/sortega/</url>
      </developer>
    </developers>
  }