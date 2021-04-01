package jacob.client

import jacob.common.model._
import jacob.common.model.char._

trait CharacterClient[F[_]] {
  def getAllChars: F[Either[JError, List[CharacterWithId]]]
  def getChar(charId: CharId): F[Either[JError, Option[Character]]]
  def createChar(char: Character): F[Either[JError, CharId]]
  def updateChar(charId: CharId, charUpdate: CharacterOpt): F[Either[JError, Unit]]
  def deleteChar(charId: CharId): F[Either[JError, Unit]]
}
