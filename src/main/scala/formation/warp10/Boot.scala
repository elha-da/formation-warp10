package formation.warp10

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import formation.warp10.mock.MockApi
import kneelnrise.warp10scala.model.Warp10Configuration

object Boot extends App {
  val configuration = new Configuration()
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val warp10Configuration = Warp10Configuration("", configuration.readToken, configuration.writeToken, Warp10Configuration.ApiVersion.ZERO)
  val warp10Api = new Warp10Api(configuration)
  val router = new Router(new MockApi)

  val bindingFuture = Http().bindAndHandle(router.route, "localhost", 9000)
}
