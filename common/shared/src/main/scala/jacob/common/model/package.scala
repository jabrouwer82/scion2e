package jacob.common

import java.util.UUID

import io.circe._
import io.estatico.newtype.macros._

package object model {
  @newsubtype case class UuidT[T](value: UUID)
  object UuidT {
    implicit def encoder[A]: Encoder[UuidT[A]] = deriving
    implicit def decoder[A]: Decoder[UuidT[A]] = deriving
  }

  @newsubtype case class StringT[T](value: String)
  object StringT {
    implicit def encoder[A]: Encoder[StringT[A]] = deriving
    implicit def decoder[A]: Decoder[StringT[A]] = deriving
  }

  @newsubtype case class IntT[T](value: Int)
  object IntT {
    implicit def encoder[A]: Encoder[IntT[A]] = deriving
    implicit def decoder[A]: Decoder[IntT[A]] = deriving
  }
}
