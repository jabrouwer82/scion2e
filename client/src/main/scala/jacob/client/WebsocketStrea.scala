package jacob.client

import cats.Show
import cats.effect._
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.circe.generic.auto._
import io.circe.parser._
import org.scalajs.dom._

import jacob.model._

object WebsocketStream {

  def stream[F[_]: ConcurrentEffect]: Stream[F, Option[Character]] =
    Stream
      .eval {
        for {
          queue <- Queue.circularBuffer[F, MessageEvent](1)
          host <- Sync[F].delay(window.location.host)
          ws <- Sync[F].delay(new WebSocket(s"ws://$host/ws"))
          _ <- Sync[F].delay(ws.onmessage = e => Effect[F].runAsync(queue.enqueue1(e))(_ => IO.unit).unsafeRunSync())
        } yield queue
      }
      .flatMap(_.dequeue)
      .map(_.data.show)
      .evalMap(decodeCharacter[F])

  private def decodeCharacter[F[_]: Sync](json: String): F[Option[Character]] =
    Sync[F].fromEither(decode[Option[Character]](json))

  implicit private val anyShow: Show[Any] =
    Show.fromToString
}
