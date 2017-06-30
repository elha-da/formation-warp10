package formation.warp10

import java.time.LocalDateTime
import scala.util.{Failure, Success}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import kneelnrise.warp10scala.model._
import kneelnrise.warp10scala.services.Warp10Client

class Warp10Api(configuration: Configuration)(implicit warp10configuration: Warp10Configuration, actorSystem: ActorSystem, actorMaterializer: ActorMaterializer) {
  implicit val ec = actorSystem.dispatcher

  val w10client = Warp10Client(configuration.host, configuration.port)

  def push(gts: GTS): Unit = {
    Source.single(gts)
      .via(w10client.push)
      .runWith(Sink.foreach(println))
      .onComplete {
        case Success(x) => println("Done: " + x)
        case Failure(x) => println("Failure: " + x)
      }
  }

  def read(time: Long): Unit = {
    Source.single(FetchQuery(
      selector = Selector("~org.test.plain.*{}"),
      interval = FetchInterval(LocalDateTime.now(), 1),
      dedup = false
    )).via(w10client.fetch)
      .runWith(Sink.foreach(println))
      .onComplete {
        case Success(x) => println("Done: " + x)
        case Failure(x) => println("Failure: " + x)
      }
  }
}
