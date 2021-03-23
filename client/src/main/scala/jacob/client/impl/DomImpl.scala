package jacob.client.impl

import cats.effect._
import cats.implicits._
import com.github.uosis.laminar.webcomponents.material._
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveElement
import org.scalajs.dom

import jacob.client._

final case class DomImpl[F[_]](charClient: CharacterClient[F])(implicit F: Sync[F]) extends Dom[F] {
  override def init: F[Unit] = for {
    header <- mdHeader
    _ <- renderF(dom.document.body, header)
  } yield ()

  val mdHeader = F.delay(
    TopAppBarFixed(
      _.slots.title(span("Character")),
    )
  )

  val mdButton = F.delay(
    Button(
      _.label := "Create A New Character",
      _.raised := true,
    )
  )

  def renderF(container: dom.Element, node: ReactiveElement.Base): F[Unit] =
    F.delay(
      documentEvents.onDomContentLoaded.foreach { _ =>
        render(container, node)
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
