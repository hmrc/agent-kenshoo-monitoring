# Agent Monitoring Wrapper

![](https://img.shields.io/github/v/release/hmrc/agent-kenshoo-monitoring)

Integrates graphite metrics via Kenshoo into Play applications, for in-bound and out-bound calls. 
Allow-listing of URLs is possible for both.

## Note

This library is currently intended for use by the Agent Services team. Since v4.5.0 the library no longer has a dependency on HttpVerbs. You will need an instance of HttpClient and an implicit ExecutionContext defined in scope.
Since v5, available across Scala versions 2.12 and 2.13.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

## Installing
 
Include the following dependency in your SBT build
 
``` scala
resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
 
libraryDependencies += "uk.gov.hmrc" %% "agent-kenshoo-monitoring" % "[INSERT-VERSION]"
```

test PR builder
