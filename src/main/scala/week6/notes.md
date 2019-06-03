## Other Immutable Sequences 

Lists are linear, access to the first element is much faster than access to the middle
or the end of a list.

The Scala library also defines an alternative sequence implementation which
has more evenly balanced access patterns than a list, it's called a `Vector`.

Vectors are very shallow trees, a vector with one level has an array of
32 elements, with 2 levels it has an array with 32 pointers where each pointer
is pointing to another array of 32 elements.

So for accessing an element, one just has to do depth wise in such a tree,
so the time for accessing an element is log(32 base) (N) where N is the size.

If access patterns have a recursive structure, viz, `list.head, list.tail`, then
lists are better, on the other hand if you're using bulk operations like map, fold
or filter etc then a Vector is more preferable. 

All methods in a list can be applied to a vector except the cons operator.

So instead of the cons operator you have either `:+` or `+:` where : part of
the operator faces a list and + faces an element.

[Info](https://stackoverflow.com/a/322742/5618041) on implementations of Java List interface. 

## Collection Hierarchy

All collections extend the trait `Iterable`. List and Vector extend `Sequence`, while we
also have `Map` and `Set` that extend Iterable.

All functions that are applicable to collections can also be applied to Arrays
and Strings in scala.

### Another simple kind of sequence is Range

A Range represents a sequence of evenly spaced integers, and extends Seq.
IndexSeq sits between a Range and Seq, it's typical implementation being a Vector

Three operators

to (inclusive), until (exclusive), by (determines step value)

## More Sequence operators

xs exists p : Boolean
xs forall p : Boolean
xs zip ys
```scala
List(1, 2, 3) zip "rad" // returns List[(Int, Char)] = List((1,r), (2,a), (3,d))
```
xs.unzip
```scala
(List(1, 2, 3) zip "rad"). unzip 
//returns (List[Int], List[Char]) = (List(1, 2, 3),List(r, a, d))
```
xs flatMap f - the function f maps each element of xs to a collection itself and in the end 
concatenates the result.

so
```
(xs map f).flatten = xs flatMap f
```

xs.sum
xs.product
xs.max
xs.min


Computing Scalar product of two vectors


```scala
def scalarProduct(xs: Vector[Int], ys: Vector[Int]): Int =
    (xs zip ys).map(xy => xy._1 * xy._2).sum
```

The body of that function above could also be replaced by pattern matching

```scala
def scalarProduct(xs: Vector[Int], ys: Vector[Int]): Int =
    (xs zip ys).map{ case(x, y) => x * y }.sum
```

Define isPrime, value conciseness over efficiency

```scala
def isPrime(n: Int): Boolean = 
    2 until n forall (v => n % v != 0)
```

### Solving nested seq problems functionally

Given n, find all pairs of i, j where 1 <= i < j < n, the sum of which is prime


```scala
def isPrime(n: Int): Boolean = 
    2 until n forall (v => n % v != 0)

def primePairs(n: Int): IndexedSeq[(Int, Int)] = 
(1 until 10).flatMap(i => 
  (1 until i).map(j => (i, j)))
  .filter(tuple => isPrime(tuple._1 + tuple._2))
```

So the flatMap above could also be achieved via:

```scala
val xs: List[Int] = List()
(xs foldRight Seq[Int]())(_ ++ _)
```

or

```scala
val xs = List()
xs.flatten
```

### For Expressions

The above expressions can be sometimes difficult to understand, so scala
has for expressions with no side effects

```
for(s) yield y
```

for expressions return a sequence y, and take s which is a sequence of `generators` and
`filters`.


```scala
case class Person(name: String, age: Int)

val persons: List[Person] = List()

for (p <- persons if p.age > 21 ) yield p.name

// the above is equivalent to

persons filter(p => p.age > 21) map(p => p.name)
```

so the previous isPairs can now be represented as


```scala

def isPrime(n: Int): Boolean = 
    2 until n forall (v => n % v != 0)

def primePairs(n: Int): IndexedSeq[(Int, Int)] = 
for{
  i <- 1 until n
  j <- 1 until i
  if isPrime(i + j)
} yield (i, j)
```

Similarly re-writing scalar product with a for expression

```scala

def scalarProduct(xs: Vector[Int], ys: Vector[Int]): Int =
    (for {
      x <- xs
      y <- ys
    } yield x * y).sum
```

## Sets

Sets are another basic abstraction of scala collections

```scala
val p = Set("H", "S")
val s = (1 to 10).toSet
```

Most operations on sequences are available for sets

#### Sets vs Sequences

+ Sets are unordered
+ Sets cannot have duplicate elements
+ Like for Lists the fundamental operations are head and tail, for vectors it's index, for sets
its the function `contains` to determine weather something is present in a set or not.

## Scala Maps

Maps associate keys to values. 
Maps are both iterables and functions because the class Map[Key, Value] also extends the function 
type key => value

```scala
val countryCapital: Map[String, String] = Map("IN" -> "Delhi", "IRL" -> "Dublin")
countryCapital("IN")
```

### The Scala Option type

```scala
trait Option[+A]
case class Some[+A](value: A) extends Option[A]
case class None extends Option[Nothing]
```

So map get key returns

+ None : if the key isn't present
+ Some(x) : if the key is present with value x


Decomposing Option
```scala
def getVal(map: Map[String, String], key: String): String = map get key match {
  case Some(value) => value
  case None => "missing data"
}
```

TODO: Notes on groupBy, orderBy