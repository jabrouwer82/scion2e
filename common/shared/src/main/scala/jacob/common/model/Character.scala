package jacob.common.model

import java.util.UUID

import io.circe._
import shapeless.syntax.std.product._
import sttp.tapir.Schema

final case class Character(
  id: UUID,
  name: String,
  level: Int,
) {
  def withUpdate(update: CharacterUpdate): Character =
    copy(id, update.name.getOrElse(name), update.level.getOrElse(level))
}

object Character {
  implicit val codec: Codec[Character] = Codec.forProduct3(
    "id",
    "name",
    "level",
  )(Character.apply _)(_.productElements.tupled)
  implicit val schema: Schema[Character] = Schema.derived
}

final case class CharacterWithoutId(
  name: String,
  level: Int,
) {
  def withId(id: UUID): Character =
    Character(
      id,
      name,
      level,
    )
}

object CharacterWithoutId {
  implicit val codec: Codec[CharacterWithoutId] = Codec.forProduct2(
    "name",
    "level",
  )(CharacterWithoutId.apply _)(_.productElements.tupled)
  implicit val schema: Schema[CharacterWithoutId] = Schema.derived
}

final case class CharacterUpdate(
  name: Option[String],
  level: Option[Int],
)

object CharacterUpdate {
  implicit val codec: Codec[CharacterUpdate] = Codec.forProduct2(
    "name",
    "level",
  )(CharacterUpdate.apply _)(_.productElements.tupled)
  implicit val schema: Schema[CharacterUpdate] = Schema.derived
}
