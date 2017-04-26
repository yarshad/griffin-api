package services

import controllers.{TradierController, YahooController}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext.Implicits.global


trait ServicesModule extends AhcWSComponents{

lazy val yahooController= wire[YahooController]
lazy val tradierController = wire[TradierController]
lazy val yahooService = new YService(wsClient)
lazy val tradierService = new TradierService(wsClient)

}
