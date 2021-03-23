package jacob.server

import scala.concurrent._

import cats.implicits._
import cats.effect._
import org.http4s.server.blaze._
import org.http4s.implicits._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.http4s.SwaggerHttp4s

import jacob.server.impl._
import jacob.common.api._

object App extends IOApp {

  val charRepo: CharacterRepo[IO] = new CharacterRepoInMemory[IO]
  val charRoutes: CharacterRoutes[IO] = CharacterRoutes[IO](charRepo)

  val openApiDocs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(CharacterApi.endpoints, "Char", "1.0.0")
  val openApiYml: String = openApiDocs.toYaml

  val ec: ExecutionContext = ExecutionContext.global

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](ec)
      .bindHttp(8292, "localhost")
      .withHttpApp(
        List(
          charRoutes.routes,
          new SwaggerHttp4s(openApiYml).routes[IO],
        ).reduceLeft(_ <+> _).orNotFound
      )
      .resource
      .use { _ =>
        IO {
          println("Go to: http://localhost:8080/docs")
          println("Press any key to exit ...")
          scala.io.StdIn.readLine()
          ExitCode.Success
        }
      }
}
