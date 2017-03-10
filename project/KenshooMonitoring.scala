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
    Dependencies.Compile.hmrcHttpVerbs,
    Dependencies.Compile.hmrcHttpException,
    Dependencies.Test.scalaTest,
    Dependencies.Test.restAssured,
    Dependencies.Test.mockito,
    Dependencies.Test.hmrcTest
  )

  lazy val kenshooMonitoring = Project("agent-kenshoo-monitoring", file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(scalaSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      targetJvm := "jvm-1.8",
      scalaVersion := "2.11.8",
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
    val hmrcHttpVerbs = "uk.gov.hmrc" %% "http-verbs" % "6.3.0"
    val hmrcHttpException = "uk.gov.hmrc" %% "http-exceptions" % "1.0.0"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
    val restAssured = "com.jayway.restassured" % "rest-assured" % "2.6.0" % "test"
    val mockito = "org.mockito" % "mockito-core" % "1.9.5" % "test"
    val hmrcTest = "uk.gov.hmrc" %% "hmrctest" % "2.3.0" % "test"
  }

}
