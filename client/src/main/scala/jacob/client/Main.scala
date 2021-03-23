package jacob.client

import cats.effect._

import sttp.client3.SttpBackend
import sttp.client3.impl.cats.FetchCatsBackend

import jacob.client.impl._

object Main extends IOApp {

  val sttp: SttpBackend[IO, Any] = FetchCatsBackend[IO]()
  val charClient: CharacterClient[IO] = CharacterClientSttp(sttp)
  val dom: Dom[IO] = DomImpl[IO](charClient)

  def run(args: List[String]): IO[ExitCode] = for {
    _ <- dom.init
  } yield ExitCode.Success
}
