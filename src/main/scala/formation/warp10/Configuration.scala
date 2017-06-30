package formation.warp10

import java.nio.file.{Path, Paths}

import com.typesafe.config.ConfigFactory

class Configuration {
  private val config = ConfigFactory.load()

  @deprecated("Here because it seems convenient for the moment, to be replaced with something more usable")
  lazy val nowAtOneTime = 1467278467000l

  lazy val magasinStorageDirectory: Path = Paths.get("src", "main", "resources", "data", "magasins")

  lazy val productsStorageDirectory: Path = Paths.get("src", "main", "resources", "data", "magasins")

  lazy val readToken: String = config.getString("formation.warp10.tokens.read")

  lazy val writeToken: String = config.getString("formation.warp10.tokens.write")

  lazy val host: String = config.getString("formation.warp10.api.host")

  lazy val port: Int = config.getInt("formation.warp10.api.port")
}
