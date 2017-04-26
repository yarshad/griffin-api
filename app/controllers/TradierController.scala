package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{TradierService}

import scala.concurrent.ExecutionContext

class TradierController(tradierService : TradierService)(implicit ec: ExecutionContext) extends Controller {

  def optionChain(symbol: String)= Action.async {
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
