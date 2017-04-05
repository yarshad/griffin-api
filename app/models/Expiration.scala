package models
import java.time.LocalDate

import play.api.libs.json.Json

case class Expiration(date: LocalDate, daysToExpiry: Long, isWeekly: Boolean)


object Expiration{
  implicit val format = Json.format[Expiration]
}