package jacob.server

import org.http4s._

trait CharacterRoutes[F[_]] {

  val getAllChars: HttpRoutes[F]

  val getChar: HttpRoutes[F]

  val createChar: HttpRoutes[F]

  val updateChar: HttpRoutes[F]

  val deleteChar: HttpRoutes[F]

  val routes: HttpRoutes[F]
}
