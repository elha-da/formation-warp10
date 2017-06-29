package formation.warp10

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import kneelnrise.warp10scala.model.Warp10Configuration

object Boot extends App {
  val configuration = new Configuration()
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val warp10Configuration = Warp10Configuration("", configuration.readToken, configuration.writeToken, Warp10Configuration.ApiVersion.ZERO)
  val warp10Api = new Warp10Api(configuration)

  val nowAtOneTime = 1498724494263l

  warp10Api.push(nowAtOneTime)
  warp10Api.read(nowAtOneTime)
}
