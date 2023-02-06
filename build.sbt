
import sbt.Resolver
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := """uk\.gov\.hmrc\.BuildInfo;.*\.Routes;.*\.RoutesPrefix;.*\.Reverse[^.]*""",
    ScoverageKeys.coverageMinimum := 75.00,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true,
    Test / parallelExecution := false
  )
}

val allDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.6" % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
    "org.mockito"       % "mockito-core"          % "2.28.2"          % "test",
    "uk.gov.hmrc"       %% "hmrctest"             % "3.10.0-play-26"   % "test"
  ),
  play27 = Seq(
    "com.typesafe.play" %% "play-json" % "2.7.4",
    "uk.gov.hmrc" %% "domain" % "6.0.0-play-27",
    "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",
    "uk.gov.hmrc" %% "bootstrap-backend-play-27" % "5.11.0"
  ),
  play28 = Seq(
    "com.typesafe.play"      %% "play-json"          % "2.8.18",
    "uk.gov.hmrc" %% "domain" % "6.0.0-play-28",
    "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",
    "uk.gov.hmrc" %% "bootstrap-backend-play-27" % "5.11.0"
  )
)

val scala2_12 = "2.12.12"
val scala2_13 = "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "agent-kenshoo-monitoring",
    organization := "uk.gov.hmrc",
    scalaVersion := scala2_12,
    crossScalaVersions := List(scala2_12, scala2_13),
    majorVersion := 5,
    isPublicArtefact := true,
    scoverageSettings,
    resolvers ++= Seq(
      Resolver.typesafeRepo("releases"),
    ),
    libraryDependencies ++= allDependencies,
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
