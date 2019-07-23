## Recap

Even functions in scala have a type of some class or trait. A type like
`JBinding => String` is just a shorthand for `scala.Function1[JBinding, String]` where


Function1: 

```scala
trait Function1[-A, +R] {
  def apply(x: A): R
}
```

So you can subclass functions, so maps are a subclass of `(Key => Value)` which is basically
a `Function1`.

```scala
trait Map[Key, Value] extends (Key => Value)
```

Similarly sequences are defined as

```scala
trait Seq[Elem] extends (Int => Elem)
```

That's why we can write for both 

`elems(i)`

### Partial Functions

Provide the ability to query if a function is defined for a given argument.

```scala
trait PartialFunction[-A, +R] extends (A => R) {
  def apply(x: A): R
  def isDefinedAt(x: A): Boolean
}
```

`isDefinedAt` only guarantees that their will be no MatchError in the outer most match 
clauses, for nested match clauses there still might be a match error.

The left side of a generator in a `For-expression` may also be a pattern.

### SQL like Queries with For

```scala

case class Book(title: String, authors: List[String])

val books: List[Book] = ???


for (b <- books; a <- b.authors if a startsWith "Singh,")
yield b.title

for (b <- books if (b.title indexOf "Program") >= 0)
yield b.title

// Return all authors who have written at least two books

val authorsWithMultipleBooks = 
{ 
  for {
  b1 <- books
  b2 <- books
  if b1.title < b2.title // Arrange them lexicographically
  a1 <- b1.authors
  a2 <- b2.authors
  if a1 == a2
  } yield a1
}.distinct
```

### For-Expressions and Higher-Order Functions

map, flatMap and filter can be written in terms of for:

```scala
def mapFun[T, U](xs: List[T], f: T => U): List[U] = {
  for {
    x <- xs
  } yield f(x)
}

def filterFun[T](xs: List[T], f: T => Boolean): List[T] = {
  for {
    x <- xs
    if f(x)
  } yield x
}

def flatMapFun[T, U](xs: List[T], f: T => Iterable[U]): List[U] = {
  for {
    x <- xs
    y <- f(x)
  } yield  y
}
```

In reality For expressions get compiled to flatMap, map and a lazy variant of filter.

`withFilter` is a lazy variant of filter.


Exercise: Translate the For expression into a higher order function

```scala
case class Book(title: String, authors: List[String])
val books: List[Book] = ???

for (b <- books; a <- b.authors if a startsWith "Singh,")
yield b.title

val title = books.flatMap(book => 
  book.authors.withFilter(author => author.startsWith("Singh,"))
  .map(_ => book.title))
```

So For expressions can be used with custom types as long as they implement map, flatMap and withFilter functions.

This is the basis of Scala DB connection frameworks like ScalaQuery and Slick. 

### Generating Random values with For Expressions

Check worksheet for example Generators

#### Testing random properties 

[ScalaCheck](https://www.scalacheck.org/): Instead of writing tests, write *properties* that are assumed to hold.

```scala
    forAll {
      (l1: List[Int], l2: List[Int]) => l1.size + l2.size == (l1 ++ l2).size
    }
```

### Monads

In simple words

Monads: Data Structures with map and flatMap functions defined over them together with some
algebraic laws that they should have.

A monad is a parametric type `M[T]` with two operations, *flatMap* and *unit* that have to satisfy
some laws

```scala
trait M[T] {
  def flatMap[U](f: T => M[U]): M[U]
}

def unit[T](x: T): M[T]
```

In literature, flatMap is more commonly called *bind*.

#### Example of Monads

List is a monad with `unit(x) = List(x)`
Generator is a monad with `unit(x) = single(x)`

#### Monads and map

map can be defined on a monad in terms of unit and flatMap


```
m map f == m flatMap (x => unit(f(x))) 
        == m flatMap (f andThen unit)
```

### Monad Laws

A type has to satisfy three laws in order to qualify as a Monad:


#### Associativity

```
(m flatMap f) flatMap g == m flatMap (x => f(x) flatMap g)
```

#### Left Unit

```
unit(x) flatMap f == f(x)
```

#### Right unit
```
m flatMap unit == m
```

For Expressions work well with Monads because of their associativity and right unit properties.

#### Another type Try

Implementation of try

```
abstract class Try[+T]

abstract class Try[+T]
case class Success[T](x: T) extends Try[T]
case class Failure(ex: Exception) extends Try[Nothing]

object Try {
  def apply[T](expr: => T): Try[T] = 
    try Success(expr)
    catch {
      case NonFatal(ex) => Failure(ex)
    }
}
```


Try-valued computations can be composed in for expressions as long as map and flatMap are implemented.
