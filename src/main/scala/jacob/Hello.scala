package jacob

import org.scalajs.dom
import org.scalajs.dom.document

import mouse._

object Hello {
  def main(args: Array[String]): Unit = {
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      setupUI()
    })
  }

  def setupUI(): Unit = {
    val button = document.createElement("button")
    button.textContent = "Click me!"
    button.addEventListener("click", { (_: dom.MouseEvent) =>
      addClickedMessage()
    })
    ignore(document.body.appendChild(button))

    appendPar(document.body, "Hello World")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    ignore(targetNode.appendChild(parNode))
  }

  def addClickedMessage(): Unit = {
    appendPar(document.body, "You clicked the button!")
  }
}