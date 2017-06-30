package formation.warp10.data

import java.nio.file.Path

import formation.warp10.Product

class ProductStorage(resourceDirectory: Path) {
  private val products = ProductParser.load(resourceDirectory)

  def findAll(): Seq[Product] = products

  def find(id: String): Option[Product] = products.find(_.id == id)
}
