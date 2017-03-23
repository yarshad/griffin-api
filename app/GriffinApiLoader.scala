import com.softwaremill.macwire._
import controllers.Assets
import filters.LoggingFilter
import play.api.ApplicationLoader.Context
import play.api._
import play.api.i18n._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.filters.cors.{CORSConfig, CORSFilter}
import router.Routes
import services.ServicesModule



class GriffinApiLoader extends ApplicationLoader {
  def load(context: Context): Application = new GriffinComponents(context).application
}


class GriffinComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with ServicesModule
    with I18nComponents
    with AhcWSComponents
{
  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment)
  }

  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

  lazy val corsFilter = CORSFilter(CORSConfig.fromConfiguration(configuration))

  lazy val loggingFilter = new LoggingFilter
  lazy val filters = wire[Filters]

  override lazy val httpFilters: Seq[EssentialFilter] =
    Seq(loggingFilter,corsFilter)

}