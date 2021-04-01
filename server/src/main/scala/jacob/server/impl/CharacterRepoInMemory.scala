package jacob.server.impl

import java.util.concurrent.atomic.AtomicReference

import scala.util._

import cats.implicits._
import cats.effect._
import mouse._
import shapeless._

import jacob.common.model._
import jacob.common.model.char._
import jacob.server._

final class CharacterRepoInMemory[F[_]](implicit F: Sync[F]) extends CharacterRepo[F] {

  val CharGen = Generic[Character]
  val CharOptGen = Generic[CharacterOpt]

  private val stateA: AtomicReference[Map[CharId, Character]] = new AtomicReference(Map.empty)

  override def getAllChars(): F[Either[JError, List[CharacterWithId]]] =
    F.delay(
      stateA
        .get()
        .toList
        .map((CharacterWithId.apply _).tupled)
        .asRight[JError]
    )

  override def getChar(id: CharId): F[Either[JError, Option[Character]]] =
    F.delay(stateA.get().get(id).asRight[JError])

  override def createChar(char: Character): F[Either[JError, CharId]] = F.delay {
    val id = CharId.randomId
    stateA.getAndUpdate(state => state + (id -> char))
    id.asRight[JError]
  }

  override def updateChar(charId: CharId, charUpdate: CharacterOpt): F[Either[JError, Unit]] = F.delay {
    ignore(
      stateA.getAndUpdate { state =>
        val updated = CharGen.from(
          CharGen.to(state(charId))
            .zip(CharOptGen.to(charUpdate))
            .map(UpdateCharPoly)
        )
        state + (charId -> updated)
      }
    )
    ().asRight[JError]
  }.handleErrorWith(_ => F.pure(JError("Ah beans").asLeft[Unit]))

  override def deleteChar(id: CharId): F[Either[JError, Unit]] = F.delay {
    stateA.getAndUpdate(state => state - id)
  } *> F.pure(().asRight[JError])
}

object UpdateCharPoly extends Poly1 {
  implicit def idOptCase[A]: Case.Aux[(Id[A], Option[A]), Id[A]] =
    at { case (ida, opta) => opta.getOrElse(ida) }
}
