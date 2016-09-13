# Agent Monitoring Wrapper

[![Build Status](https://travis-ci.org/hmrc/agent-kenshoo-monitoring.svg?branch=master)](https://travis-ci.org/hmrc/agent-kenshoo-monitoring) [ ![Download](https://api.bintray.com/packages/hmrc/releases/agent-kenshoo-monitoring/images/download.svg) ](https://bintray.com/hmrc/releases/agent-kenshoo-monitoring/_latestVersion)

Integrates graphite metrics via Kenshoo into Play applications, for in-bound and out-bound calls. 
White-listing of URLs is possible for both.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

## Installing
 
Include the following dependency in your SBT build
 
``` scala
resolvers += Resolver.bintrayRepo("hmrc", "releases")
 
libraryDependencies += "uk.gov.hmrc" %% "agent-kenshoo-monitoring" % "[INSERT-VERSION]"
```
