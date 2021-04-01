package jacob.common.api

import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.codec.newtype._

import jacob.common.model._
import jacob.common.model.char._

object CharacterApi {
  val charBase: Endpoint[Unit, JError, Unit, Any] =
    endpoint
      .in("char")
      .errorOut(jsonBody[JError])

  val getAllChars: Endpoint[Unit, JError, List[CharacterWithId], Any] =
    charBase
      .get
      .out(jsonBody[List[CharacterWithId]])

  val getChar: Endpoint[CharId, JError, Option[Character], Any] =
    charBase
      .get
      .in(path[CharId])
      .out(jsonBody[Option[Character]])

  val updateChar: Endpoint[(CharId, CharacterOpt), JError, Unit, Any] =
    charBase
      .post
      .in("update" / path[CharId])
      .in(jsonBody[CharacterOpt])

  val deleteChar: Endpoint[CharId, JError, Unit, Any] =
    charBase
      .post
      .in("delete" / path[CharId])

  val createChar: Endpoint[Character, JError, CharId, Any] =
    charBase
      .post
      .in("create")
      .in(jsonBody[Character])
      .out(jsonBody[CharId])

  val endpoints = List(
    getAllChars,
    getChar,
    createChar,
    updateChar,
    deleteChar,
  )
}
