package jacob.common.model

import java.util.UUID

class UuidTFactory[A] {
  def apply(id: UUID): UuidT[A] = UuidT[A](id)
  def randomId: UuidT[A] = UuidT[A](UUID.randomUUID())
}
