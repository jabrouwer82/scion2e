package jacob.client

import cats.MonadError

import model._

final case class ClientState[F[_]: MonadError[*[_], Throwable]](state: Map[String, ChartState[F]]) {

  def +(pair: (String, ChartState[F])): ClientState[F] =
    ClientState(state + pair)

  def -(key: String): ClientState[F] =
    ClientState(state - key)

  def get(id: String): F[ChartState[F]] =
    MonadError[F, Throwable].catchNonFatal(state(id))

  def partition(data: Option[Character]): ClientState.Partition = {
    val keySet = state.keySet
    val containerIds = data.map(_.id)

    val removed = keySet.diff(containerIds)
    val added = containerIds.diff(keySet)
    val updated = keySet.intersect(containerIds)

    ClientState.Partition(removed, added, updated)
  }
}

object ClientState {
  case class Partition(removed: Set[String], added: Set[String], updated: Set[String])

  def empty[F[_]: MonadError[*[_], Throwable]]: ClientState[F] =
    ClientState(Map.empty)
}
