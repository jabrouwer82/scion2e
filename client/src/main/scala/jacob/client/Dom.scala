package jacob.client

trait Dom[F[_]] {
  def init: F[Unit]
}
