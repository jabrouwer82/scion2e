package jacob.server

import scala.concurrent.ExecutionContext

import cats.effect._
import fs2.concurrent.Topic
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import jacob.model._

object App extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    Blocker[IO]
      .use { blocker =>
        for {
          topic <- Topic[IO, Option[Character]](None)
          _ <- CharacterStream.stream[IO](blocker).through(topic.publish).compile.drain.start
          _ <- BlazeServerBuilder[IO](ExecutionContext.global)
                 .bindHttp(8292, "0.0.0.0")
                 .withHttpApp(new Service(blocker, topic).routes.orNotFound)
                 .serve
                 .compile
                 .drain
        } yield ()
      }
      .as(ExitCode.Success)
}
