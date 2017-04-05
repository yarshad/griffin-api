package utilities

import java.time.LocalDate
import java.time.format.DateTimeFormatter


object DateImplicits {

  implicit def ISODateConverter(date: String ): LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)

}
