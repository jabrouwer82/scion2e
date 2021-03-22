package jacob.model

import java.util.UUID

final case class Character(
  id: UUID,
  name: String,
  level: Int,
)

final case class CharacterWithoutId(
  name: String,
  level: Int,
)

final case class CharacterUpdate(
  name: Option[String],
  level: Option[Int],
)
