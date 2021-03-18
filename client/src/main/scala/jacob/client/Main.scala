package jacob.client

import cats.effect._

import client.impl._

object Main extends IOApp {

  implicit val ioDom = DOMImpl[IO]

  def run(args: List[String]): IO[ExitCode] =
    ioDom.init *> IO.pure(ExitCode.Success)
}
