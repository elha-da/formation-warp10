package formation.warp10.warp10

import scala.util.Random

import formation.warp10.data.MagasinStorage
import formation.warp10.{Configuration, Magasin, Product, Warp10Api}
import kneelnrise.warp10scala.model._

class ProductWarp10(magasinStorage: MagasinStorage, warp10Api: Warp10Api, configuration: Configuration) {
  val random = new Random()

  def registerAll(products: Seq[Product]): Unit = {
    warp10Api.pushAll(products.flatMap(generateGTSEntries))
  }

  def register(product: Product): Unit = {
    warp10Api.pushAll(generateGTSEntries(product))
  }

  def generateGTSEntries(product: Product): Seq[GTS] = {
    magasinStorage.findAll().flatMap { magasin =>
      (1 to 5).map { i =>
        val time = configuration.nowAtOneTime + (i * 86400)
        productToGTS(product, time, magasin)
      }
    }
  }

  private def productToGTS(product: Product, time: Long, magasin: Magasin): GTS = {
    GTS(
      ts = Some(time),
      coordinates = Some(magasin.coordinates),
      elev = None,
      name = "org.test.plain.product",
      labels = Map("productId" -> product.id, "storeId" -> magasin.id),
      value = GTSDoubleValue(randomPrice(product).toDouble)
    )
  }

  private def randomPrice(product: Product): BigDecimal = {
    // reference price * [0.5, 1.5]
    product.referencePrice * (random.nextDouble() + 0.5)
  }
}
