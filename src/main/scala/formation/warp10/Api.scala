package formation.warp10

trait Api {
  def allMagasins(filters: MagasinFilter): Seq[Magasin]

  def allProducts(): Seq[Product]

  def searchMagasinByProduct(productSearchMagasinFilter: ProductSearchMagasinFilter): Seq[ProductSearchMagasinResult]

  def productEvolution(productId: String): ProductPriceEvolution
}