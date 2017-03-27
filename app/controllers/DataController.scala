package controllers

import play.api.Logger
import play.api.i18n.Langs
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.YService

import scala.concurrent.ExecutionContext

class DataController (yahooService : YService)(implicit ec: ExecutionContext) extends Controller {

  def getQuote(symbol:String) = Action.async {
    Logger.info(s"Loading options for $symbol")
    val result = yahooService.getQuotesFromYahoo(symbol)
    result.map{ r=> Ok(Json.toJson(r)) }

  }

  def getOptionsByExpiration(symbol: String, expiration: Long)= Action.async {
    val result = yahooService.getOptionsByExpiration(symbol, expiration)
    result.map{ r=> Ok(Json.toJson(r)) }
  }
}