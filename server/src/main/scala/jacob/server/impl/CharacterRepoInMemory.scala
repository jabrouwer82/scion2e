package jacob.server.impl

import java.util.concurrent.atomic.AtomicReference
import java.util.UUID

import scala.util._

import cats.implicits._
import cats.effect._
import mouse._

import jacob.common.model._
import jacob.server._

final class CharacterRepoInMemory[F[_]](implicit F: Sync[F]) extends CharacterRepo[F] {
  private val stateA: AtomicReference[Map[UUID, Character]] = new AtomicReference(Map.empty)

  override def getAllChars(): F[Either[JError, List[Character]]] =
    F.delay(stateA.get().values.toList.asRight[JError])

  override def getChar(id: UUID): F[Either[JError, Option[Character]]] =
    F.delay(stateA.get().get(id).asRight[JError])

  override def createChar(char: CharacterWithoutId): F[Either[JError, UUID]] = F.delay {
    val id = UUID.randomUUID()
    stateA.getAndUpdate(state => state + (id -> char.withId(id)))
    id.asRight[JError]
  }

  override def updateChar(charId: UUID, charUpdate: CharacterUpdate): F[Either[JError, Unit]] = F.delay {
    ignore(stateA.getAndUpdate(state => state + (charId -> state(charId).withUpdate(charUpdate))))
      .asRight[JError]
  }.handleErrorWith(_ => F.pure(JError("Something went wrong").asLeft[Unit]))

  override def deleteChar(id: UUID): F[Either[JError, Unit]] = F.delay {
    stateA.getAndUpdate(state => state - id)
  } *> F.pure(().asRight[JError])
}
