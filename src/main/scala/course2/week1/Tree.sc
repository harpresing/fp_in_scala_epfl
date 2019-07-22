import course2.week1._

trait Tree {
}

case class Inner(left: Tree, right: Tree) extends Tree

case class Leaf(x: Int) extends Tree

def integers = new Generator[Int] {
  override def generate = scala.util.Random.nextInt()
}

def booleans = integers map (_ >= 0)

def leafs: Generator[Leaf] = for (x <- integers) yield Leaf(x)


object treeGen {
  def trees: Generator[Tree] = for {
    isLeaf <- booleans
    tree <- if (isLeaf) leafs else inners
  } yield tree

  def inners: Generator[Inner] = for {
    l <- trees
    r <- trees
  } yield Inner(l, r)
}

treeGen.trees.generate

// Random Testing with generators
def testRandom[T](generator: Generator[T], numTimes: Int = 100)
                 (test: T => Boolean): Unit = {
  for (_ <- 0 until numTimes) {
    val genVal: T = generator.generate
    assert(test(genVal), "test failed for " + genVal)
  }
  println("Passed")
}
