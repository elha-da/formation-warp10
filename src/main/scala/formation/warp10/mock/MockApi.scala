package formation.warp10.mock

import java.time.LocalDateTime

import formation.warp10
import formation.warp10._
import kneelnrise.warp10scala.model.Coordinates

class MockApi extends Api {
  override def allMagasins(filters: MagasinFilter): Seq[Magasin] = Seq(
    Magasin("a", "Auchan Nantes St-Nazaire", Coordinates(47.3015, -2.209280000000035)),
    Magasin("b", "Carrefour Nantes Beaujoire", Coordinates(47.2579, -1.512809999999945)),
    Magasin("c", "Casino NANTES BUAT", Coordinates(47.2267, -1.5429300000000694))
  )

  override def allProducts(): Seq[warp10.Product] = Seq(
    Product("x", "Bouteille d'eau"),
    Product("y", "Café"),
    Product("z", "Thé"),
    Product("3124480183811", "Oasis Tropical")
  )

  override def findProduct(barcode: String): Option[Product] = allProducts().find(_.id == barcode)

  override def searchMagasinByProduct(productSearchMagasinFilter: ProductSearchMagasinFilter): Seq[ProductSearchMagasinResult] = Seq(
    ProductSearchMagasinResult(Magasin("a", "Auchan Nantes St-Nazaire", Coordinates(47.3015, -2.209280000000035)), 20),
    ProductSearchMagasinResult(Magasin("b", "Carrefour Nantes Beaujoire", Coordinates(47.2579, -1.512809999999945)), 19.90)
  )

  override def productEvolution(productId: String): ProductPriceEvolution = ProductPriceEvolution(
    id = "x",
    entries = Seq(
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-01T00:00:00"), 20),
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-02T00:00:00"), 19),
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-03T00:00:00"), 20),
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-04T00:00:00"), 20.5),
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-05T00:00:00"), 22.5),
      ProductPriceEvolutionEntry(LocalDateTime.parse("2017-01-06T00:00:00"), 17.99)
    )
  )

  override def nearestMagasin(coordinates: Coordinates): Magasin = {
    Magasin("a", "Auchan Nantes St-Nazaire", Coordinates(47.3015, -2.209280000000035))
  }

  override def updateProductPrice(productId: String, price: BigDecimal): Unit = {

  }
}
