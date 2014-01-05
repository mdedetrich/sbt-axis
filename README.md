An sbt version of the [Axis Tools Maven Plugin][1]. Supports calling
`WSDL2Java` only, to generate Java from WSDL. This is a fork of
[mdr's plugin][3] to be able to use more rencent versions of Axis2.

You should also consider using [scalaxb][2].

Compatible with sbt 0.11.2, 0.11.3 or 0.13.1.

Configuration
-------------

Put this in your `project/plugins.sbt`:

    addSbtPlugin("com.github.mdr" % "sbt-axis" % "0.0.1")

Light configuration example (in `build.sbt`):

    seq(sbtAxisSettings : _*)

    SbtAxisKeys.wsdlFiles <+= baseDirectory(_ / "service.wsdl")

    SbtAxisKeys.packageSpace := Some("com.example")

Full configuration example:

    import sbtaxis.Plugin.{ SbtAxisKeys, sbtAxisSettings }

    class MyBuild extends Build {
         lazy val myProject = Project("Mine", file("."), settings = Defaults.defaultSettings ++ sbtAxisSettings ++ Seq(
             SbtAxisKeys.wsdlFiles <+= baseDirectory(_ / "service.wsdl"),
             SbtAxisKeys.packageSpace := Some("com.example"))
    }

There is an `SbtAxisKeys.otherArgs` for other `WSDL2Java` arguments -- if you use this, 
please consider adding a new setting to the plug-in and sending a pull request ;-)

  [1]: http://mojo.codehaus.org/axistools-maven-plugin/
  [2]: http://scalaxb.org/sbt-scalaxb
  [3]: https://github.com/mdr/sbt-axis
