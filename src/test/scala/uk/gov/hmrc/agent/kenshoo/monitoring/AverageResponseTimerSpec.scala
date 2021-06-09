/*
 * Copyright 2021 HM Revenue & Customs
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

import java.util.concurrent.TimeUnit
import java.{lang, util}

import com.codahale.metrics.{MetricRegistry, Timer}
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers._
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AverageResponseTimerSpec extends UnitSpec {

  implicit val hc = HeaderCarrier()

  def greaterThanOrEqualTo(l: Long): ArgumentMatcher[lang.Long] = new ArgumentMatcher[lang.Long] {
    override def matches(argument: lang.Long): Boolean = argument >= l
  }

  "timer" should {
    "update kenshoo timer for service if one already exists" in new AverageResponseTimerTest {
      given(kenshooRegistry.getTimers).willReturn(new util.TreeMap[String, Timer]() {
        {
          put("Timer-servicename", kenshooTimer)
        }
      })

      await(timer("servicename") {
        Future(Thread.sleep(1000))
      })

      verify(kenshooTimer).update(longThat(greaterThanOrEqualTo(millisToNano(1000))), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }

    "update kenshoo timer for service if one doesn't exists" in new AverageResponseTimerTest {
      given(kenshooRegistry.timer("Timer-servicename")).willReturn(kenshooTimer)

      await(timer("servicename") {
        Future(Thread.sleep(100))
      })

      verify(kenshooTimer).update(longThat(greaterThanOrEqualTo(millisToNano(100))), org.mockito.ArgumentMatchers.eq(TimeUnit.NANOSECONDS))
    }
  }

  def millisToNano(millis: Long): Long = millis * Math.pow(10, 6).asInstanceOf[Long]

}

class AverageResponseTimerTest extends AverageResponseTimer with MockitoSugar {
  val kenshooRegistry = mock[MetricRegistry]
  val kenshooTimer: Timer = mock[Timer]
}
