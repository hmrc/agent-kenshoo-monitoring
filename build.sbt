import sbt.Resolver
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := """uk\.gov\.hmrc\.BuildInfo;.*\.Routes;.*\.RoutesPrefix;.*\.Reverse[^.]*""",
    ScoverageKeys.coverageMinimumStmtTotal := 75.00,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true,
    Test / parallelExecution := false
  )
}

val scala2_12 = "2.12.12"
val scala2_13 = "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "agent-kenshoo-monitoring",
    organization := "uk.gov.hmrc",
    scalaVersion := scala2_12,
    crossScalaVersions := List(scala2_12),
    majorVersion := 5,
    isPublicArtefact := true,
    scoverageSettings,
    resolvers ++= Seq(
      Resolver.typesafeRepo("releases"),
    ),
    libraryDependencies ++= LibDependencies.compile ++ LibDependencies.test
  )//v Required to prevent https://github.com/scalatest/scalatest/issues/1427 (unstable build due to failure to read test reports)
  .disablePlugins(JUnitXmlReportPlugin)

