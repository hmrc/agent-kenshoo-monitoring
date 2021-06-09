
import sbt.Resolver
import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray
import uk.gov.hmrc.{SbtArtifactory, SbtAutoBuildPlugin}
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := """uk\.gov\.hmrc\.BuildInfo;.*\.Routes;.*\.RoutesPrefix;.*\.Reverse[^.]*""",
    ScoverageKeys.coverageMinimum := 80.00,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution in Test := false
  )
}

val allDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.6" % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
    "org.mockito"       % "mockito-core"          % "2.28.2"          % "test",
    "uk.gov.hmrc"       %% "hmrctest"             % "3.9.0-play-26"   % "test"
  ),
  play26 = Seq(
    "com.typesafe.play" %% "play-json" % "2.6.13",
    "uk.gov.hmrc" %% "domain" % "5.10.0-play-26",
    "com.kenshoo"       %% "metrics-play"         % "2.6.19_0.7.0",
    "uk.gov.hmrc"       %% "bootstrap-play-26"    % "1.8.0"

  ),
  play27 = Seq(
    "com.typesafe.play" %% "play-json" % "2.7.4",
    "uk.gov.hmrc" %% "domain" % "5.10.0-play-27",
    "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",
    "uk.gov.hmrc" %% "bootstrap-backend-play-27" % "4.3.0"
  ),
  play28 = Seq(
    "com.typesafe.play"      %% "play-json"          % "2.8.1",
    "uk.gov.hmrc" %% "domain" % "5.10.0-play-28",
    "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",
    "uk.gov.hmrc" %% "bootstrap-backend-play-27" % "4.3.0"
  )
)

lazy val root = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    name := "agent-kenshoo-monitoring",
    organization := "uk.gov.hmrc",
    scalaVersion := "2.12.12",
    crossScalaVersions := List("2.12.12"),
    majorVersion := 4,
    makePublicallyAvailableOnBintray := true,
    scoverageSettings,
    resolvers ++= Seq(
      Resolver.typesafeRepo("releases"),
    ),
    libraryDependencies ++= allDependencies
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
