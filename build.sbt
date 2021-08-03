val axisVersion = "1.4"

lazy val root = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name         := "sbt-axis",
    organization := "org.mdedetrich",
    version      := "0.1.0",
    libraryDependencies ++= Seq(
      "axis"              % "axis"              % axisVersion,
      "axis"              % "axis-saaj"         % axisVersion,
      "axis"              % "axis-wsdl4j"       % "1.5.1",
      "javax.activation"  % "activation"        % "1.1.1",
      "javax.mail"        % "mail"              % "1.4",
      "commons-logging"   % "commons-logging"   % "1.0.4",
      "commons-discovery" % "commons-discovery" % "0.2"
    ),
    publishMavenStyle      := true,
    publishTo              := sonatypePublishTo.value,
    Test / publishArtifact := false,
    pomIncludeRepository   := { _ => false },
    homepage               := Some(url("https://github.com/mdedetrich/sbt-axis")),
    licenses += ("MIT", url("https://opensource.org/licenses/mit")),
    developers := List(
      Developer("mdr", "Matthew D. Russell", "", url("https://github.com/mdr")),
      Developer("sortega", "Sebastián Ortega", "sebastian.ortega@letgo.com", url("https://github.com/sortega")),
      Developer("mdedetrich", "Matthew de Detrich", "mdedetrich@gmail.com", url("https://github.com/mdedetrich"))
    ),
    scmInfo := Some(
      ScmInfo(url("https://github.com/mdedetrich/sbt-axis"), "git:git@github.com:mdedetrich/sbt-axis.git")
    )
  )
