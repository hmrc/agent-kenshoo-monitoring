/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.PublishingSettings._
import uk.gov.hmrc.versioning.SbtGitVersioning

object KenshooMonitoringBuild extends Build {
  import uk.gov.hmrc.SbtAutoBuildPlugin
  import uk.gov.hmrc.SbtArtifactory
  import uk.gov.hmrc.versioning.SbtGitVersioning
  import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
  import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray

  val appDependencies = Seq(
    "uk.gov.hmrc" %% "bootstrap-play-25" % "3.15.0",
    "de.threedimensions" %% "metrics-play" % "2.5.13",

    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.mockito" % "mockito-core" % "2.23.4" % "test",
    "uk.gov.hmrc" %% "hmrctest" % "3.2.0" % "test"
  )

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
    .settings(majorVersion := 3)
    .settings(scalaSettings ++ scoverageSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      targetJvm := "jvm-1.8",
      libraryDependencies ++= appDependencies,
      crossScalaVersions := Seq("2.11.8")
    )
    .settings(publishAllArtefacts : _*)
    .settings(makePublicallyAvailableOnBintray := true)
    .settings(resolvers ++= Seq(
      Resolver.bintrayRepo("hmrc", "releases"),
      Resolver.jcenterRepo))
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
}
