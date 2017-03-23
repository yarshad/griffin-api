package models

import play.api.libs.json.Json

case class YahooResult(underlyingSymbol : String, expirationDates: Seq[Long], strikes: Seq[Double])

object YahooResult{

  implicit val format = Json.format[YahooResult]

}
