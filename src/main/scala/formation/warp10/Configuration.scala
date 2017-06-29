package formation.warp10

import com.typesafe.config.ConfigFactory

class Configuration {
  private val config = ConfigFactory.load()

  lazy val readToken: String = config.getString("formation.warp10.tokens.read")

  lazy val writeToken: String = config.getString("formation.warp10.tokens.write")

  lazy val host: String = config.getString("formation.warp10.api.host")

  lazy val port: Int = config.getInt("formation.warp10.api.port")
}
