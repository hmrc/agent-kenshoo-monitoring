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
  val appDependencies = Seq(
    Dependencies.Compile.kenshoo,
    Dependencies.Compile.microserviceBootstrap,
    Dependencies.Compile.hmrcHttpException,
    Dependencies.Test.scalaTest,
    Dependencies.Test.restAssured,
    Dependencies.Test.mockito,
    Dependencies.Test.hmrcTest
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
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(scalaSettings ++ scoverageSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      targetJvm := "jvm-1.8",
      scalaVersion := "2.11.11",
      libraryDependencies ++= appDependencies,
      crossScalaVersions := Seq("2.11.8")
    )
    .settings(publishAllArtefacts : _*)
    .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"))
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
}

object Dependencies {

  object Compile {
    val kenshoo = "de.threedimensions" %% "metrics-play" % "2.5.13"
    val microserviceBootstrap = "uk.gov.hmrc" %% "microservice-bootstrap" % "6.18.0"
    val hmrcHttpException = "uk.gov.hmrc" %% "http-exceptions" % "1.1.0"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
    val restAssured = "com.jayway.restassured" % "rest-assured" % "2.6.0" % "test"
    val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"
    val hmrcTest = "uk.gov.hmrc" %% "hmrctest" % "2.4.0" % "test"
  }

}
