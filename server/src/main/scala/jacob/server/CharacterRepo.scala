package jacob.server

import cats.effect._

import jacob.model._

abstract class CharacterRepo[F[_]: Sync] {
  def getChar(id: String): F[Option[Character]]
  def addChar(char: CharacterWithoutId): F[String]
  def updateChar(char: Character): F[Unit]
  def deleteChar(id: String): F[Unit]
}
