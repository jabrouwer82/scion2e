package jacob.client.impl

import cats.implicits._
import cats.effect._
import colibri.ext.monix._
import outwatch._
import outwatch.dsl._
import outwatch.reactive.handlers.monix._
import monix.reactive.subjects._
import monix.execution.Scheduler.Implicits.global

import jacob.client._
import jacob.common.model.char._


object CharacterSubject {
  def empty: SyncIO[CharacterK[BehaviorSubject]] =
    SyncIO(CharacterK[BehaviorSubject](BehaviorSubject(Name("")), BehaviorSubject(Level(1))))

  def fromCharacter(char: Character): SyncIO[CharacterK[BehaviorSubject]] =
    SyncIO(CharacterK[BehaviorSubject](BehaviorSubject(Name(char.name)), BehaviorSubject(Level(char.level))))
}

final case class DomImpl[F[_]: Sync](charClient: CharacterClient[F]) extends Dom[F] {
  override def init: F[Unit] =
    OutWatch.renderReplace[F]("#app", div(app))

  def app: SyncIO[VNode] = for {
    charName <- monixHandler(Name("Character name"))
    charLevel <- monixHandler(Level(0))
    safeCharLevel = charLevel.lmap((v: Level) => Level(math.min(20, math.max(1, v))))
  } yield mdContent(charName, safeCharLevel)

  def monixHandler[A](initial: A): SyncIO[BehaviorSubject[A]]  =
    SyncIO(BehaviorSubject(initial))

  def mdContent(
    charName: Handler[Name],
    charLevel: Handler[Level],
  ): VNode= div(
    cls := "mdl-layout",
    cls := "mdl-js-layout",
    cls := "mdl-layout--fixed-header",
    cls := "mdl-layout--no-drawer-button",
    header(
      cls := "mdl-layout__header",
      div(
        cls := "mdl-layout__header-row",
        span(
          cls := "mdl-layout-title",
          "Scion 2E CharGen",
        )
      )
    ),
    div(
      cls := "mdl-layout__content",
      div(
        cls := "page-content",
        p(charName),
        p(charLevel.map(_.toString)),
        mdButton(
          "+",
          onClick(charLevel.map(l => Level(l + 1))) --> charLevel,
        ),
        mdButton(
          "-",
          onClick(charLevel.map(l => Level(l - 1))) --> charLevel,
        ),
      ),
    )
  )

  val mdButton: VNode = button(
    cls := "mdl-button",
    cls := "mdl-js-button",
    cls := "mdl-button--raised",
    cls := "mdl-button--colored",
  )

  // val mdContent = F.delay {
  //   val charName = Var("Character Name")
  //   val charLevel = Var(1)
  //   // val charLevelBus = new EventBus[Int]

  //   // val charLevel: Signal[Int] = charLevelBus.events.foldLeft(initial = 0)(_ + _)

  //   div(
  //     TopAppBarFixed(
  //       _.slots.title(
  //         span("Scion 2E CharGen")
  //       ),
  //     ),
  //     div(
  //       cls := "mdc-top-app-bar--fixed-adjust",
  //       cls := "half-width",
  //       div(
  //         cls := "mdc-card",
  //         p(
  //           child.text <-- charName.signal,
  //           cls := "card-text",
  //         ),
  //         p(
  //           child.text <-- charLevel.signal.map(_.toString),
  //           cls := "card-text",
  //         ),
  //         Textfield(
  //           _.label := "Character Name",
  //           _ => onInput.mapToValue --> charName.writer,
  //         ),
  //         Button(
  //           _.label := "+",
  //           _.raised := true,
  //           _.disabled <-- charLevel.signal.map(_ == 20),
  //           _ => onClick.mapTo(min(20, charLevel.now() + 1)) --> charLevel.writer,
  //         ),
  //         Button(
  //           _.label := "-",
  //           _.raised := true,
  //           _.disabled <-- charLevel.signal.map(_ == 1),
  //           _ => onClick.mapTo(max(1, charLevel.now() - 1)) --> charLevel.writer,
  //         ),
  //       )
  //     ),
  //   )
  // }

  // def renderOnBody(node: ReactiveElement.Base): F[Unit] =
  //   F.delay(
  //     documentEvents.onDomContentLoaded.foreach { _ =>
  //       render(dom.document.body, node)
  //       ()
  //     } (unsafeWindowOwner)
  //   ) *> F.unit

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
