addSbtPlugin("net.virtual-void" % "sbt-cross-building" % "0.8.1")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0-RC1")

resolvers += Resolver.url("sbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.1")
