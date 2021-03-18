package jacob.client

import jacob.model._

trait Dom[F[_]] {
  def init: F[Unit]
}

object Dom {
  def apply[F[_]: Dom]: Dom[F] = implicitly
}
