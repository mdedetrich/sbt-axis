package sbtaxis

import org.apache.axis.utils._
import org.apache.axis.wsdl._
import org.apache.axis.wsdl.gen._
import sbt.Keys._
import sbt.TupleSyntax._
import sbt._

import scala.collection.JavaConverters._

object AxisPlugin extends sbt.AutoPlugin {

  override val trigger: PluginTrigger = noTrigger

  object autoImport {
    val SbtAxis = config("sbtaxis")

    val axisWsdl2java = TaskKey[Seq[File]]("wsdl2java", "Runs WSDL2Java")
    val axisWsdlFiles = SettingKey[Seq[File]]("axis-wsdl-files")
    val axisPackageSpace = SettingKey[Option[String]](
      "axis-package-space",
      "Package to create Java files under, corresponds to -p / --package option in WSDL2Java"
    )
    val axisTimeout   = SettingKey[Option[Int]]("axis-timeout", "Timeout used when generating sources")
    val axisOtherArgs = SettingKey[Seq[String]]("axis-other-args", "Other arguments to pass to WSDL2Java")
    val axisOutputDir = SettingKey[File]("axis-output-dir", "Output directory for the sources")
  }

  import autoImport._

  override lazy val projectConfigurations = Seq(
    SbtAxis
  )

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      SbtAxis / javaSource := (Compile / sourceManaged).value,
      axisWsdlFiles        := Nil,
      axisPackageSpace     := None,
      axisOtherArgs        := Nil,
      axisTimeout          := Some(45),
      axisOutputDir        := sourceManaged.value,
      axisWsdl2java := {
        (streams,
         axisWsdlFiles,
         SbtAxis / javaSource,
         axisPackageSpace,
         axisTimeout,
         axisOutputDir,
         axisOtherArgs
        ) map runWsdlToJavas
      }.value,
      Compile / sourceGenerators += axisWsdl2java,
      Compile / managedSourceDirectories += (SbtAxis / javaSource).value,
      cleanFiles += (SbtAxis / javaSource).value,
      libraryDependencies ++= Seq(
        "axis"             % "axis"        % "1.4",
        "axis"             % "axis-saaj"   % "1.4",
        "axis"             % "axis-wsdl4j" % "1.5.1",
        "javax.activation" % "activation"  % "1.1.1",
        "javax.mail"       % "mail"        % "1.4.6"
      )
    )

  private case class WSDL2JavaSettings(dest: File,
                                       packageSpace: Option[String],
                                       timeout: Option[Int],
                                       outputDir: File,
                                       otherArgs: Seq[String]
  )

  private def runWsdlToJavas(streams: TaskStreams,
                             wsdlFiles: Seq[File],
                             dest: File,
                             packageSpace: Option[String],
                             timeout: Option[Int],
                             outputDir: File,
                             otherArgs: Seq[String]
  ): Seq[File] =
    wsdlFiles
      .flatMap(wsdl => runWsImport(streams, wsdl, WSDL2JavaSettings(dest, packageSpace, timeout, outputDir, otherArgs)))
      .distinct

  private def makeArgs(wsdlFile: File, settings: WSDL2JavaSettings): Seq[String] =
    settings.packageSpace.toSeq.flatMap(p => Seq("--package", p)) ++
      Seq("-O", settings.timeout.map(_.toString).getOrElse("-1")) ++
      Seq("-o", settings.outputDir.getCanonicalPath) ++
      settings.otherArgs ++
      Seq(wsdlFile.getAbsolutePath)

  private def runWsImport(streams: TaskStreams, wsdlFile: File, settings: WSDL2JavaSettings): Seq[File] = {
    streams.log.info("Generating Java from " + wsdlFile)

    streams.log.debug("Creating dir " + settings.dest)
    settings.dest.mkdirs()

    val args = makeArgs(wsdlFile, settings)
    streams.log.debug("wsimport " + args.mkString(" "))
    try new WSDL2JavaWrapper().execute(args.toArray)
    catch {
      case t: Throwable =>
        streams.log.error("Problem running WSDL2Java " + args.mkString(" "))
        throw t
    }
    (settings.outputDir ** "*.java").get
  }

}

class WSDL2JavaWrapper extends WSDL2Java {

  def execute(args: Array[String]) {
    // Extremely ugly hack because the "options" static field in WSDL2Java
    // shadows the "options" instance field in WSDL2. It is the field
    // in WSDL2 that we need because the command line options
    // defined in subclasses get copied to it.
    // The result is that options defined in WSDL2 ( timeout, Debug )
    // are not available otherwise.  (MOJO-318)
    val field = classOf[WSDL2].getDeclaredField("options")

    val options = field.get(this).asInstanceOf[Array[CLOptionDescriptor]]

    for (option <- new CLArgsParser(args, options).getArguments.asScala)
      parseOption(option.asInstanceOf[CLOption])

    validateOptions()

    parser.run(wsdlURI)

  }

}
