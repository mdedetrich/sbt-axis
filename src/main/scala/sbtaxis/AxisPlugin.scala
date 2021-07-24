package sbtaxis

import org.apache.axis2.wsdl._
import sbt.Keys._
import sbt.TupleSyntax._
import sbt._

object AxisPlugin extends sbt.AutoPlugin {

  override val trigger: PluginTrigger = noTrigger

  object autoImport {
    lazy val SbtAxis = config("sbtaxis")

    val axisWsdl2java = TaskKey[Seq[File]]("axis-wsdl-2-java", "Runs WSDL2Java")
    val axisWsdlFiles = SettingKey[Seq[File]]("axis-wsdl-files")
    val axisPackageSpace = SettingKey[Option[String]]("axis-package-space", "Package to create Java files under, corresponds to -p / --package option in WSDL2Java")
    val axisDataBindingName = SettingKey[Option[String]]("axis-data-binding-name", "Data binding framework name. Possible values include \"adb\", \"xmlbeans\", \"jibx\".")
    val axisOtherArgs = SettingKey[Seq[String]]("axis-other-args", "Other arguments to pass to WSDL2Java")
  }

  import autoImport._
  override lazy val requires: Plugins = sbt.plugins.JvmPlugin

  override lazy val projectConfigurations = Seq(
    SbtAxis
  )

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      SbtAxis / javaSource := (Compile / sourceManaged).value,
      axisWsdlFiles := Nil,
      axisPackageSpace := None,
      axisDataBindingName := None,
      axisOtherArgs := Nil,
      axisWsdl2java := { (streams, axisWsdlFiles, SbtAxis / javaSource, axisPackageSpace, axisDataBindingName, axisOtherArgs) map { runWsdlToJavas } }.value,
      Compile / sourceGenerators += axisWsdl2java,
      Compile / managedSourceDirectories += (SbtAxis / javaSource).value,
      cleanFiles += (SbtAxis / javaSource).value
    )

  private case class WSDL2JavaSettings(
      dest: File,
      packageSpace: Option[String],
      dataBindingName: Option[String],
      otherArgs: Seq[String]
  )

  private def runWsdlToJavas(
    streams: TaskStreams,
    wsdlFiles: Seq[File],
    dest: File,
    packageSpace: Option[String],
    dataBindingName: Option[String],
    otherArgs: Seq[String]): Seq[File] =
    wsdlFiles.flatMap(wsdl =>
      runWsImport(streams, wsdl, WSDL2JavaSettings(dest, packageSpace, dataBindingName, otherArgs)))

  private def makeArgs(wsdlFile: File, settings: WSDL2JavaSettings): Seq[String] =
    settings.packageSpace.toSeq.flatMap(p => Seq("-p", p)) ++
      settings.dataBindingName.toSeq.flatMap(n => Seq("-d", n)) ++
      Seq("-o", settings.dest.getAbsolutePath) ++
      settings.otherArgs ++
      Seq("-uri", wsdlFile.getAbsolutePath)

  private def runWsImport(streams: TaskStreams, wsdlFile: File, settings: WSDL2JavaSettings): Seq[File] = {
    streams.log.info("Generating Java from " + wsdlFile)

    streams.log.debug("Creating dir " + settings.dest)
    settings.dest.mkdirs()

    val args = makeArgs(wsdlFile, settings)
    streams.log.info("wsimport " + args.mkString(" "))
    try
      WSDL2Code.main(args.toArray)
    catch {
      case t: Throwable =>
        streams.log.error("Problem running WSDL2Java " + args.mkString(" "))
        throw t
    }
    (settings.dest ** "*.java").get
  }
}
