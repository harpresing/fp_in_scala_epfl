### Functions on Lists

+ xs.length
+ xs.last - last element of a list
+ xs.init - all elements except the last
+ xs take n - returns a list with the first n elements or xs itself if its shorter
+ xs drop n
+ xs(n)

```scala
def init[T](xs: List[T]): List[T] = xs match {
  case List() => throw new Error("init of empty list")
  case List(x) => List()
  case y :: ys => y :: init(ys)
}
```

##### Creating new lists

+ xs ++ ys
+ xs updated (n, x) - returns new list with x at index n

##### Finding elements

+ xs indexOf x - -1 if its not present 
+ xs contains x

Function to remove nth element from a list

```scala
def removeAt[T](n: Int, xs: List[T]): List[T] = (xs take n) ::: (xs drop n+1)
```

Flatten a list structure

```scala
def flatten(xs: List[Any]): List[Any] = xs match {
  case List() => List()
  case (y: List[Any]) :: ys => flatten(y) :: flatten(ys)
  case y :: ys => y :: flatten(ys)
}
```


### Merge Sort

Algorithm

+ Separate the list into two sub-lists
+ sort the two sub-lists
+ Merge the sorted sub lists into a single sorted list

#### Tuples

```scala
val pair = ("£", 90)

// Pattern binding
var (label, value) = pair

// The above definition is the same as writing

label = pair._1
value = pair._2
```


Method to merge two sorted lists to get final sorted list

```scala
def merge(xs: List[Int], ys: List[Int]): List[Int] =
  (xs, ys) match {
    case (List(), ys1) => ys1
    case (xs1, List()) => xs1
    case (x :: xs1, y :: ys1) => if (x < y) x :: merge(xs1, ys)
    else y :: merge(xs, ys1)
  }
```

+ xs splitAt n - Returns two sub lists, the first list contains elements upto n while
the second list contains all the remaining

#### Merge Sort Implementation
```scala
def msort(xs: List[Int]): List[Int] = {
  val n = xs.length / 2 
  if (n == 0){
    xs
  } else {
    val (fst, snd) = xs splitAt n
    merge(msort(fst), msort(snd))
  }
}

def merge(xs: List[Int], ys: List[Int]): List[Int] =
  (xs, ys) match {
    case (List(), ys1) => ys1
    case (xs1, List()) => xs1
    case (x :: xs1, y :: ys1) => if (x < y) x :: merge(xs1, ys)
    else y :: merge(xs, ys1)
  }
```

##### With Implicit Ordering Param

```scala
def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
  val n = xs.length / 2
  if (n == 0){
    xs
  } else {
    def merge(xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match {
        case (List(), ys1) => ys1
        case (xs1, List()) => xs1
        case (x :: xs1, y :: ys1) => if (ord.lt(x, y)) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
      }
    val (fst, snd) = xs splitAt n
    merge(msort(fst), msort(snd))
  }
}

msort(List(4, 0, -2, 3, 5))
```

#### List higher order functions

+ Map
+ Filter 

Square each elements in a list

```scala
// With Pattern Matching
def squareList(xs: List[Int]): List[Int] = xs match {
  case List() => List()
  case y :: ys => (y * y) :: squareList(ys) 
}

// With map fun
def sqLMap(xs: List[Int]) = xs map (x => x * x)
```

#### Reduction on Lists

reduceLeft - takes a list and a binary operator (int the form of an anonymous function) and performs the operation on all elements
accumulative-ly from left to right.


```scala
List(1, 2) reduceLeft ((x, y) => x + y) // gives 3
```

In the previous example instead of defining `((x, y) => x + y)`, you could use the
shorthand notation `(_ + _)` where each _ represents a function parameter going left
to right`

reduceLeft can only be applied to non empty lists.

`foldLeft` - similar to reduceLeft but also takes an accumulator z which it returns if the given list is empty,
it acc as the name suggests is used to accumulate the result of the binary operation so it is added to the final
result.

```scala
def sum(xs: List[Int]): Int = (xs foldLeft 0)(_ + _)
```

Application of foldLeft and reduceLeft produce trees that lean to the left.

So they also have dual functions reduceRight and foldRight.

```scala
abstract class List[T] {
  def foldRight[U](z: U)(op: (T, U) => U): U = this match {
    case Nil => z
    case x :: xs => op(x (xs foldRight z)(op))
  }
}

```

foldLeft and foldRight are generally equivalent for operations that are 
`commutative` and `associative`.


### Referential Transparency

It's an important tool for equational proofs for Functional programs because they don't have side effects
so the term is equivalent to the term it reduces.

```
Prove that:
xs ++ Nil = xs

We know that
(x :: xs1) ++ ys = x :: (xs1 ++ ys)

=> x :: (xs1 ++ Nil) (where ys is nil)
=> x :: xs1
=> xs
```

There's another method `fold\unfold` which can be used for equational proofs of functional programs.

Good luck trying to do this :P

```
Prove the following distribution law for map over concatenation. For any lists xs, ys, function f:
(xs ++ ys) map f = (xs map f) ++ (ys map f)

You will need the clauses of ++ as well as the following clauses for map:

Nil map f = Nil
(x :: xs) map f = f(x) :: (xs map f)
```