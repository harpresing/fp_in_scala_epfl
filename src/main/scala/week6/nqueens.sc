
object nqueens {

  def queens(n: Int): Set[List[Int]] = {
    def placeQueens(k: Int): Set[List[Int]] =
      if (k == 0) Set(List())
      else
        for {
          queens <- placeQueens(k - 1)
          col <- 0 until n
          if isSafe(col, queens)
        } yield col :: queens

    placeQueens(n)
  }

  def isNotPlacedDiagonally(col: Int, row: Int, r: Int, c: Int): Boolean = math.abs(col - c) != row - r

  def isSafe(col: Int, queens: List[Int]): Boolean = {
    val row = queens.length
    val queensWithIndex = (row - 1 to 0 by -1) zip queens

    queensWithIndex forall {
      case (r, c) => c != col && isNotPlacedDiagonally(col, row, r, c)
    }
  }

  def show(board: List[Int]) = {
    val size = board.length
    val lines: List[String] =
      for (col <- board.reverse) yield Vector.fill(size)("* ").updated(col, "Q ").mkString
    "\n" + (lines mkString "\n")
  }
}

print(nqueens.queens(6).map(l => nqueens.show(l) + "\n"))
