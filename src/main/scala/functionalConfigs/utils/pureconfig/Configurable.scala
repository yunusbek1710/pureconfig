package functionalConfigs.utils.pureconfig

import eu.timepit.refined.pureconfig._
import Configurable.serviceConfig
import Configuration.ServiceConfig
import pureconfig._
import pureconfig.generic.auto._

trait Configurable {
  val config: ServiceConfig = serviceConfig
}

object Configurable {
  private val serviceConfig: ServiceConfig = ConfigSource.default.loadOrThrow[ServiceConfig]
}
