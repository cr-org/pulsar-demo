package pulsar.consumer

import cats.Inject
import cats.effect._
import cats.effect.concurrent.Deferred
import cats.implicits._
import cr.pulsar._
import cr.pulsar.schema.circe._
import fs2._
import io.circe.generic.auto._

object Main extends IOApp {

  case class Msg(name: String, year: Int)

  val config = Config.default
  val topic  = Topic(config, Topic.Name("demo"), Topic.Type.NonPersistent)
  val sub    = Subscription(Subscription.Name("my-sub"), Subscription.Type.Shared)

  val resources: Resource[IO, Consumer[IO]] =
    for {
      pulsar <- Pulsar.create[IO](config.serviceUrl)
      consumer <- Consumer.create[IO](pulsar, topic, sub)
    } yield consumer

  val decodeMessage = Inject[Msg, Array[Byte]].prj

  def run(args: List[String]): IO[ExitCode] =
    Deferred[IO, Unit]
      .flatMap { shutdown =>
        Stream
          .resource(resources)
          .evalTap(_ => IO(println("Starting up Pulsar consumer")))
          .flatMap { consumer =>
            consumer.subscribe
              .evalTap(m => IO(println(decodeMessage(m.getData))))
              .evalMap(m => consumer.ack(m.getMessageId) >> shutdown.complete(()))
              .interruptWhen(shutdown.get.attempt)
          }
          .compile
          .drain
      }
      .as(ExitCode.Success)

}
