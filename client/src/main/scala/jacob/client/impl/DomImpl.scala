package jacob.client.impl

import scala.math._

import cats.effect._
import cats.implicits._
import com.github.uosis.laminar.webcomponents.material._
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveElement
import org.scalajs.dom

import jacob.client._

final case class DomImpl[F[_]](charClient: CharacterClient[F])(implicit F: Sync[F]) extends Dom[F] {
  override def init: F[Unit] = for {
    header <- mdContent
    _ <- renderOnBody(header)
  } yield ()

  val mdContent = F.delay {
    val charName = Var("Character Name")
    val charLevel = Var(1)
    // val charLevelBus = new EventBus[Int]

    // val charLevel: Signal[Int] = charLevelBus.events.foldLeft(initial = 0)(_ + _)

    div(
      TopAppBarFixed(
        _.slots.title(
          span("Scion 2E CharGen")
        ),
      ),
      div(
        cls := "mdc-top-app-bar--fixed-adjust",
        cls := "half-width",
        div(
          cls := "mdc-card",
          p(
            child.text <-- charName.signal,
            cls := "card-text",
          ),
          p(
            child.text <-- charLevel.signal.map(_.toString),
            cls := "card-text",
          ),
          Textfield(
            _.label := "Character Name",
            _ => onInput.mapToValue --> charName.writer,
          ),
          Button(
            _.label := "+",
            _.raised := true,
            _.disabled <-- charLevel.signal.map(_ == 20),
            _ => onClick.mapTo(min(20, charLevel.now() + 1)) --> charLevel.writer,
          ),
          Button(
            _.label := "-",
            _.raised := true,
            _.disabled <-- charLevel.signal.map(_ == 1),
            _ => onClick.mapTo(max(1, charLevel.now() - 1)) --> charLevel.writer,
          ),
        )
      ),
    )
  }

  def renderOnBody(node: ReactiveElement.Base): F[Unit] =
    F.delay(
      documentEvents.onDomContentLoaded.foreach { _ =>
        render(dom.document.body, node)
        ()
      } (unsafeWindowOwner)
    ) *> F.unit

  // val actionVar = Var("Do the thing")
  // val allowedIcons = List("🎉", "🚀", "🍉")
  // val iconVar = Var(initial = allowedIcons.head)

  // val app = div(
  //   p(
  //     label("Button label: "),
  //     input(
  //       value <-- actionVar.signal,
  //       inContext { thisNode =>
  //         onInput.mapTo(thisNode.ref.value) --> actionVar.writer
  //       }
  //       )
  //     ),
  //   p(
  //     label("Button icon: "),
  //     select(
  //       inContext { thisNode =>
  //         onChange.mapTo(thisNode.ref.value) --> iconVar.writer
  //       },
  //       value <-- iconVar.signal,
  //       allowedIcons.map(icon => option(value(icon), icon))
  //     )
  //   ),
  // p(
  //   Button(
  //     _.id := "myButton",
  //     _.label <-- actionVar.signal,
  //     _.raised := true,
  //     _.styles.mdcThemePrimary := "#6200ed",
  //     _ => onClick --> (_ => dom.window.alert("Click")), // standard event
  //     _.onMouseOver --> (_ => println("MouseOver")), // "custom" event
  //     _.slots.icon(span(child.text <-- iconVar.signal)),
  //     //_ => onMountCallback(ctx => ctx.thisNode.ref.doThing()) // doThing is not implemented, just for reference
  //   )
  // ),
// )

  // render(containerNode, app)
}
