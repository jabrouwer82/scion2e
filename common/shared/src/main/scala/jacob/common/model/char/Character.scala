package jacob.common.model.char

import cats._
import io.circe._
import shapeless.syntax.std.product._
import sttp.tapir.Schema

final case class CharacterK[F[_]](
  name: F[Name],
  level: F[Level],
)
object CharacterK {

  implicit def decoderF[A, F[_]]: Decoder[F[A]] = Decoder[F[A]]
  implicit def encoderF[A, F[_]]: Encoder[F[A]] = Encoder[F[A]]
  implicit def schemaF[A, F[_]]: Schema[F[A]] = implicitly[Schema[F[A]]]

  implicit def codec[F[_]]: Codec[CharacterK[F]] = Codec.forProduct2(
    "name",
    "level",
  )(CharacterK.apply[F] _)(_.productElements.tupled)

  implicit def schema[F[_]]: Schema[CharacterK[F]] = Schema.derived
}

class CharacterFactory[F[_]](implicit F: Applicative[F]) {
  def apply(
    name: Name,
    level: Level,
  ): CharacterK[F] =
    CharacterK[F](F.pure(name), F.pure(level))
}


final case class CharacterWithId(
  uuid: CharId,
  character: Character,
)

object CharacterWithId {
  implicit val codec: Codec[CharacterWithId] = Codec.forProduct2(
    "id",
    "character",
  )(CharacterWithId.apply _)(_.productElements.tupled)

  implicit val schema: Schema[CharacterWithId] = Schema.derived
}

