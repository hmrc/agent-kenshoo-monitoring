
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
    parallelExecution in Test := false
  )
}

val allDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.15" % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalacheck" %% "scalacheck" % "1.17.0" % Test,
    "org.mockito"       % "mockito-core"          % "5.2.0"          % "test",
    "uk.gov.hmrc"       %% "hmrctest"             % "3.10.0-play-26"   % "test",

    "com.typesafe.play"      %% "play-json"          % "2.9.4",
    "uk.gov.hmrc" %% "domain" % "8.2.0-play-28",
    "com.kenshoo" %% "metrics-play" % "2.7.3_0.8.2",
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "7.15.0"
  )


lazy val root = (project in file("."))
  .settings(
    name := "agent-kenshoo-monitoring",
    organization := "uk.gov.hmrc",
    scalaVersion := "2.12.12", // TODO DUE to change
    crossScalaVersions := List("2.12.12","2.13.8"),
    majorVersion := 5,
    isPublicArtefact := true,
    scoverageSettings,
    resolvers ++= Seq(
      Resolver.typesafeRepo("releases"),
    ),
    libraryDependencies ++= allDependencies,
  )

