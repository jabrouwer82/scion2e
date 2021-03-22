package jacob.common.model

import java.util.UUID

import shapeless._
import shapeless.syntax.std.product._

final case class Character(
  id: UUID,
  name: String,
  level: Int,
)

object Character {
  implicit val codec: Codec[Character] = Codec.forProduct3(
    "id",
    "name",
    "level",
  )((Character.apply _).tupled)(_.productElements.tupled)
}

final case class CharacterWithoutId(
  name: String,
  level: Int,
)

final case class CharacterUpdate(
  name: Option[String],
  level: Option[Int],
)
