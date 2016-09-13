/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.agent.kenshoo.monitoring

import org.scalatest.Matchers
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Headers, RequestHeader, ResponseHeader, Result}
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class MonitoringFilterSpec extends UnitSpec {

  implicit val hc = HeaderCarrier()

  "monitoring filter" should {
    "monitor known incoming requests" in new MonitoringFilterTestImp {
      await(apply(requestHeader => Future(Result(ResponseHeader(200), Enumerator())))(TestRequestHeader("/agent/agentcode", "GET")))
      assertRequestIsMonitoredAs("API-Agent-GET")
    }

    "do not monitor unknown incoming requests" in new MonitoringFilterTestImp {
      await(apply(requestHeader => Future(Result(ResponseHeader(200), Enumerator())))(TestRequestHeader("/agent/client/empref", "GET")))
      assertRequestIsNotMonitored()
    }
  }
}

case class TestRequestHeader(expectedUri: String, expectedMethod: String) extends RequestHeader {
  override def id: Long = -1L
  override def secure: Boolean = false
  override def uri: String = expectedUri
  override def remoteAddress: String = ""
  override def queryString: Map[String, Seq[String]] = Map()
  override def method: String = expectedMethod
  override def headers: Headers = new Headers {
    override protected val data: Seq[(String, Seq[String])] = Seq()
  }
  override def path: String = ""
  override def version: String = ""
  override def tags: Map[String, String] = Map()
}


class MonitoringFilterTestImp extends MonitoringFilter with Matchers {
  override val urlPatternToNameMapping: Map[String, String] = Map("/agent/agentcode" -> "Agent")

  var serviceName : String = ""

  override def monitor[T](serviceName: String)(function: => Future[T])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[T] = {
    this.serviceName = serviceName
    function
  }

  def assertRequestIsMonitoredAs(expectedServiceName: String): Unit = {
    serviceName shouldBe expectedServiceName
  }

  def assertRequestIsNotMonitored(): Unit = {
    serviceName shouldBe ""
  }

}
