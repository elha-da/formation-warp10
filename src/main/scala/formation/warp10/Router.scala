package formation.warp10

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, _}
import ch.megard.akka.http.cors.CorsDirectives.cors
import ch.megard.akka.http.cors.CorsSettings
import formation.warp10.utils.NumberUtils
import kneelnrise.warp10scala.model.Coordinates
import spray.json.{DefaultJsonProtocol, JsNumber, JsValue}

class Router(api: Api) extends SprayJsonSupport with DefaultJsonProtocol {
  import akka.http.scaladsl.unmarshalling.Unmarshaller._

  private val corsSettings = CorsSettings.defaultSettings.copy(
    allowedMethods = CorsSettings.defaultSettings.allowedMethods ++ Seq(HttpMethods.PUT, HttpMethods.DELETE),
    allowGenericHttpRequests = true
  )

  lazy val route: Route = cors(corsSettings) {
    handleRejections(rejectionHandler) {
      apiRoute
    }
  }

  lazy val apiRoute: Route = cors(corsSettings) {
    pathPrefix("magasins") {
      pathEndOrSingleSlash {
        get {
          parameterMap { parameters =>
            ctx =>
              ctx.complete(StatusCodes.OK, api.allMagasins(httpParametersToMagasinFilter(parameters)))
          }
        }
      } ~
        path("nearest") {
          get {
            parameterMap { parameters =>
              ctx =>
                httpParametersToCoordinatesOpt(parameters).map { coordinates =>
                  ctx.complete(StatusCodes.OK, api.nearestMagasin(coordinates))
                } getOrElse {
                  ctx.complete(StatusCodes.BadRequest, "Please provide coordinates (lat, lon)")
                }
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
          pathEndOrSingleSlash {
            get { ctx =>
              api.findProduct(productId) match {
                case Some(product) => ctx.complete(StatusCodes.OK, product)
                case None => ctx.complete(StatusCodes.NotFound)
              }
            }
          } ~
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
          } ~ path("newPrice") {
            post {
              entity(as[JsValue]) { price =>
                ctx =>
                  price match {
                    case JsNumber(value) =>
                      api.updateProductPrice(productId, value)
                      ctx.complete(StatusCodes.OK, "done")
                    case _ =>
                      ctx.complete(StatusCodes.BadRequest, "invalid number")
                  }
              }
            }
          }
        }
    } ~ front
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

  def front: Route =
    pathEndOrSingleSlash { getFromResource("front/index.html") } ~
    path("main.js") { getFromResource("front/main.js") } ~
    path("styles.css") { getFromResource("front/styles.css") }

  private def rejectionHandler = RejectionHandler.newBuilder().handle {
    case MalformedRequestContentRejection(message, cause) =>
      complete(HttpResponse(
        status = StatusCodes.BadRequest,
        entity = message
      ))
    case UnsupportedRequestContentTypeRejection(_) =>
      complete(HttpResponse(
        status = StatusCodes.BadRequest,
        entity = "Please provide a valid JSON value"
      ))
    case MalformedFormFieldRejection(fieldName, errorMsg, _) =>
      complete(HttpResponse(
        status = StatusCodes.BadRequest,
        entity = s"Malformed field: $fieldName (info: $errorMsg)"
      ))
    case MissingFormFieldRejection(fieldName) =>
      complete(HttpResponse(
        status = StatusCodes.BadRequest,
        entity = s"Missing field: $fieldName"
      ))
    case AuthenticationFailedRejection(cause, challenge) =>
      complete(HttpResponse(
        status = StatusCodes.Unauthorized,
        entity = StatusCodes.Unauthorized.defaultMessage
      ))
    case AuthorizationFailedRejection =>
      complete(HttpResponse(
        status = StatusCodes.Forbidden,
        entity = StatusCodes.Forbidden.defaultMessage
      ))
  }.result()
}
