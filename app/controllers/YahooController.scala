package controllers

import java.time.{LocalDate, ZoneOffset}
import utilities.DateImplicits._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.YService

import scala.concurrent.ExecutionContext

class YahooController(yahooService: YService)(implicit ec: ExecutionContext) extends Controller {

  def getQuote(symbol:String) = Action.async {
    Logger.info(s"[YAHOO] -- Loading options for $symbol")
    val result = yahooService.getQuotesFromYahoo(symbol)
    result.map{ r=> Ok(Json.toJson(r)) }
  }

  def getOptionsByExpiration(symbol: String, expiration: String)= Action.async{

    Logger.info(s"[YAHOO] -- Loading CHAIN for $symbol -  $expiration")

    val dt : LocalDate = expiration
    val expiry: Long = dt.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli / 1000
    val result = yahooService.getOptionsByExpiration(symbol, expiry)
    result.map{ r=> Ok(Json.toJson(r)) }
  }
}
