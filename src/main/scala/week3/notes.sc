// Abstract classes in Scala can have methods without a body just like
// in java

abstract class IntSet {
  def contains(x: Int): Boolean

  def include(x: Int): IntSet

  def union(other: IntSet): IntSet
}

// So you cannot create an instance of an abstract class with the new operator

// Implementing sets as binary trees so there are two possible trees
// + Tree of an empty set
// + Tree consisting of an integer and two sub trees

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  override def contains(x: Int) = {
    if (x < elem) left.contains(x)
    else if (x > elem) right contains x // Infix notation to call a method
    else true
  }

  override def include(x: Int): IntSet = {
    if (x < elem) new NonEmpty(elem, left include x, right)
    else if (x > elem) new NonEmpty(elem, left, right include x)
    else this // the node with the same value is already there
  }

  override def union(other: IntSet): IntSet = {
    ((left union right) union other) include elem
  }

  override def toString = "{" + left + elem + right + "}"
}

class Empty extends IntSet {

  /** Empty tree cannot contain any value, also works as the base condition for
    * recursive calls in contains for NonEmpty
    * */
  override def contains(x: Int) = false

  override def include(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)

  override def union(other: IntSet): IntSet = other

  override def toString = "."
}

// One could argue that empty need not be created more than once, so
// for this purpose, we can simply have a singleton-empty object

object EmptySingleton extends IntSet {
  override def contains(x: Int): Boolean = false

  override def include(x: Int): IntSet = new NonEmpty(x, EmptySingleton, EmptySingleton)

  override def union(other: IntSet): IntSet = other
}

// the data structure like the one above are called persistent data structures since even if we
// change these data structures, the old values still remain


val t1 = new NonEmpty(10, new Empty, new Empty)
val t2 = new NonEmpty(23, new Empty, new Empty)
val t3 = t1 include 12
val t4 = t2 union t3
val t5 = t4 union new NonEmpty(1, new Empty, new Empty)