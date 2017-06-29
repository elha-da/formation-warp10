package formation.warp10

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import kneelnrise.warp10scala.model.Coordinates
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, deserializationError}

case class MagasinFilter(
  coordinates: Option[Coordinates],
  radius: Option[Int]
)

case class Magasin(
  id: String,
  name: String,
  coordinates: Coordinates
)

object Magasin extends DefaultJsonProtocol {
  implicit val impCoordinatesFormat = jsonFormat2(Coordinates.apply)
  implicit val impFormat = jsonFormat3(Magasin.apply)
}

case class Product(
  id: String,
  name: String
)

object Product extends DefaultJsonProtocol {
  implicit val impFormat = jsonFormat2(Product.apply)
}

case class ProductSearchMagasinFilter(
  id: String,
  coordinates: Option[Coordinates],
  radius: Option[Int],
  kind: ProductSearchMagasinKind.Value
)

object ProductSearchMagasinKind extends Enumeration {
  val cheapest, closest = Value

  def fromString(value: String): Option[ProductSearchMagasinKind.Value] = {
    values.find(_.toString.equalsIgnoreCase(value))
  }
}

case class ProductSearchMagasinResult(
  magasin: Magasin,
  price: BigDecimal
)

object ProductSearchMagasinResult extends DefaultJsonProtocol {
  implicit val impFormat = jsonFormat2(ProductSearchMagasinResult.apply)
}

case class ProductPriceEvolution(
  id: String,
  entries: Seq[ProductPriceEvolutionEntry]
)

object ProductPriceEvolution extends DefaultJsonProtocol {
  import ProductPriceEvolutionEntry._
  implicit val impFormat = jsonFormat2(ProductPriceEvolution.apply)
}

case class ProductPriceEvolutionEntry(
  date: LocalDateTime,
  price: BigDecimal
)

object ProductPriceEvolutionEntry extends DefaultJsonProtocol {
  implicit val impLocalDateTimeFormat = new RootJsonFormat[LocalDateTime] {
    override def write(obj: LocalDateTime): JsValue = JsString(obj.format(DateTimeFormatter.ISO_DATE_TIME))
    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(date) => LocalDateTime.parse(date)
      case x => deserializationError("Expected date as JsString, but got " + x)
    }
  }
  implicit val impFormat = jsonFormat2(ProductPriceEvolutionEntry.apply)
  implicit val impProductPriceEvolutionEntrySeq: RootJsonFormat[Seq[ProductPriceEvolutionEntry]] = seqFormat[ProductPriceEvolutionEntry](impFormat)
}