package jacob.model

final case class Character(
  id: String,
  name: String,
  level: Int,
)

final case class CharacterWithoutId(
  name: String,
  level: Int,
)
