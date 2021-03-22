package jacob.server

import cats._
import cats.data._
import cats.effect._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s._

import jacob.shared.api._
import jacob.shared.model._

final case class CharacterRoutes[F[_]](charRepo: CharacterRepo[F]) {

  val getAllChars: HttpRoutes[F] =
    Http4sServerInterpreter.toRoutes(CharacterApi.getAllChars)(charRepo.getAllChars _)

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
    ).reduceLeft(SemigroupK[HttpRoutes[F]].combineK)
}
