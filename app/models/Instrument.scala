package models

import play.api.libs.json.Json

case class Instrument(symbol : String, expirations: Seq[Long], calls: Seq[EquityOption], puts: Seq[EquityOption])

object Instrument{
  implicit val format = Json.format[Instrument]
}