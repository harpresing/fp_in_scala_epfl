// Abstract classes in Scala can have methods without a body just like
// in java

abstract class IntSet {
  def contains(x: Int): Boolean

  def include(x: Int): IntSet
}

// So you cannot create an instance of an abstract class with the new operator

// Implementing sets as binary trees so there are two possible trees
// + Tree of an empty set
// + Tree consisting of an integer and two sub trees

class Empty extends IntSet {

  /** Empty tree cannot contain any value, also works as the base condition for
    * recursive calls in contains for NonEmpty
    * */
  override def contains(x: Int) = false

  override def include(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
}

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  override def contains(x: Int) = {
    if (x < elem) left.contains(x)
    else if (x > elem) right contains x // Infix notation to call a method
    else true
  }

  override def include(x: Int) : IntSet = {
    if (x < elem) new NonEmpty(elem, left include x, right)
    else if (x > elem) new NonEmpty(elem, left, right include x)
    else this // the node with the same value is already there
  }
}