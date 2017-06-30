package formation.warp10.data

import java.nio.file.Path

import formation.warp10.Magasin

class MagasinStorage(resourceDirectory: Path) {
  private val magasins = MagasinParser.load(resourceDirectory)

  def findAll(): Seq[Magasin] = magasins

  def find(id: String): Option[Magasin] = magasins.find(_.id == id)
}
