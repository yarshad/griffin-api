import javax.inject._

import filters.LoggingFilter
import play.api._
import play.api.http.HttpFilters
import play.filters.cors.CORSFilter


@Singleton
class Filters @Inject() (env: Environment, corsFilter: CORSFilter,loggingFilter: LoggingFilter) extends HttpFilters {

  override val filters = {
    // Use the example filter if we're running development mode. If
    // we're running in production or test mode then don't use any
    // filters at all.
    //    if (env.mode == Mode.Dev) Seq(exampleFilter) else Seq.empty

    Seq(corsFilter, loggingFilter)
  }

}
