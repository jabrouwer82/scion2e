package jacob.common.api

import sttp.tapir._
import sttp.tapir.json.circe._

import jacob.common.model._

object CharacterApi {
  val charBase: Endpoint[Unit, JError, Unit, Any] =
    endpoint
      .in("char")
      .errorOut(jsonBody[JError])

  val getAllChars: Endpoint[Unit, JError, List[Character], Nothing] =
    charBase
      .get
      .out(jsonBody[List[Character]])

  val getChar: Endpoint[UUID, JError, Option[Character], Nothing] =
    charBase
      .get
      .in(path[UUID]("charId"))
      .out(jsonBody[Option[Character]])

  val updateChar: Endpoint[(UUID, CharacterWithoutId), JError, Unit, Nothing] =
    charBase
      .post
      .in("update" / path[UUID]("charId"))
      .in(jsonBody[CharacterWithoutId])

  val deleteChar: Endpoint[UUID, JError, Unit, Nothing] =
    charBase
      .post
      .in("delete" / path[UUID]("charId"))

  val createChar: Endpoint[CharacterWithoutId, JError, UUID, Nothing] =
    charBase
      .post
      .in("create")
      .in(jsonBody[CharacterWithoutId])
}
