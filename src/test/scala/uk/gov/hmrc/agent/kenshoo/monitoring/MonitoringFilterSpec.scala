/*
 * Copyright 2023 HM Revenue & Customs
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

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.codahale.metrics.MetricRegistry
import org.mockito.Mockito.mock
import org.scalatest.matchers.must.Matchers
import play.api.http.HttpEntity
import play.api.mvc.{Headers, RequestHeader, ResponseHeader, Result}
import uk.gov.hmrc.agent.kenshoo.monitoring.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier
import org.scalatest.matchers.should.Matchers._
import play.api.libs.typedmap.TypedMap
import play.api.mvc.request.{RemoteConnection, RequestTarget}
import play.api.test.Helpers.{await, defaultAwaitTimeout}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class MonitoringFilterSpec extends UnitSpec {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  "monitoring filter" should {
    // TODO - is test flakey?
//    "monitor known incoming requests" in new MonitoringFilterTestImp {
//      val reqHeader: TestRequestHeader = TestRequestHeader("/agent/agentcode", "GET")
//      await(apply(_ => Future(Result(ResponseHeader(200), HttpEntity.NoEntity)))(reqHeader))
//      assertRequestIsMonitoredAs("API-Agent-GET")
//    }

    "do not monitor unknown incoming requests" in new MonitoringFilterTestImp {
      val reqHeader: TestRequestHeader = TestRequestHeader("/agent/client/empref", "GET")
      await(apply(_ => Future(Result(ResponseHeader(200), HttpEntity.NoEntity)))(reqHeader))
      assertRequestIsNotMonitored()
    }
  }
}

case class TestRequestHeader(expectedUri: String, expectedMethod: String) extends RequestHeader {
  override def method: String = expectedMethod
  override def headers: Headers = new Headers(Seq())
  override def version: String = ""
  override def connection: RemoteConnection = RemoteConnection("", secure = false, None)
  override def target: RequestTarget = RequestTarget(expectedUri, "", Map())
  override def attrs: TypedMap = TypedMap()
}


class MonitoringFilterTestImp
  extends MonitoringFilter(Map("/agent/agentcode" -> "Agent"), mock(classOf[MetricRegistry]))
    with Matchers {
  implicit val system: ActorSystem = ActorSystem("Tests")
  override implicit val mat: Materializer = ActorMaterializer()

  var serviceName : String = ""

   def monitor[T](serviceName: String)(function: => Future[T])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[T] = {
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
