val f: String => String = {
  case "ping" => "pong"
}

f("ping")

// f("table") this will give a match error

val fpartial: PartialFunction[String, String] = {
  case "ping" => "pong"
}

fpartial("ping")
fpartial.isDefinedAt("table")

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
  } yield y
}

mapFun((1 until 10).toList, (x: Int) => x * 2)
flatMapFun(List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9)), (x: List[Int]) => x map (y => y * 2))
filterFun((1 until 10).toList, (x: Int) => x % 2 != 0)