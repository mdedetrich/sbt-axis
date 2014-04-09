An sbt version of the [Axis Tools Maven Plugin][1]. Supports calling
`WSDL2Java` only, to generate Java from WSDL. Loosely based off
[mdr's plugin][3].



You should also consider using [scalaxb][2].

Compatible with sbt 0.11.2, 0.11.3 or 0.13.1.

# Notes

There are 2 branches for this plugin, one that uses Axis and the other uses Axis2.
Versions 0.1.x correspond to Axis where as versions 0.2.x correspond to Axis2. Both branches
are designed to be updated as SBT/Scala gets updated (the reason why Axis is supported is because
there are some wsdl's that don't compile with the newer Axis2 due to them using deprecated settings)

# Configuration

If you want to use Axis, put this in your `project/plugins.sbt`:

First the repository

```scala
resolvers ++= Seq("mdedetrich-releases" at "http://artifactory.mdedetrich.com/libs-release")
```

Then the plugin

```scala
addSbtPlugin("com.mdedetrich" % "sbt-axis" % "0.1.0")
```

For Axis2, put this in your `project/plugins.sbt`:

```scala
addSbtPlugin("com.mdedetrich" % "sbt-axis" % "0.2.0")
```

Light configuration example (in `build.sbt`):

```scala
seq(sbtAxisSettings : _*)
SbtAxisKeys.wsdlFiles <+= baseDirectory(_ / "service.wsdl")
SbtAxisKeys.packageSpace := Some("com.example")
```


Full configuration example:

```scala
import sbtaxis.Plugin.{ SbtAxisKeys, sbtAxisSettings }
class MyBuild extends Build {
     lazy val myProject = Project("Mine", file("."), settings = Defaults.defaultSettings ++ sbtAxisSettings ++ Seq(
         SbtAxisKeys.wsdlFiles <+= baseDirectory(_ / "service.wsdl"),
         SbtAxisKeys.packageSpace := Some("com.example"))
}
```

There is an `SbtAxisKeys.otherArgs` for other `WSDL2Java` arguments

  [1]: http://mojo.codehaus.org/axistools-maven-plugin/
  [2]: http://scalaxb.org/sbt-scalaxb
  [3]: https://github.com/mdr/sbt-axis
