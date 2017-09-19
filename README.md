# Agent Monitoring Wrapper

[![Build Status](https://travis-ci.org/hmrc/agent-kenshoo-monitoring.svg?branch=master)](https://travis-ci.org/hmrc/agent-kenshoo-monitoring) [ ![Download](https://api.bintray.com/packages/hmrc/releases/agent-kenshoo-monitoring/images/download.svg) ](https://bintray.com/hmrc/releases/agent-kenshoo-monitoring/_latestVersion)

Integrates graphite metrics via Kenshoo into Play applications, for in-bound and out-bound calls. 
White-listing of URLs is possible for both.

## Note

This library is currently intended for use by the Agent Services team.
It has a direct dependency on http-verbs which makes the upgrade path to Play 2.6 or later more difficult.
Until this is resolved we and PlatOps recommend that other teams avoid adding new dependencies on agent-kenshoo-monitoring.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

## Installing
 
Include the following dependency in your SBT build
 
``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")
 
libraryDependencies += "uk.gov.hmrc" %% "agent-kenshoo-monitoring" % "[INSERT-VERSION]"
```
