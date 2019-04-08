package week1.recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int =
    if (c == 0) 1 // at the edge, the value is one
    else if (c == r) 1 // the far edge, value is one again
    else pascal(c - 1, r - 1) + pascal(c, r - 1) // sum the values of the two above

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {

    @tailrec
    def isBalanced(left: List[Char], right: List[Char]): Boolean = {
      if (left.isEmpty) right.isEmpty
      else if (left.tail.isEmpty) right.nonEmpty && right.tail.isEmpty
      else if (right.tail.isEmpty) findBalancedParenthesis(left.tail, '(').isEmpty
      else isBalanced(findBalancedParenthesis(left.tail, '('),
        findBalancedParenthesis(right.tail, ')'))
    }

    def findBalancedParenthesis(chars: List[Char], parenthesis: Char): List[Char] =
      if (chars.isEmpty) chars
      else if (chars.head.equals(parenthesis)) chars
      else findBalancedParenthesis(chars.tail, parenthesis)


    isBalanced(findBalancedParenthesis(chars, '('),
      findBalancedParenthesis(chars, ')'))

  }

  /**
    * Exercise 3
    *
    * Hopefully, this does not spoiler too much, but here's the text from SICP (1.2.2)
    *
    * The number of ways to change amount a using n kinds of coins equals
    *
    * The number of ways to change amount a using all but the first kind of coin, plus
    *
    * the number of ways to change amount a - d using all n kinds of coins, where d is the denomination of the first kind of coin.
    *
    */
  def countChange(money: Int, coins: List[Int]): Int = {

    if (coins.isEmpty) 0

    def count(money: Int, coins: List[Int], denomination: Int): Int = {
      if (money == 0) 1
      else if (money < 0) 0
      else if (coins.isEmpty) count(money - denomination, coins, denomination)
      else count(money, coins.tail, coins.head) + count(money - denomination, coins, denomination)
    }

    count(money, coins.tail, coins.head)
  }
}
