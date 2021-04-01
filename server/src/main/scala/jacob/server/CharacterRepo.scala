package jacob.server

import jacob.common.model._
import jacob.common.model.char._

abstract class CharacterRepo[F[_]] {
  def getAllChars(): F[Either[JError, List[CharacterWithId]]]
  def getChar(id: CharId): F[Either[JError, Option[Character]]]
  def createChar(char: Character): F[Either[JError, CharId]]
  def updateChar(charId: CharId, char: CharacterOpt): F[Either[JError, Unit]]
  def deleteChar(id: CharId): F[Either[JError, Unit]]
}
