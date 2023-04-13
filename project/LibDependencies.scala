import sbt._

object LibDependencies {
  private val bootstrapVer = "7.15.0"

  val compile = Seq(
    "com.typesafe.play"      %% "play-json"                 % "2.9.4",
    "uk.gov.hmrc"            %% "domain"                    % "8.2.0-play-28",
    "com.kenshoo"            %% "metrics-play"              % "2.7.3_0.8.2",
    "uk.gov.hmrc"            %% "bootstrap-backend-play-28" % bootstrapVer
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"    % bootstrapVer % Test,
    "org.scalatest"          %% "scalatest"                 % "3.2.15"     % Test,
    "org.pegdown"            %  "pegdown"                   % "1.6.0"      % Test,
    "org.scalacheck"         %% "scalacheck"                % "1.17.0"     % Test,
    "org.mockito"            %  "mockito-core"              % "5.2.0"      % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"        % "5.1.0"      % Test
  )

}
