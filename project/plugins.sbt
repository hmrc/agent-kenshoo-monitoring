resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)


addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "3.0.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("uk.gov.hmrc" % "sbt-play-cross-compilation" % "2.0.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.16")

val playPlugin =
  if (sys.env.get("PLAY_VERSION").contains("2.8"))
    "com.typesafe.play" % "sbt-plugin" % "2.8.6"
  else if (sys.env.get("PLAY_VERSION").contains("2.7"))
    "com.typesafe.play" % "sbt-plugin" % "2.7.7"
  else
    "com.typesafe.play" % "sbt-plugin" % "2.6.25"

addSbtPlugin(playPlugin)
