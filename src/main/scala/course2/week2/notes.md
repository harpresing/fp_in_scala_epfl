### Streams - Delayed Evaluation

Finding the 2nd prime number between 1000 and 10000

```scala
def isPrime(n: Int) = ???

val secondPrime = ((1000 to 10000) filter isPrime)(1)
```

The above impl is concise but very bad performance wise since it checks all the
numbers if they are prime till 10000.

We can make this efficient by using a trick:


```
Avoid computing the tail of a sequence until it is needed
for the evaluation result (which might be never)
```

This is implemented in the class `Stream`.


Can be defined from a constant `Stream.empty` and a constructor Stream.cons.

```scala
val sc = Stream.cons(1, Stream.empty)
val sc2 = Stream(1, 2, 3)
val sc3 = (1 to 100).toStream // deprecated since 2.13
// val sc4 = (1 to 100).toLazyList
```

Mostly all functions on streams are also defined on lists.
Cons operator for streams `#::` - Can be used in expressions as well as patterns.

### Lazy Evaluation

Do things as late as possible and never do them twice. 

TODO: Add cons example of call by name eval and lazy eval definition

Scala uses strict evaluation by default, to enable lazy evaluation:

```scala
lazy val x = 1 to 100000
```

Call by Name Evaluation