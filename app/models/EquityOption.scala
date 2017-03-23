package models


import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}

import play.api.libs.json.Reads
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class EquityOption(
                         optionSymbol : String,
                         strike: Double,
                         lastPrice:  Double,
                         bid: Double,
                         ask: Double,
                         expiry: LocalDate,
                         volume: Int,
                         openInterest: Int
                         //                         impliedVolatility: Double,
                         //                         isCall : Boolean,
                         //                         inTheMoney: Boolean,
                         //                         underlying: String
                       )


object EquityOption{

  implicit val equityOptionReads: Reads[EquityOption] = (
    ( __ \ "contractSymbol").read[String] and
      (__ \ "strike").read[Double] and
      (__ \ "lastPrice").read[Double] and
      (__ \ "bid").read[Double] and
      (__ \ "ask").read[Double] and
      (__ \ "expiration").read[Long].map(d => LocalDateTime.ofInstant(Instant.ofEpochMilli(d * 1000L), ZoneOffset.UTC).toLocalDate) and
      (__ \ "volume").read[Int] and
      (__ \ "openInterest").read[Int]
    ) (EquityOption.apply _)


  implicit val writes = Json.writes[EquityOption]

}