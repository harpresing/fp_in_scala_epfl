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

###### Upper Bounds
 
```scala
def assertAllPos[S <: IntSet](r: S): S
```

Above `<: IntSet` is an upper bound of type parameter S.

+ S <: T means: S is a subtype of T
+ S >: T means: S is a supertype of T

###### Lower Bounds

Example

```
[S >: NonEmpty]
```

###### Mixed Bounds

```
[S >: NonEmpty <: IntSet]
```

###### Liskov Substitution Principle

This principle tells us when a type can be a subtype of another

```
If A <: B then everything once can do with a value of type B
one should also be able to do with a value of type A.
```

More formally

```
Let q(x) be a property provable about objects x of type B. Then 
q(y) should be provable for objects y of type A where A <: B.
```
