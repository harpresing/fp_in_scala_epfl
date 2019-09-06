import course2.week1._

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


trait SimpleGenerator[+T] {
  def generate: T
}

val exampleRandomInts = new SimpleGenerator[Int] {
  val rand = new java.util.Random
  override def generate: Int = rand.nextInt()
}


val integers = new Generator[Int] {
  val rand = new java.util.Random
  override def generate: Int = rand.nextInt()
}

// This complies because we have implemented map in Generator
val randomBooleans = for (x <- integers) yield x > 0

// randomBooleans is compiled to

val b = integers map {x => x > 0}

// which is further compiled to beta reduction form https://wiki.haskell.org/Beta_reduction
// and then finally to the form below

val rb = new Generator[Boolean] {
  override def generate: Boolean = integers.generate > 0
}