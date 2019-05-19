def timesExcluding(chars: List[Char], ex: List[Char]): List[(Char, Int)] = {

  def getFrequency(head: Char, tail: List[Char], count: Int): Int = tail match {
    case List() => 1
    case x :: xs =>
      if (xs.isEmpty) if(x == head) count + 1 else count
      else if (x == head) getFrequency(head, xs, count + 1)
      else getFrequency(head, xs, count)
  }

  chars match {
    case List() => List()
    case x :: xs => {
      if (ex.contains(x)) timesExcluding(xs, ex)
      else (x, getFrequency(x, xs,1)) :: timesExcluding(xs, x :: ex)
    }
  }
}

def times(chars: List[Char]): List[(Char, Int)] = {
  if(chars.nonEmpty) timesExcluding(chars, List())
  else List()
}


def timesIter(chars: List[Char]): List[(Char, Int)] = {
  var res: List[(Char, Int)] = List()
  var exclude: List[Char] = List()

  chars.foreach(head => {
    var count = 0
    if (!exclude.contains(head)) {
      chars.foreach(ch => {
        if (head == ch) {
          count = count + 1
        }
      })
      val tuple = (head, count)
      res = tuple :: res
      exclude = head :: exclude
    }
  })

  res
}

print(times(List('a', 'a', 'b')))
print(timesIter(List()))
print(timesIter(List('a', 'a', 'b', 'c', 'b', 'b')))
print(times(List('a', 'a', 'b', 'c', 'b', 'b')))
print(times(List('a', 'a', 'b', 'c', 'b', 'b', 'f', 'd', 'c')))
print(times(List()))