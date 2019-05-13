package week3

trait List[T] {
  def isEmpty: Boolean

  def head: T

  def tail: List[T]
}

class Nil[T] extends List[T] {
  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException("Nil.head")

  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty: Boolean = false
}

object List0 {
  def apply[T](): List[T] = new Nil[T]
}

object List1 {
  def apply[T](x: T): List[T] = new Cons[T](x, List0())
}

object List2 {
  def apply[T](x: T, y: T): List[T] = new Cons[T](x, List1(y))
}
