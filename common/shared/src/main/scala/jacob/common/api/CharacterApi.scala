package jacob.common.api

import java.util.UUID

import sttp.tapir._
import sttp.tapir.json.circe._

import jacob.common.model._

object CharacterApi {
  val charBase: Endpoint[Unit, JError, Unit, Any] =
    endpoint
      .in("char")
      .errorOut(jsonBody[JError])

  val getAllChars: Endpoint[Unit, JError, List[Character], Any] =
    charBase
      .get
      .out(jsonBody[List[Character]])

  val getChar: Endpoint[UUID, JError, Option[Character], Any] =
    charBase
      .get
      .in(path[UUID])
      .out(jsonBody[Option[Character]])

  val updateChar: Endpoint[(UUID, CharacterUpdate), JError, Unit, Any] =
    charBase
      .post
      .in("update" / path[UUID])
      .in(jsonBody[CharacterUpdate])

  val deleteChar: Endpoint[UUID, JError, Unit, Any] =
    charBase
      .post
      .in("delete" / path[UUID])

  val createChar: Endpoint[CharacterWithoutId, JError, UUID, Any] =
    charBase
      .post
      .in("create")
      .in(jsonBody[CharacterWithoutId])
      .out(jsonBody[UUID])

  val endpoints = List(
    getAllChars,
    getChar,
    createChar,
    updateChar,
    deleteChar,
  )
}
