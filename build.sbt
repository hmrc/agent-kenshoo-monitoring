
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

val allDependencies = Seq(
  "com.typesafe.play"      %% "play-json"                 % "2.9.4",
  "uk.gov.hmrc"            %% "domain"                    % "8.1.0-play-28",
  "com.kenshoo"            %% "metrics-play"              % "2.7.3_0.8.2",
  "uk.gov.hmrc"            %% "bootstrap-backend-play-28" % "7.13.0",
  "org.scalatest"          %% "scalatest"                 % "3.2.15"   % Test,
  "org.pegdown"            %  "pegdown"                   % "1.6.0"    % Test,
  "org.scalacheck"         %% "scalacheck"                % "1.14.0"   % Test,
  "org.mockito"            %  "mockito-core"              % "2.28.2"   % "test",
  "org.scalatestplus.play" %% "scalatestplus-play"        % "5.1.0"    % "test"
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
