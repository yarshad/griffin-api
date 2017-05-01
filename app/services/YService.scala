package services


import models.{EquityOption, Instrument, YahooResult}
import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.ExecutionContext
//import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.Future

class YService(ws: WSClient) {

  def getQuotesFromYahoo(symbol: String) (implicit ec: ExecutionContext) : Future[Instrument] = {
    val url = s"https://query2.finance.yahoo.com/v7/finance/options/$symbol"
    val request: WSRequest = ws.url(url)
    val response = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }
        val output = response.body
        val json = Json.parse(output)
        responseParser(json)
      }
    }
    response
  }

  private def responseParser(json: JsValue) : Instrument = {

    val isError = (json \ "optionChain" \ "error").asOpt[String]
    val result = (json \ "optionChain" \ "result").as[JsArray].head
    val yahooResult = result.as[YahooResult]
    val callOptions = (json \\ "calls").map(_.as[Seq[EquityOption]])
    val putOptions = (json \\ "puts").map(_.as[Seq[EquityOption]])
    val spot = (result \ "quote" \ "regularMarketPrice").as[Double]
    val marketTime = (result \ "quote" \ "regularMarketTime").as[Long]
    val changePct = (result \ "quote" \ "regularMarketChangePercent").as[Double]
    val change = (result \ "quote" \ "regularMarketChange").as[Double]
    val asOfDate: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(marketTime * 1000L), ZoneId.systemDefault())
    val expiries = yahooResult.expirationDates.map(d => LocalDateTime.ofInstant(Instant.ofEpochMilli(d * 1000L), ZoneOffset.UTC).toLocalDate)

//    println(yahooResult.expirationDates.head)
    callOptions.flatten
    Instrument(yahooResult.underlyingSymbol, spot, asOfDate, expiries, callOptions.flatten, putOptions.flatten)
  }

  def getOptionsByExpiration(symbol: String, expiration: Long)(implicit ec: ExecutionContext) : Future[Instrument] = {

    val url = s"https://query2.finance.yahoo.com/v7/finance/options/$symbol?date=$expiration"
    val request: WSRequest = ws.url(url)
    val response = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }
        val output = response.body
        val json = Json.parse(output)
        responseParser(json)
      }
    }
    response
  }
}
