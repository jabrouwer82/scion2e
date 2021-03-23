package jacob.server

import java.util.UUID

import jacob.common.model._

abstract class CharacterRepo[F[_]] {
  def getAllChars(): F[Either[JError, List[Character]]]
  def getChar(id: UUID): F[Either[JError, Option[Character]]]
  def createChar(char: CharacterWithoutId): F[Either[JError, UUID]]
  def updateChar(charId: UUID, char: CharacterUpdate): F[Either[JError, Unit]]
  def deleteChar(id: UUID): F[Either[JError, Unit]]
}
