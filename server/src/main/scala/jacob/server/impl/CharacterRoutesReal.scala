package jacob.server.impl

import cats.effect._
import cats.data._
import cats.implicits._
import sttp.tapir.server.http4s._
import org.http4s._

import jacob.common.api._
import jacob.server._

final case class CharacterRoutesReal[F[_]: Concurrent: ContextShift: Http4sServerOptions: Timer](
  charRepo: CharacterRepo[F]
) extends CharacterRoutes[F] {

  val getAllChars: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.getAllChars)(_ => charRepo.getAllChars())

  val getChar: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.getChar)(charRepo.getChar _)

  val createChar: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.createChar)(charRepo.createChar _)

  val updateChar: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.updateChar)((charRepo.updateChar _).tupled)

  val deleteChar: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.deleteChar)(charRepo.deleteChar _)

  val routes: HttpRoutes[F] =
    NonEmptyList.of(
      getAllChars,
      getChar,
      createChar,
      updateChar,
      deleteChar,
    ).reduceLeft(_ <+> _)
}
