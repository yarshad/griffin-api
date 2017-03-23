package services

import controllers.{DataController}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

import scala.concurrent.ExecutionContext.Implicits.global


trait ServicesModule extends AhcWSComponents{

//  lazy val greetingService = wire[GreetingService]

    lazy val dataController = wire[DataController]


  lazy val yahooService = new YService(wsClient)

}
