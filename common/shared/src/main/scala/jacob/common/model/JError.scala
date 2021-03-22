package jacob.common.model

import io.circe._

final case class JError(message: String)

object JError {
  implicit val codec: Codec[JError] = Codec.forProduct1("message")(JError.apply _)(_.message)
}
