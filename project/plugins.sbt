resolvers += Resolver.url("HMRC Sbt Plugin Releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

resolvers += Resolver.bintrayRepo("hmrc", "releases")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "1.14.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "1.16.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "0.17.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.19")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.16")

addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "0.15.0")
