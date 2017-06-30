package formation.warp10.data

import java.nio.file.{Files, Path}
import java.util.UUID

import formation.warp10.Magasin
import formation.warp10.utils.CollectionUtils
import kneelnrise.warp10scala.model.Coordinates

object MagasinParser {
  def load(directoryPath: Path): Seq[Magasin] = {
    CollectionUtils.scalaSeq(Files.list(directoryPath))
      .filter(_.endsWith(".csv"))
      .flatMap(loadFile)
  }

  def loadFile(filePath: Path): Seq[Magasin] = {
    CollectionUtils.scalaSeq(Files.readAllLines(filePath))
      .map(line => {
        val elems = line.split(';')
        try {
          val (name: String, lat: Double, long: Double) = (elems(0), elems(1).toDouble, elems(2).toDouble)
          Magasin(
            id = UUID.randomUUID().toString,
            name = name,
            coordinates = Coordinates(lat, long)
          )
        } catch {
          case e: Exception =>
            println(e)
            null
        }
      })
  }
}
