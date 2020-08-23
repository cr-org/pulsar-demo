package pulsar.consumer

import cats.Inject
import cats.effect._
import cats.implicits._
import cr.pulsar._
import fs2._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.apache.pulsar.client.api.SubscriptionInitialPosition

object Main extends IOApp {

  case class Msg(name: String, year: Int)

  // Pulsar configuration
  val config = Config.default
  val topic  = Topic(config, Topic.Name("my-topic"), Topic.Type.NonPersistent)

  // Consumer details
  val subs    = Subscription(Subscription.Name("my-sub"), Subscription.Type.Shared)
  val initPos = SubscriptionInitialPosition.Latest

  // Needed for consumers and producers to be able to decode and encode messages, respectively
  implicit def circeBytesInject[T: Encoder: Decoder]: Inject[T, Array[Byte]] =
    new Inject[T, Array[Byte]] {
      val inj: T => Array[Byte] = _.asJson.noSpaces.getBytes("UTF-8")
      val prj: Array[Byte] => Option[T] =
        bytes => decode[T](new String(bytes, "UTF-8")).toOption
    }

  val resources: Resource[IO, Consumer[IO]] =
    for {
      client <- PulsarClient.create[IO](config.serviceUrl)
      consumer <- Consumer.create[IO](client, topic, subs, initPos)
    } yield consumer

  val decodeMessage = Inject[Msg, Array[Byte]].prj

  def run(args: List[String]): IO[ExitCode] =
    Stream
      .resource(resources)
      .flatMap { consumer =>
        consumer.subscribe
          .evalTap(m => IO(println(decodeMessage(m.getData))))
          .evalMap(m => consumer.ack(m.getMessageId))
      }
      .compile
      .drain
      .as(ExitCode.Success)

}
