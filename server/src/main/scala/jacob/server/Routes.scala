package jacob.server

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._

object Routes {
  val charBase: String = "character"

  def charRoutes[F[_]](charRepo: CharacterRepo[F]): HttpRoutes = HttpRoutes.of[F] {
    case GET -> ROOT / charBase / charId =>
      charRepo.getChar(charId)
        .flatMap(_.fold(NotFound(charId))(c => Ok(char.asJson)))
  }
}
