package jacob.server

import scala.concurrent.duration._

import cats.Functor
import cats.effect._
import fs2._

import jacob.model._

object CharacterStream {

  def stream[F[_]: Sync: ContextShift: Timer](blocker: Blocker): Stream[F, Option[Character]] =
    ticker(
      Stream.emits(
        List.range(0, 20).map(i =>
          Some(
            Character(s"a$i", "jacob-$i", i)
          ),
        )
      )
    )

  private def ticker[F[_]: Functor: Timer, A](stream: Stream[F, A]): Stream[F, A] =
    (Stream.emit(Duration.Zero) ++ Stream.awakeEvery[F](5.seconds))
      .as(stream)
      .flatten
}
