/*
 * Copyright 2020 HM Revenue & Customs
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

import com.codahale.metrics.{Meter, Timer}
import org.mockito.BDDMockito._
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.Matchers
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext.fromLoggingDetails

import scala.concurrent.Future
import com.codahale.metrics.MetricRegistry
import org.mockito.ArgumentMatcher
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.{HeaderCarrier, HttpException}
import uk.gov.hmrc.play.test.UnitSpec

class HttpAPIMonitorSpec extends UnitSpec with Matchers {

  implicit val hc = HeaderCarrier()

  def greaterThanOrEqualTo(l: Long): ArgumentMatcher[lang.Long] = new ArgumentMatcher[lang.Long] {
    override def matches(argument: lang.Long): Boolean = argument >= l
  }

  "monitor" should {
    "record invocation rate, average response time and error rate" in new HttpAPIMonitorTest {
      given(kenshooRegistry.timer("Timer-servicename")).willReturn(kenshooTimer)
      given(kenshooRegistry.meter("Http4xxErrorCount-servicename")).willReturn(errorMeter4xx)

      try {
        await(monitor("servicename") {
          Future({Thread.sleep(10); throw new HttpException("foobar", 400)})
        })
      } catch {
        case ex: Throwable => ()
      }
      verify(kenshooTimer).update(longThat(greaterThanOrEqualTo(10000000L)), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
      verify(errorMeter4xx).mark()
    }
  }
}

class HttpAPIMonitorTest extends HttpAPIMonitor with MockitoSugar {
  val kenshooRegistry = mock[MetricRegistry]
  val kenshooTimer = mock[Timer]
  val errorMeter4xx = mock[Meter]
}
