package jacob.shared.api

import jacob.shared.model._

object CharacterApi {
  val charBase: Endpoint[Unit, JError, Unit, Any] =
    endpoint
      .in("char")
      .errorOut(jsonBody[JError])

  val getChar: Endpoint[UUID, JError, Character, Nothing] =
    charBase
      .get
      .in(charBase / path[UUID]("charId"))
      .out(jsonBody[Option[Character]])

  val updateChar: Endpoint[UUID, JacobError, Character, Nothing] =
    charBase
      .post
      .in(charBase / path[UUID]("charId"))
      .in(jsonBody[CharacterWithoutId])
      .out(jsonBody[Option[Character]])

}
