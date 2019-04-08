## Functional Programming in Scala (Week 1)


Recomended reading - 
Structure and Interpretation of Computer Programs (Harold Abelson)

+ Stuff in FP is evaluated using the substitution model which forms the basis of lambda calculus (reduce an expression to a value)

Two ways of evaluating functions

+ Call by Value - function arguments are evaluated first
+ Call by Name - functions are evaluated first

Scala normally uses *CBV* but you can force it to use CBN by using => when indicating the type of a function argument

```scala
def loop : Any = loop

def eval(x: Int, y:  Any) = 1

eval(1, loop) // Infinite recursion

def evalCBV(x: Int, y: =>  Any) = 1

evalCBV(1, loop) // returns 1
```

Similarly anything definied with the keyword *def* uses CBN while anything definied with the keyword *val* uses CBV

```scala
def loop : Any = loop

def x = loop // repl with respond with x: Any

val y = loop // repl will hang since it'll start evaluating the RHS at the point of definition because val is CBV
```

### Tail Recursion

If a function calls itself (or any other function) as it's last action, the function's stack frame 
can be reused, so it will use constant space like it's iterative counterpart, such a function is called *tail recursive*.

One stack frame would be sufficient for both functions, such calls are called *tail calls*.

You can require a function to be tail recursive using the annotation `@tailrec`

```scala
import scala.annotation.tailrec

@tailrec
def gcd(x: Int, y: Int): Int = 
    if (y == 0) x else gcd(y, x % y)

```