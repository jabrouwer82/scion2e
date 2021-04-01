package jacob.common.model

import cats._
import cats.implicits._

package object char {
  type AllChar = CharacterK[F forSome { type F[X] }]

  type Character = CharacterK[Id]
  val Character = new CharacterFactory[Id].apply _

  type CharacterOpt = CharacterK[Option]
  val CharacterOpt = new CharacterFactory[Option].apply _

  type CharId = UuidT[AllChar]
  val CharId = new UuidTFactory[AllChar]

  type Name = StringT[AllChar]
  val Name = StringT.apply[AllChar] _

  type Level = IntT[AllChar]
  val Level = IntT.apply[AllChar] _
}

