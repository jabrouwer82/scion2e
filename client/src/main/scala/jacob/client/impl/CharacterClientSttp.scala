package jacob.client.impl

import sttp.client3._
import sttp.tapir.client.sttp.SttpClientInterpreter

import jacob.client._
import jacob.common.api._
import jacob.common.model._
import jacob.common.model.char._

final case class CharacterClientSttp[F[_]](
  backend: SttpBackend[F, Any],
) extends CharacterClient[F] {
  val getAllCharsRequest = SttpClientInterpreter.toClientThrowDecodeFailures(CharacterApi.getAllChars, None, backend)
  val getCharRequest = SttpClientInterpreter.toClientThrowDecodeFailures(CharacterApi.getChar, None, backend)
  val createCharRequest = SttpClientInterpreter.toClientThrowDecodeFailures(CharacterApi.createChar, None, backend)
  val updateCharRequest = SttpClientInterpreter.toClientThrowDecodeFailures(CharacterApi.updateChar, None, backend)
  val deleteCharRequest = SttpClientInterpreter.toClientThrowDecodeFailures(CharacterApi.deleteChar, None, backend)

  override def getAllChars: F[Either[JError, List[CharacterWithId]]] =
    getAllCharsRequest(())

  override def getChar(charId: CharId): F[Either[JError, Option[Character]]] =
    getCharRequest(charId)

  override def createChar(char: Character): F[Either[JError, CharId]] =
    createCharRequest(char)

  override def updateChar(charId: CharId, charUpdate: CharacterOpt): F[Either[JError, Unit]] =
    updateCharRequest((charId, charUpdate))

  override def deleteChar(charId: CharId): F[Either[JError, Unit]] =
    deleteCharRequest(charId)
}
