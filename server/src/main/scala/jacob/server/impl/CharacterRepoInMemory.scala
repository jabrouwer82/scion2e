package jacob.server.impl

import java.util.concurrent.atomic.AtomicReference
import java.util.UUID

import cats.implicits._
import cats.effect._

import jacob.model._
import jacob.server._

final class CharacterRepoInMemory[F[_]](implicit F: Sync[F]) extends CharacterRepo[F] {
  val stateA: AtomicReference[Map[UUID, Character]] = new AtomicReference(Map.empty)

  override def getAllChars(): F[List[Character]] =
    F.delay(stateA.get().values.toList)

  override def getChar(id: UUID): F[Option[Character]] =
    F.delay(stateA.get().get(id))

  override def createChar(char: CharacterWithoutId): F[UUID] = F.delay {
    val id = UUID.randomUUID()
    stateA.getAndUpdate(state => state + (id -> char))
    id
  }

  override def updateChar(charId: UUID, char: CharacterWithoutId): F[Unit] = F.delay {
    stateA.getAndUpdate(state => state - (char.id) + (char.id -> char))
  } *> F.unit

  override def deleteChar(id: UUID): F[Unit] = F.delay {
    stateA.getAndUpdate(state => state - id)
  } *> F.unit
}
