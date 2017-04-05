package services

import java.time.LocalDate
import models.{Expiration, Instrument}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest}
import java.time.temporal.ChronoUnit.DAYS
import scala.concurrent.{ExecutionContext, Future}

class TradierService (ws: WSClient){

  val tradierHost = "https://sandbox.tradier.com"

  private def makeRequest(url: String) = {
    val request: WSRequest = ws.url(url)
      .withHeaders(("Accept", "application/json"), ("Authorization", "Bearer 8kY8wdo5wvFAr5tYj6MVU7We0lTL"))
    request
  }

  def getExpirations(symbol: String)  (implicit ec: ExecutionContext) = {

    val url = s"${tradierHost}/v1/markets/options/expirations?symbol=$symbol"
    val request: WSRequest = makeRequest(url)
    val output = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }
        else{
          val output = response.body
          val json = Json.parse(output)
          val dates = (json \ "expirations" \ "date").as[Seq[LocalDate]]
          val expirations = dates.map(d => Expiration(d, DAYS.between(LocalDate.now, d),false))
          expirations
        }}}
    output
  }

  def getPriceHistory(symbol: String)  (implicit ec: ExecutionContext) = {

    val url = s"${tradierHost}/v1/markets/history?symbol=$symbol&interval=daily&start=01-01-2017"
    val request: WSRequest = makeRequest(url)
    val output = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }
        else{
          val output = response.body
          val json = Json.parse(output)
          val dates = (json \ "expirations" \ "date").as[Seq[LocalDate]]
          val expirations = dates.map(d => Expiration(d, DAYS.between(LocalDate.now, d),false))
          expirations
        }}}
    output
  }

  def getQuote(symbol: String)(implicit ec: ExecutionContext) = {
    val url = s"${tradierHost}/v1/markets/quotes?symbols=$symbol"
    val request: WSRequest = makeRequest(url)

    val output = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }

        else{
          val output = response.body
          val json = Json.parse(output)
          json
        }}}
    output
  }

  def getOptionChain(symbol: String) (implicit ec: ExecutionContext) = {
    val url = s"${tradierHost}/v1/markets/options/chains?symbol=$symbol&expiration=2017-04-07"
    val request: WSRequest = makeRequest(url)

    val response = request.get().map {
      response => {
        if (!(200 to 299).contains(response.status)) {
          sys.error(s"Received unexpected status ${response.status} : ${response.body}")
        }

        else{
          val output = response.body
          val json = Json.parse(output)
          json
        }}}
    response
  }


}
