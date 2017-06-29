package formation.warp10

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import formation.warp10.utils.NumberUtils
import kneelnrise.warp10scala.model.Coordinates

class Router(api: Api) extends SprayJsonSupport {
  val route: Route = {
    pathEndOrSingleSlash {
      get {
        complete("Welcome!")
      }
    } ~ pathPrefix("magasins") {
      pathEndOrSingleSlash {
        get {
          parameterMap { parameters =>
            ctx =>
              ctx.complete(StatusCodes.OK, api.allMagasins(httpParametersToMagasinFilter(parameters)))
          }
        }
      }
    } ~ pathPrefix("products") {
      pathEndOrSingleSlash {
        get { ctx =>
          ctx.complete(StatusCodes.OK, api.allProducts())
        }
      } ~
        pathPrefix(Segment) { productId =>
          path("searchMagasin") {
            get {
              parameterMap {
                parameters =>
                  ctx =>
                    ctx.complete(StatusCodes.OK, api.searchMagasinByProduct(httpParametersToProductSearchMagasin(productId, parameters)))
              }
            }
          } ~ path("evolution") {
            get { ctx =>
              ctx.complete(StatusCodes.OK, api.productEvolution(productId))
            }
          }
        }

    }
  }

  def httpParametersToMagasinFilter(parameters: Map[String, String]): MagasinFilter = {
    MagasinFilter(
      coordinates = httpParametersToCoordinatesOpt(parameters),
      radius = parameters.get("radius").flatMap(NumberUtils.toIntOpt)
    )
  }

  def httpParametersToCoordinatesOpt(parameters: Map[String, String]): Option[Coordinates] = {
    (parameters.get("lat"), parameters.get("lon")) match {
      case (Some(lat), Some(lon)) if NumberUtils.isDouble(lat) && NumberUtils.isDouble(lon) =>
        Some(Coordinates(lat.toDouble, lon.toDouble))
      case _ => None
    }
  }

  def httpParametersToProductSearchMagasin(productId: String, parameters: Map[String, String]): ProductSearchMagasinFilter = {
    ProductSearchMagasinFilter(
      id = productId,
      coordinates = httpParametersToCoordinatesOpt(parameters),
      radius = parameters.get("radius").flatMap(NumberUtils.toIntOpt),
      kind = parameters.get("kind").flatMap(ProductSearchMagasinKind.fromString).getOrElse(ProductSearchMagasinKind.cheapest)
    )
  }
}
