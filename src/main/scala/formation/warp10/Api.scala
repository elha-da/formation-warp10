package formation.warp10

import formation.warp10.data.{MagasinStorage, ProductStorage}
import formation.warp10.warp10.{MagasinWarp10, ProductWarp10}
import kneelnrise.warp10scala.model.Coordinates

trait Api {
  def allMagasins(filters: MagasinFilter): Seq[Magasin]

  def nearestMagasin(coordinates: Coordinates): Magasin

  def allProducts(): Seq[Product]

  def findProduct(barcode: String): Option[Product]

  def searchMagasinByProduct(productSearchMagasinFilter: ProductSearchMagasinFilter): Seq[ProductSearchMagasinResult]

  def productEvolution(productId: String): ProductPriceEvolution

  def updateProductPrice(productId: String, price: BigDecimal): Unit
}

class ApiImpl(
  magasinStorage: MagasinStorage,
  productStorage: ProductStorage,
  magasinWarp10: MagasinWarp10,
  productWarp10: ProductWarp10
) extends Api {
  override def allMagasins(filters: MagasinFilter): Seq[Magasin] = magasinStorage.findAll()

  override def nearestMagasin(coordinates: Coordinates): Magasin = ???

  override def allProducts(): Seq[Product] = productStorage.findAll()

  override def findProduct(barcode: String): Option[Product] = productStorage.find(barcode)

  override def searchMagasinByProduct(productSearchMagasinFilter: ProductSearchMagasinFilter): Seq[ProductSearchMagasinResult] = ???

  override def productEvolution(productId: String): ProductPriceEvolution = ???

  override def updateProductPrice(productId: String, price: BigDecimal): Unit = ???
}