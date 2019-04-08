import scala.annotation.tailrec

def loop: Any = loop

def eval(x: Int, y: Any) = 1

// eval(1, loop) // Infinite recursion

def evalCBV(x: Int, y: => Any) = 1

evalCBV(1, loop) // returns 1


// Calculating sqrt using Newton's method
def sqrt(x: Double) = {

  // Nested Functions

  def abs(d: Double) = if (d > 0) d else -d

  def isGoodEnough(guess: Double) =
    abs(guess * guess - x) / x < 0.001

  def nextGuess(currentGuess: Double) =
    (currentGuess + x / currentGuess) / 2

  def sqrtNewton(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else sqrtNewton(nextGuess(guess))

  sqrtNewton(1)
}

sqrt(0.001)
sqrt(1e-21)
sqrt(1e21)

val x = 0

def f(y: Int) = y + 1

val result = {
  val x = f(3)
  x * x // outer definition of x is shadowed
} + x // result: Int = 16


// Tail recursive factorial

@tailrec
def computeFactorial(ans: Double, x: Double): Double =
  if (x == 0) 1
  else if (x - 1 == 0) ans
  else computeFactorial(ans * (x - 1), x - 1)

def factorial(x: Double) = computeFactorial(x, x)

factorial(25)