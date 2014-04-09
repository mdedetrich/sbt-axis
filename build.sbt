name := "sbt-axis"

organization := "com.mdedetrich"

version := "0.2.0"

scalaVersion := "2.10.3"

sbtPlugin := true

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.11.3", "0.11.3", "0.13.1")

libraryDependencies ++= Seq(
  "org.apache.axis2" % "axis2-kernel" % "1.6.2",
  "org.apache.axis2" % "axis2-java2wsdl" % "1.6.2",
  "org.apache.axis2" % "axis2-adb" % "1.6.2",
  "org.apache.axis2" % "axis2-jaxbri" % "1.6.2",
  "org.apache.axis2" % "axis2-adb-codegen" % "1.6.2",
  "org.apache.axis2" % "axis2-codegen" % "1.6.2",
  "org.apache.axis2" % "axis2-xmlbeans" % "1.6.2",
  "commons-logging" % "commons-logging" % "1.0.4",
  "commons-discovery" % "commons-discovery" % "0.2"
)