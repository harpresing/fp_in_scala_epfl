def flatten(xs: List[Any]): List[Any] = xs match {
  case List() => List()
  case (y: List[Any]) :: ys => flatten(y) ::: flatten(ys)
  case y :: ys => y :: flatten(ys)
}


// Scala math.Ordering can be used for comparisons
// It's similar to Java Comparator in some sense

def msort[T](xs: List[T])(ord: Ordering[T]): List[T] = {
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
    merge(msort(fst)(ord), msort(snd)(ord))
  }
}

print(flatten(List(List(1,2), List('a', 'b'), 'c')))

print(msort(List(4, 0, -2, 3, 5))(Ordering.Int))

// List methods

val nums = List(2, -4, 5, 7, 1)

nums filter (x => x < 0)
nums filterNot (x => x < 0)
nums partition  (x => x < 0)

nums takeWhile(x => x > 0) // Stops after it encounters an element that doesnt match the predicate
nums dropWhile(x => x > 0) // Opposite
val tup = nums span(x => x > 0) // Combines the two

// Write a function that packs consecutive duplicates of list elements into sub lists

def pack[T](xs: List[T]): List[List[T]] = xs match {
  case Nil => Nil
  case y :: ys => {
    val (matching, remaining) = ys span(x => x == y)
    (y :: matching) :: pack(remaining)
  }
}

print(pack(List('a', 'a', 'b', 'b', 'c', 'a')))
pack(List("a", "a", "a", "b", "c", "c", "a"))

// Instead of packing, count successive number of same elements

def encode(xs: List[String]): List[(String, Int)] = xs match {
  case Nil => Nil
  case y :: ys => {
    val (matching, remaining) = ys span(x => x == y)
    (y, matching.length + 1) :: encode(remaining)
  }
}

encode(List("a", "a", "a", "b", "c", "c", "a"))
