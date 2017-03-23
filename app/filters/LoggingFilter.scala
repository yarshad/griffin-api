package filters

import play.api.Logger
import play.api.mvc.{EssentialAction, EssentialFilter, RequestHeader}
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class LoggingFilter extends EssentialFilter {

  def apply(nextFilter: EssentialAction) = new EssentialAction {

    val logger: Logger = Logger(this.getClass())

    def apply(requestHeader: RequestHeader) = {

      val startTime = System.currentTimeMillis

      nextFilter(requestHeader).map { result =>

        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime

        logger.info(s"${requestHeader.method} ${requestHeader.uri}" +
          s" toook ${requestTime}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> requestTime.toString)

      }
    }
  }
}