package controllers

import play.api.Logger
import play.api.i18n.Langs
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.YService
import services.TradierService
import scala.concurrent.ExecutionContext

class DataController (yahooService : YService, tradierService: TradierService)(implicit ec: ExecutionContext) extends Controller {

  def getQuote(symbol:String) = Action.async {
    Logger.info(s"Loading options for $symbol")
    val result = yahooService.getQuotesFromYahoo(symbol)
    result.map{ r=> Ok(Json.toJson(r)) }

  }

  def getOptionsByExpiration(symbol: String, expiration: Long)= Action.async {
    println("test")
    val result = yahooService.getOptionsByExpiration(symbol, expiration)
    result.map{ r=> Ok(Json.toJson(r)) }
  }



  def optionChain(symbol: String)= Action.async {
//    implicit val formats = Json.format[String]
    val result = tradierService.getOptionChain(symbol)
    result.map{r => Ok(Json.toJson(r))}
  }

  def tradier (symbol: String)= Action.async {
    val result = tradierService.getQuote(symbol)
    result.map{r => Ok(Json.toJson(r))}
  }

  def expirations(symbol: String) = Action.async {
    val result = tradierService.getExpirations(symbol)
    result.map{r => Ok(Json.toJson(r))}
  }
}