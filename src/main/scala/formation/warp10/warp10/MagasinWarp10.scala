package formation.warp10.warp10

import formation.warp10.{Configuration, Magasin, Warp10Api}
import kneelnrise.warp10scala.model.{GTS, GTSStringValue}

class MagasinWarp10(warp10Api: Warp10Api, configuration: Configuration) {
  def registerAll(magasins: Seq[Magasin]): Unit = {
    warp10Api.pushAll(magasins.map(magasinToGTS))
  }

  def register(magasin: Magasin): Unit = {
    warp10Api.push(magasinToGTS(magasin))
  }

  private def magasinToGTS(magasin: Magasin): GTS = {
    GTS(
      ts = Some(configuration.nowAtOneTime),
      coordinates = Some(magasin.coordinates),
      elev = None,
      name = "org.test.plain.store",
      labels = Map("id" -> magasin.id),
      value = GTSStringValue(magasin.name)
    )
  }
}
