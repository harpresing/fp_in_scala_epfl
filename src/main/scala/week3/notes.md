### Week 3

One class can oly extend one other class but can extend multiple *traits*

```scala
class Square extends Shape with Planar with Movable
```

Traits resemble interfaces in java.

Classes can value *value parameters* however traits cannot!

#### Scala Top level Types

+ Any - The base type of all types and defines operators such as `==`, `equals`, `hashCode`, `toString`
+ AnyRef - Alias of `java.lang.Object`
+ AnyVal - The base type of all primitive types.

#### The nothing type

Nothing is at the bottom of Scala type hierarchy

Uses

+ To signal abnormal termination
+ As an element type of empty collections

#### The Null type

It is the subtype of all AnyRef types. The type of null is `Null`

```scala
val x = null // x: Null
val y: String = null // y: String
val z: Int = null // error: type mismatch, Int is of type AnyVal
```

#### Exceptions

Are handled in a similar manner as that in Java

### Cons-List

A fundamental data structure in many fp languages is an immutable liked list.

Has two building blocks

+ Nil - the empty list
+ Cons - a cell containing an element and the remainder of the list

Note: Type parameters are written in square brackets, e.g. [T].

```scala
trait List[T]
class Cons(val head: T, val tail: List[T]) extends List[T]
class Nil[T] extends List[T]
```