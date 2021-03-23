package jacob.client

import java.util.UUID

import jacob.common.model._

trait CharacterClient[F[_]] {
  def getAllChars: F[Either[JError, List[Character]]]
  def getChar(charId: UUID): F[Either[JError, Option[Character]]]
  def createChar(char: CharacterWithoutId): F[Either[JError, UUID]]
  def updateChar(charId: UUID, charUpdate: CharacterUpdate): F[Either[JError, Unit]]
  def deleteChar(charId: UUID): F[Either[JError, Unit]]
}
