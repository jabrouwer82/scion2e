package jacob.client.impl

import cats.effect._
import cats.implicits._
import org.scalajs.dom._

import jacob.client._
import jacob.model._

class DomImpl[F[_]: Sync] extends Dom[F] {
  override def init: F[Unit] = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    appendChild(document.body, document.createElement)
  }

  def elementById(id: String): F[Element] =
    Sync[F].delay(document.getElementById(id))

  def appendChild(element: Element, child: Element): F[Unit] =
    Sync[F].delay(element.appendChild(child)) >> Sync[F].unit

  def removeChild(element: Element, child: Element): F[Unit] =
    Sync[F].delay(element.removeChild(child)) >> Sync[F].unit
}
