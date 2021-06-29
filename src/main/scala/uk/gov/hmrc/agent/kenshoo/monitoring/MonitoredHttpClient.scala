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

import play.api.Logger
import play.api.libs.json.Writes
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.concurrent.{ExecutionContext, Future}

trait MonitoredHttpClient extends HttpAPIMonitor {

  val http: HttpClient
  implicit val ec: ExecutionContext

  private case class HttpAPI(urlPattern: String, name: String)

  private case class HttpAPINames(urlPatternAndName: Map[String, String]) {
    val httpAPIs = urlPatternAndName.map { httpApi => HttpAPI(httpApi._1, httpApi._2) }

    def nameFor(method: String, url: String): Option[String] = {
      httpAPIs.find(downstreamService => url.matches(downstreamService.urlPattern)) match {
        case Some(service) => Some(s"ConsumedAPI-${service.name}-$method")
        case None => None
      }
    }
  }

  val httpAPIs: Map[String, String]
  private lazy val apiNames = HttpAPINames(httpAPIs)

  private def monitorUrl(method: String, url: String)(func: => Future[HttpResponse]): Future[HttpResponse] = {
    apiNames.nameFor(method, url) match {
      case None =>
        Logger(this.getClass).debug(s"ConsumedAPI-Not-Monitored: $method-$url")
        func
      case Some(name) => {
        monitor(name) {
          func
        }
      }
    }
  }

  private def monitorUrlWithBody[A](method: String, url: String)(func: => Future[HttpResponse]): Future[HttpResponse] = {
    apiNames.nameFor(method, url) match {
      case None =>
        Logger(this.getClass).debug(s"ConsumedAPI-Not-Monitored: $method-$url")
        func
      case Some(name) => {
        monitor(name) {
          func
        }
      }
    }
  }


 def doGet( url: String)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
   monitorUrl("GET", url) {
     http.GET[HttpResponse](url)
   }
 }

  def doPost[A](url: String, body: A, headers: Seq[(String, String)])(implicit rds: Writes[A], hc: HeaderCarrier): Future[HttpResponse] = {
    monitorUrlWithBody("POST", url) {
      http.POST[A,HttpResponse](url, body, headers)
    }
  }

  def doEmptyPost[A](url: String)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
    monitorUrl("POST", url) {
      http.POSTEmpty(url)
    }
  }

  def doPut[A](url: String, body: A)(implicit rds: Writes[A], hc: HeaderCarrier): Future[HttpResponse] = {
    monitorUrlWithBody("PUT", url) {
      http.PUT[A,HttpResponse](url, body)
    }
  }

  def doDelete(url: String)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
    monitorUrl("DELETE", url) {
      http.DELETE[HttpResponse](url)
    }
  }
}
