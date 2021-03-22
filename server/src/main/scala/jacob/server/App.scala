package jacob.server

import cats.effect._

import jacob.shared.model._

object App extends IOApp {

  def run(args: List[String]): IO[ExitCode] = IO(ExitCode.Success)
}
