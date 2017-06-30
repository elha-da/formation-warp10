package formation.warp10

import kneelnrise.warp10scala.model.Coordinates

trait Api {
  def allMagasins(filters: MagasinFilter): Seq[Magasin]

  def nearestMagasin(coordinates: Coordinates): Magasin

  def allProducts(): Seq[Product]

  def searchMagasinByProduct(productSearchMagasinFilter: ProductSearchMagasinFilter): Seq[ProductSearchMagasinResult]

  def productEvolution(productId: String): ProductPriceEvolution

  def updateProductPrice(productId: String, price: BigDecimal): Unit
}