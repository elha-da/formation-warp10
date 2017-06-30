package formation.warp10

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import formation.warp10.data.{MagasinStorage, ProductStorage}
import formation.warp10.mock.MockApi
import formation.warp10.warp10.{MagasinWarp10, ProductWarp10}
import kneelnrise.warp10scala.model.Warp10Configuration

object Boot extends App {
  val configuration = new Configuration()
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val warp10Configuration = Warp10Configuration("", configuration.readToken, configuration.writeToken, Warp10Configuration.ApiVersion.ZERO)
  val warp10Api = new Warp10Api(configuration)
  val router = new Router(new MockApi)

  val magasinStorage = new MagasinStorage(configuration.magasinStorageDirectory)
  val productStorage = new ProductStorage(configuration.productsStorageDirectory)

  val magasinWarp10 = new MagasinWarp10(warp10Api, configuration)
  val productWarp10 = new ProductWarp10(magasinStorage, warp10Api, configuration)

  loadIntoWarp10()

  val bindingFuture = Http().bindAndHandle(router.route, "localhost", 9000)


  def loadIntoWarp10(): Unit = {
    magasinWarp10.registerAll(magasinStorage.findAll())
    productWarp10.registerAll(productStorage.findAll())
  }
}
