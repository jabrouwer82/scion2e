package jacob.server.impl

import java.util.UUID
import cats.implicits._
import cats.effect._

import jacob.model._
import jacob.server._

final case class CharacterRepoInMemory[F[_]](implicit F: Sync[F]) extends CharacterRepo[F] {
  var state: Map[String, Character] = Map.empty

  def getChar(id: String): F[Option[Character]] =
    F.delay(state.get(id))

  def addChar(char: CharacterWithoutId): F[String] = F.delay {
    val id = UUID.randomUUID().toString()
    state = state + (id, char)
    id
  }

  def updateChar(char: Character): F[Unit] = F.delay {
    state = state - (char.id) + (char.id, char)
  } *> F.unit

  def deleteChar(id: String): F[Unit] = F.delay {
    state = state - id
  } *> F.unit
}
