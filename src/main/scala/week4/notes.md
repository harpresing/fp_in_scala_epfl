### Functions as Objects

In scala functions are actually objects.

The type `A => B` is actually this:

```scala
package scala

trait Function1[A, B] {
  def apply(x: A): B
}
```

So functions are objects with apply methods.


An annonymous function such as 

```scala
  (x: Int) => x * x
```

This translates to

```scala
{
  class AnonFun extends Function1[Int, Int] {
    def apply(x:  Int): Int = x * x
  }
  new AnonFun
}
```

An example of `eta-expansion`

```scala
def f(x: Int): Boolean

// Is converted to

(x: Int) => f(x)
```

#### Polymorphism

+ Subtyping
+ Generics

##### Type Bounds

###### Upper Bounds
 
```scala
def assertAllPos[S <: IntSet](r: S): S
```

Above `<: IntSet` is an upper bound of type parameter S.

+ S <: T means: S is a subtype of T
+ S >: T means: S is a supertype of T

###### Lower Bounds

Example

```
[S >: NonEmpty]
```

###### Mixed Bounds

```
[S >: NonEmpty <: IntSet]
```

###### Liskov Substitution Principle

This principle tells us when a type can be a subtype of another

```
If A <: B then everything once can do with a value of type B
one should also be able to do with a value of type A.
```

More formally

```
Let q(x) be a property provable about objects x of type B. Then 
q(y) should be provable for objects y of type A where A <: B.
```

#### Pattern Matching

`Decomposition` - A general and convenient way to access objects in an
extensible class hierarchy.

Eg - Using `instanceof` operator in java to find the type of an object and then
casting it to that type.

So basically Pattern Matching tries to figure out which subclass was used in the
construction of an object and what were the arguments of the constructor.

In Scala we achieve Functional Decomposition via Pattern matching using `Case classes`

```scala
trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
```

So when you do this, Scala compiler creates companion objects like so

```scala
object Number extends Expr {
  def apply(n: Int): Number = new Number(n)
}

object Sum extends Expr {
  def apply(e1: Expr, e2: Expr): Sum = new Sum(e1, e2)
}
```

So example of Pattern matching

```scala
def eval(e: Expr): Int = e match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}
```

A `MatchError` exception is thrown if no pattern matches the value of the selector.

Patterns can be constructed from:

+ constructors
+ variables
+ wildcard patters _
+ constants, eg 1, true

```scala
def show(e: Expr): String = e match {
  case Number(n) => n.toString
  case Sum(e1, e2) => show(e1) + " + " + show(e2)
}
```

#### Immutable Collections

##### Lists

A list having x1,....,xn elements is written as List(x1,....,xn)

eg

```scala
val diagonalMatrix3 = List(List(1, 0, 1), List(1, 0, 1), List(1, 0, 1))
val empty: List[Nothing] = List()
```

Properties

+ Lists are immutable
+ Lists are recursive, arrays are flat

##### Construction of Lists

All lists are constructed from 

+ the empty list Nil, and
+ the construction operation `::` (pronounced cons)

```scala
val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
val empty = Nil
```

All operators in scala ending with a `":"` associate to the right. So you can remove
the parenthesis in the above definition

```scala
val fruit = "apples" :: "oranges" :: "pears" :: Nil
```

You can even decompose lists with pattern matching

```
Nil - The Nil Constant
p :: ps - A pattern that matches a list with a head matching p and a tail
matching ps
List(p1, ..., pn)
```

##### Sorting Lists

+ A way to sort a list is to sort the tail first and then insert the head in the right place.

This describes `Insertion sort`

```scala
def isort(xs: List[Int]): List[Int] = xs match {
  case List() => List()
  case y :: ys => insert(y, isort(ys))
}

def insert(x: Int, xs: List[Int]): List[Int] = xs match {
  case List() => List(x)
  case y :: ys => if (x <= y) x :: xs else y :: insert(x, ys)
}
```