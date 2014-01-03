package sbtaxis

import sbt._
import sbt.Keys._

import org.apache.axis2.wsdl._

object Plugin extends sbt.Plugin {

  val SbtAxis = config("sbtaxis")

  object SbtAxisKeys {

    val wsdl2java = TaskKey[Seq[File]]("wsdl2java", "Runs WSDL2Java")
    val wsdlFiles = SettingKey[Seq[File]]("axis-wsdl-files")
    val packageSpace = SettingKey[Option[String]]("axis-package-space", "Package to create Java files under, corresponds to -p / --package option in WSDL2Java")
    val dataBindingName = SettingKey[Option[String]]("axis-data-binding-name", "Data binding framework name. Possible values include \"adb\", \"xmlbeans\", \"jibx\".")
    val otherArgs = SettingKey[Seq[String]]("axis-other-args", "Other arguments to pass to WSDL2Java")

  }

  import SbtAxisKeys._

  val sbtAxisSettings: Seq[Setting[_]] =
    Seq(
      javaSource in SbtAxis <<= sourceManaged in Compile,
      wsdlFiles := Nil,
      packageSpace := None,
      dataBindingName := None,
      otherArgs := Nil,
      wsdl2java <<= (streams, wsdlFiles, javaSource in SbtAxis, packageSpace, dataBindingName, otherArgs) map { runWsdlToJavas },
      sourceGenerators in Compile <+= wsdl2java,
      managedSourceDirectories in Compile <+= (javaSource in SbtAxis),
      cleanFiles <+= (javaSource in SbtAxis))

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
