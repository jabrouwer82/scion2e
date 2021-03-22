package jacob.server

import java.util.UUID

import jacob.shared.model._

abstract class CharacterRepo[F[_]] {
  def getAllChars(): F[List[Character]]
  def getChar(id: String): F[Option[Character]]
  def createChar(char: CharacterWithoutId): F[String]
  def updateChar(charId: UUID, char: CharacterWithoutId): F[Unit]
  def deleteChar(id: String): F[Unit]
}
