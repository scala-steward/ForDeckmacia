import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

lazy val fordeckmacia = project.in(file("."))
  .settings(commonSettings, releaseSettings, publish / skip := true)
  .aggregate(core.jvm, core.js)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    commonSettings,
    releaseSettings,
    name := "fordeckmacia",
    crossScalaVersions := List(scalaVersion.value, "2.12.11"),
  )
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))

lazy val docs = project
  .in(file("fordeckmacia-docs"))
  .settings(
    commonSettings,
    mdocIn := file("docs/README.md"),
    mdocOut := file("README.md"),
    publish / skip := true
  )
  .dependsOn(core.jvm)
  .enablePlugins(MdocPlugin)

lazy val commonSettings = Seq(
  organization := "com.github",
  scalaVersion := "2.13.2",
  libraryDependencies ++= Seq(
    "org.scodec"    %%% "scodec-core"      % "1.11.7",
    "org.scodec"    %%% "scodec-bits"      % "1.1.20",
    "org.scalameta" %%% "munit"            % "0.7.12" % Test,
    "org.scalameta" %%% "munit-scalacheck" % "0.7.12" % Test
  ),
  testFrameworks += new TestFramework("munit.Framework"),
  mimaPreviousArtifacts := Set(organization.value %%% moduleName.value % "1.0.0")
)

lazy val releaseSettings = Seq(
  organization := "com.github.billzabob",
  homepage := Some(url("https://github.com/billzabob/fordeckmacia")),
  licenses := Seq("LGPL-3.0" -> url("https://www.gnu.org/licenses/lgpl-3.0.en.html")),
  developers := List(
    Developer(
      "billzabob",
      "Nick Hallstrom",
      "cocolymoo@gmail.com",
      url("https://github.com/billzabob")
    )
  )
)

Global / onChangedBuildSource := ReloadOnSourceChanges
