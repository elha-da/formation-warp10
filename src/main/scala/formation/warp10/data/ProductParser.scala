package formation.warp10.data

import java.nio.file.{Files, Path}

import formation.warp10.Product
import formation.warp10.utils.{CollectionUtils, NumberUtils}

object ProductParser {
  def load(directoryPath: Path): Seq[Product] = {
    CollectionUtils.scalaSeq(Files.list(directoryPath))
      .filter(_.endsWith(".csv"))
      .flatMap(loadFile)
  }

  def loadFile(filePath: Path): Seq[Product] = {
    CollectionUtils.scalaSeq(Files.readAllLines(filePath))
      .map(line => {
        line.split(';').toList match {
          case ean :: name :: referencePrice :: Nil if NumberUtils.isDouble(referencePrice) => Some(Product(ean, name, referencePrice.toDouble))
          case _ => None
        }
      })
      .collect { case Some(product) => product }
  }
}
