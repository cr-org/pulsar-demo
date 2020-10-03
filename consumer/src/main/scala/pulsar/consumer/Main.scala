package pulsar.consumer

import cats.effect._
import cats.effect.concurrent.Deferred
import cats.implicits._
import cr.pulsar._
import cr.pulsar.schema.circe._
import fs2._
import io.circe.generic.auto._

object Main extends IOApp {

  case class Msg(name: String, year: Int)

  val config = Config.Builder.default

  val topic =
    Topic.Builder
      .withName(Topic.Name("demo"))
      .withConfig(config)
      .withType(Topic.Type.NonPersistent)
      .build

  val subs =
    Subscription.Builder
      .withName(Subscription.Name("my-sub"))
      .withType(Subscription.Type.Shared)
      .build

  val resources: Resource[IO, Consumer[IO, Msg]] =
    for {
      pulsar <- Pulsar.create[IO](config.url)
      consumer <- Consumer.create[IO, Msg](pulsar, topic, subs)
    } yield consumer

  def run(args: List[String]): IO[ExitCode] =
    Deferred[IO, Unit]
      .flatMap { shutdown =>
        Stream
          .resource(resources)
          .evalTap(_ => IO(println("Starting up Pulsar consumer")))
          .flatMap {
            _.autoSubscribe
              .evalTap(m => IO(println(m)) >> shutdown.complete(()))
              .interruptWhen(shutdown.get.attempt)
          }
          .compile
          .drain
      }
      .as(ExitCode.Success)

}
