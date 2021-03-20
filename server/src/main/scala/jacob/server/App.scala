package jacob.server

import scala.concurrent.ExecutionContext

import cats.effect._
import fs2.concurrent.Topic
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import jacob.model._

object App extends IOApp {

  def run(args: List[String]): IO[ExitCode] = IO(ExitCode.success)
}
