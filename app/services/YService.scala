package services


import models.{EquityOption, Instrument, YahooResult}
import java.time.{Instant, LocalDateTime, ZoneOffset}

import play.api.libs.json.{JsArray, Json}
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.ExecutionContext
//import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.Future

class YService(ws: WSClient) {

  def getQuotesFromYahoo(symbol: String) (implicit ec: ExecutionContext) : Future[Instrument] = {

    //    val wsClient = NingWSClient()

    val url = s"https://query2.finance.yahoo.com/v7/finance/options/$symbol"
    //    val url = s"https://www.google.com/finance/option_chain?q=NASDAQ:$symbol&output=json"

    val request: WSRequest = ws.url(url)

    val response = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }
        val output = response.body
        val json = Json.parse(output)

        val isError = (json \  "optionChain" \ "error").asOpt[String]
        val yahooResult = (json \  "optionChain" \ "result").as[JsArray].head.as[YahooResult]
        val callOptions = (json \\ "calls").map( _.as[Seq[EquityOption]])
        val putOptions = (json \\ "puts").map( _.as[Seq[EquityOption]])

        callOptions.flatten

        val expiries = yahooResult.expirationDates.map(d => LocalDateTime.ofInstant(Instant.ofEpochMilli(d * 1000L), ZoneOffset.UTC).toLocalDate)

        Instrument(yahooResult.underlyingSymbol, yahooResult.expirationDates, callOptions.flatten, putOptions.flatten)
      }
    }
    response
  }
}
