package jacob.client

import model._

final case class ClientState(state: Option[Character]) {

  def loadChar(char: Character): ClientState =
    ClientState(Some(char))

  def deleteChar: ClientState =
    ClientState(None)

  def get(id: String): Option[Character] =
    MonadError[F, Throwable].catchNonFatal(state.get)
}

object ClientState {
  def empty: ClientState =
    ClientState(None)
}
