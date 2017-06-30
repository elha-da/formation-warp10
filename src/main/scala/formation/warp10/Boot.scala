package formation.warp10

import java.io.File
import java.nio.file.{Files, Path, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import formation.warp10.mock.MockApi
import kneelnrise.warp10scala.model.Warp10Configuration
import kneelnrise.warp10scala.model._

import scala.util.Random

object Boot extends App {
  val configuration = new Configuration()
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val warp10Configuration = Warp10Configuration("", configuration.readToken, configuration.writeToken, Warp10Configuration.ApiVersion.ZERO)
  val warp10Api = new Warp10Api(configuration)
  val router = new Router(new MockApi)

  val bindingFuture = Http().bindAndHandle(router.route, "localhost", 9000)
  val nowAtOneTime = 1467278467000l

  val filePathLoire = Paths.get("./src/main/resources/data/magasins/loire-atlantique.csv")
  val filePathParis = Paths.get("./src/main/resources/data/magasins/paris.csv")
  var inMemoryStores : Map[Int, GTS] = Map()
  val inMemoryProducts : Map[String, String] = Map(("50000" -> "Chocolat Milka"), ("50001" -> "Chips a l'ancienne"),
    ("50002" -> "Leffe Royale"),("50003" -> "Fondant au chocolat Brossard"),("50004" -> "Salade verte"),("50005" ->  "Eau de Javel LaCroix"),
    ("50006" -> "Eponge Grat-grat Spontex"),("50007" -> "Raviolis en boite Buitoni"),("50008" -> "Papier toilette quadruple epaisseur"),("50009" -> "Nintendo Switch"))
  var lastStoreId = 0;

  if (Files.exists(filePathLoire) && Files.exists(filePathParis)) {
    initStores(filePathLoire)
    //initStores(filePathParis)
    initProducts()
  }

  def generateProductGTS(nowAtOneTime: Long, priceLow: Double, priceUp: Double, coordinates: Option[Coordinates], productId : String, storeId: String): GTS = {
    val r = new Random()
    val price = priceLow + (priceUp - priceLow) * r.nextDouble();
    GTS(
      ts = Some(nowAtOneTime),
      coordinates = coordinates,
      elev = None,
      name = "org.test.plain.product",
      labels = Map("productId" -> productId, "storeId" -> storeId),
      value = GTSDoubleValue(price)
    )
  }

  def initProducts() = {
    inMemoryStores.foreach{ case (id, store) => {
      val storeId = store.labels.get("id").getOrElse("UnknownStore")
        inMemoryProducts.foreach{case (productId, productName) => {
        var time = nowAtOneTime
        val r = new Random()
        val factor = -2 + (4) * r.nextDouble()
        val priceDiff = 10 + (90) * r.nextDouble()
        var priceLow = 3 + priceDiff
        var priceUp = 9 + priceDiff
        for (i <- 1 to 360) {
            var productGts = generateProductGTS(time,priceLow, priceUp, store.coordinates, productId, storeId)
            warp10Api.push(productGts)
            time += 86400 // We add a day
            // Changing the price range for the product
            // using math.abs to avoid negative prices (duh)
            priceLow = math.abs(priceLow + factor)
            priceUp = math.abs(priceUp + factor)
          }
      }}
    }}
  }

  def initStores(filePath : Path): Unit ={
    Files.readAllLines(filePath)
      .forEach(line => {
        lastStoreId += 1
        val elems = line.split(';')
        val (name : String, lat : Double, long : Double) = (elems(0), elems(1).toDouble , elems(2).toDouble )
        val gts = GTS(
          ts = Some(nowAtOneTime),
          coordinates = Some(Coordinates(lat, long)),
          elev = None,
          name = "org.test.plain.store",
          labels = Map("name" -> name, "id" -> lastStoreId.toString),
          value = GTSStringValue(name)
        )
        warp10Api.push(gts)
        inMemoryStores += (lastStoreId -> gts)
      })

  }
}
