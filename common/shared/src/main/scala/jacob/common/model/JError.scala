package jacob.common.model

import io.circe._
import sttp.tapir.Schema

final case class JError(message: String)

object JError {
  implicit val codec: Codec[JError] = Codec.forProduct1("message")(JError.apply _)(_.message)
  implicit val schema: Schema[JError] = Schema.derived
}
