import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings, targetJvm}
import uk.gov.hmrc.PublishingSettings.publishAllArtefacts

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

lazy val kenshooMonitoring = Project("agent-kenshoo-monitoring", file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(majorVersion := 4)
  .settings(scalaSettings ++ scoverageSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    targetJvm := "jvm-1.8",
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12", "2.12.11"),
    libraryDependencies ++= Seq(
      "org.scalatest"     %% "scalatest"            % "3.0.8"           % "test",
      "org.mockito"       % "mockito-core"          % "2.28.2"          % "test",
      "com.kenshoo"       %% "metrics-play"         % "2.6.19_0.7.0",
      "uk.gov.hmrc"       %% "bootstrap-play-26"    % "1.8.0",
      "uk.gov.hmrc"       %% "hmrctest"             % "3.9.0-play-26"   % "test")
  )
  .settings(publishAllArtefacts : _*)
  .settings(makePublicallyAvailableOnBintray := true)
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo))
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
