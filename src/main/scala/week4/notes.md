### Functions as Objects

In scala functions are actually objects.

The type `A => B` is actually this:

```scala
package scala

trait Function1[A, B] {
  def apply(x: A): B
}
```

So functions are objects with apply methods.


An annonymous function such as 

```scala
  (x: Int) => x * x
```

This translates to

```scala
{
  class AnonFun extends Function1[Int, Int] {
    def apply(x:  Int): Int = x * x
  }
  new AnonFun
}
```

An example of `eta-expansion`

```scala
def f(x: Int): Boolean

// Is converted to

(x: Int) => f(x)
```

#### Polymorphism

+ Subtyping
+ Generics

##### Type Bounds

