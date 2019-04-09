// Higher Order Function with Currying syntactic sugar
def sum(f: Int => Int)(a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a + 1, acc + f(a))
  }

  loop(a, 0)
}

// Currying without syntactic sugar equivalent to the one above
def sumNoSugar(f: Int => Int): (Int, Int) => Int = {
  def sumF(a: Int, b: Int): Int = {
    def loop(a: Int, acc: Int): Int = {
      if (a > b) acc
      else loop(a + 1, acc + f(a))
    }

    loop(a, 0)
  }

  sumF
}

def cube = (x: Int) => x * x * x


sum((x: Int) => x)(1, 10) // Calling using anonymous function
sum(cube)(1, 10)
def f = sumNoSugar(cube)
f(1, 10)

def product(a: Int, b: Int): Int = {

  def loop(a: Int, acc: Int): Int = {
    if(a > b) acc
    else loop(a + 1, acc * a)
  }

  loop(a, 1)
}

def factorial(n: Int): Int = {
  product(1, n)
}

product(1,5)

// Write a function that generalises both sum and product

def mapReduce(f: Int => Int, combine: (Int, Int) => Int, identity: Int)
             (a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a + 1, combine(acc, a))
  }
  loop(a, identity)
}

mapReduce(x => x, (a, b) => a + b, 0)(1, 10) // sum
mapReduce(x => x, (a, b) => a * b, 1)(1, 10) // product