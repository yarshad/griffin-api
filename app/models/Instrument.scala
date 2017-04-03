package models

import java.time.LocalDateTime

import play.api.libs.json.Json

case class Instrument(symbol : String, spot: Double, asOfDate: LocalDateTime, expirations: Seq[Long], calls: Seq[EquityOption], puts: Seq[EquityOption])

object Instrument{
  implicit val format = Json.format[Instrument]
}