inThisBuild(
  List(
    organization := "org.polyvariant",
    homepage := Some(url("https://github.com/polyvariant/todo")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "kubukoz",
        "Jakub Kozłowski",
        "kubukoz@gmail.com",
        url("https://blog.kubukoz.com"),
      )
    ),
    Compile / doc / sources := Seq(),
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    sonatypeRepository := "https://s01.oss.sonatype.org/service/local",
  )
)

val Scala212 = "2.12.15"
val Scala213 = "2.13.7"

def crossPlugin(x: sbt.librarymanagement.ModuleID) = compilerPlugin(x.cross(CrossVersion.full))

val compilerPlugins = List(
  crossPlugin("org.typelevel" % "kind-projector" % "0.13.2"),
  crossPlugin("org.polyvariant" % "better-tostring" % "0.3.11"),
)

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(Scala212, Scala213)

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches += RefPredicate.StartsWith(Ref.Tag("v"))
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    env =
      List(
        "PGP_PASSPHRASE",
        "PGP_SECRET",
        "SONATYPE_PASSWORD",
        "SONATYPE_USERNAME",
      ).map { envKey =>
        envKey -> s"$${{ secrets.$envKey }}"
      }.toMap,
  )
)

val root = project
  .in(file("."))
  .settings(
    name := "todo",
    libraryDependencies ++= Seq(
    ) ++ compilerPlugins,
  )
