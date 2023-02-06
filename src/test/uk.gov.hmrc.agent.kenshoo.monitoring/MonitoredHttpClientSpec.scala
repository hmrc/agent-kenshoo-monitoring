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

import java.lang
import java.util.concurrent.TimeUnit
import com.codahale.metrics.{MetricRegistry, Timer}
import org.mockito.{ArgumentMatcher, Mockito}
import org.mockito.ArgumentMatchers.longThat
import org.mockito.BDDMockito.given
import org.mockito.Mockito.{never, times, verify}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}
import uk.gov.hmrc.agent.kenshoo.monitoring.support.UnitSpec
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class MonitoredHttpClientSpec extends UnitSpec with MockitoSugar with BeforeAndAfterEach {
  val http: HttpClient = mock[HttpClient]
  val httpAPIs = Map("/test/endpoint" -> "testEndpoint")
  val testEndpointTimer: Timer = mock[Timer]

  val kenshooRegistry: MetricRegistry = mock[MetricRegistry]

  implicit val hc: HeaderCarrier = HeaderCarrier()

  override def beforeEach(): Unit = {

    super.beforeEach()
    Mockito.reset(kenshooRegistry)
    Mockito.reset(testEndpointTimer)
  }

  def greaterThanOrEqualTo(l: Long): ArgumentMatcher[lang.Long] = new ArgumentMatcher[lang.Long] {
    override def matches(argument: lang.Long): Boolean = argument >= l
  }

  private val monitoredHttpClient = new MonitoredHttpClientTest(http, kenshooRegistry, httpAPIs)

  "doGet" should {

    "time the request if the url is allow-listed" in {

      given(kenshooRegistry.timer("Timer-ConsumedAPI-testEndpoint-GET")).willReturn(testEndpointTimer)

      given(http.GET[HttpResponse]("/test/endpoint")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doGet("/test/endpoint"))

      verify(testEndpointTimer).update(longThat(greaterThanOrEqualTo(10L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
      verify(kenshooRegistry, times(1)).getTimers()
    }

    "not time the request if the url is not allow-listed" in {

        given(http.GET[HttpResponse]("/test/notmonitored")).willReturn(Future.successful(HttpResponse(200, "")))

        await(monitoredHttpClient.doGet("/test/notmonitored"))
        verify(kenshooRegistry, never()).getTimers()
    }
  }

  "doPost" should {

    "time the request if the url is allow-listed" in {

      given(kenshooRegistry.timer("Timer-ConsumedAPI-testEndpoint-POST")).willReturn(testEndpointTimer)
      given(http.POST[String, HttpResponse]("/test/endpoint","something",Seq.empty)).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doPost("/test/endpoint","something", Seq.empty))
      verify(testEndpointTimer).update(longThat(greaterThanOrEqualTo(10L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }

    "not time the request if the url is allow-listed" in {

        given(http.POST[String, HttpResponse]("/test/notmonitored","something",Seq.empty)).willReturn(Future.successful(HttpResponse(200, "")))

        await(monitoredHttpClient.doPost("/test/notmonitored", "something", Seq.empty))
        verify(kenshooRegistry, never()).getTimers()
    }
  }

  "doPostEmpty" should {

    "time the request if the url is allow-listed" in {

      given(kenshooRegistry.timer("Timer-ConsumedAPI-testEndpoint-POST")).willReturn(testEndpointTimer)
      given(http.POSTEmpty[HttpResponse]("/test/endpoint")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doEmptyPost("/test/endpoint"))
      verify(testEndpointTimer).update(longThat(greaterThanOrEqualTo(10L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }

    "not time the request if the url is allow-listed" in  {

        given(http.POSTEmpty[HttpResponse]("/test/notmonitored")).willReturn(Future.successful(HttpResponse(200, "")))

        await(monitoredHttpClient.doEmptyPost("/test/notmonitored"))
        verify(kenshooRegistry, never()).getTimers()
      }
  }

  "doPut" should {
    "time the request if the request is allow-listed" in {
      given(kenshooRegistry.timer("Timer-ConsumedAPI-testEndpoint-PUT")).willReturn(testEndpointTimer)
      given(http.PUT[String, HttpResponse]("/test/endpoint","something")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doPut("/test/endpoint","something"))
      verify(testEndpointTimer).update(longThat(greaterThanOrEqualTo(10L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }

    "not time the request if the url is allow-listed" in {

      given(http.PUT[String, HttpResponse]("/test/notmonitored","something")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doPut("/test/notmonitored", "something"))
      verify(kenshooRegistry, never()).getTimers()
    }
  }

  "doDelete" should {
    "time the request if the request is allow-listed" in {
      given(kenshooRegistry.timer("Timer-ConsumedAPI-testEndpoint-DELETE")).willReturn(testEndpointTimer)
      given(http.DELETE[HttpResponse]("/test/endpoint")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doDelete("/test/endpoint"))
      verify(testEndpointTimer).update(longThat(greaterThanOrEqualTo(1L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }

    "not time the request if the url is allow-listed" in {
      given(http.DELETE[HttpResponse]("/test/notmonitored")).willReturn(Future.successful(HttpResponse(200, "")))

      await(monitoredHttpClient.doDelete("/test/notmonitored"))
      verify(kenshooRegistry, never()).getTimers()
    }
  }
}

class  MonitoredHttpClientTest(
                                override val http: HttpClient,
                                override val kenshooRegistry: MetricRegistry,
                                override val httpAPIs: Map[String, String])(implicit val ec: ExecutionContext) extends MonitoredHttpClient
