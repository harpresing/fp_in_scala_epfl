### Apache Spark

#### Data Parallelism in a Distributed Setting

Concepts about shared memory data-parallel collections can be applied to their distributed counterparts
apart from `Non-Associative reduction operations`.

Two extra things to consider which is not an issue when dealing with data parallelism in memory:

+ Node failure
+ Network Latency

[Latency Numbers](https://gist.github.com/jboner/2841832)  every programmer should know.

In general speed of operations follows: `memory > disk > network`.

[Humanized](https://gist.github.com/hellerbarde/2843375#file-latency_humanized-markdown) latency numbers.
So in summary, network and disk operations can be very expensive in comparison to memory.


##### Spark's Predecessor Hadoop

Hadoop is an open source large-scale batch processing framework. It got really popular because

+ Allowed programming with map and reduce API which was easy to reason about
+ Was fault tolerant which is very important in a distributed computation setting because nodes can
and will fail.
+ So it allowed computations on very large data sets succeed to completion because of fault tolerance.

##### Why Spark?

+ Hadoop shuffles and writes intermediate data to `disk` in order to recover from potential failures.
+ And as we know, these network and disk operations can be quite expensive.

```
 Reading\Writing to disk is upto 100 times slower than in-memory.
 Network Communication is upto 1,000,000 times slower than in-memory.
```

Spark on the other hand

+ Retains fault tolerance
+ Different strategy for handling latency, using ideas from functional programming.

Idea: Keep all data `Immutable` and `in-memory`. All operations on data are like functional
transformations on Scala collections. Fault tolerance is achieved by replaying functional transformations
over original datasets.

Result: Spark is `100`x faster than Hadoop with a more expressive API.

Spark tries to aggressively minimize any network traffic.

#### RDDs (Resilient Distributed Datasets)

RDDs are similar to immutable sequential or parallel Scala collections

They have similar methods like
+ map
+ flatmap
+ filter
+ reduce
+ fold
+ aggregate

Example of counting words in a text file

```scala
val text: RDD[String] = spark.textFile("hdfs://..")

val count = text.flatmap(lines => lines.split)
                .map(word => (word, 1))
                .reduceByKey(_+_)
```

##### Creation of an RDD

Two ways to create an RDD

+ Transforming an existing RDD.
+ From a SparkContext (or SparkSession) Object.

`SparkContext`: It is your handle to the Spark Cluster, representing the connection between the spark
cluster and your running application.

It defines methods to generate RDDs

+ `parallelize`: Converts a local Scala Collection to an RDD.
+ `textfile`: reads a text file from a file system and returns an RDD of String

#### RDD Transformations and Actions

In Scala, Transformers return a new collection as a result.
Eg: map, filter, flatmap, groupBy

```scala
def map(f: T => U): Traversable[U]
```

While Accessors return a single value as a result
Eg: reduce, fold, aggregate

```scala
def reduce(f: (T, T) => T): T
```

For RDDs, `Transformations` are analogous to Scala `Transformers`, they return a 
new RDD instead of a new Collection, the only difference being that transformations
are `Lazy` (result is not immediately computed).

While `Actions` are analogous to Scala `Accessors`, they return a value and are `eager`.

```
Lazyness and Eagerness is how Spark reduces network communication using the programming model.
```


#### Common Transformation used with RDDs

+ map
+ flatMap
+ filter
+ distinct - removes duplicates from an RDD

All of the above a Lazy Transformations that return a new RDD


#### Common Actions used with RDDs

+ collect(): Array[T] - returns all elements from RDD
+ count(): Long - return number of elements in the RDD
+ take(num: Int): Array[T] - return first n elements of the RDD 
+ reduce(op: (A, A) => A)
+ foreach(f: T => Unit): Unit - Apply f to all elements in the RDD


```scala
val logs: RDD[String] = ???
val firstLogsWithErrors = logs.filter(_.contains("ERROR")).take(10)
```

In the above example spark defers filtering until an action is called, in the case 
above the action being take(10), since only 10 elements are required spark doesn't filter
the entire RDD, instead it just filters it upto the first 10 so it saves processing and
network usage.

#### Transformations on two RDDs (Lazy)

These are mostly set like operations

+ union(other: RDD[T]): RDD[T]
+ intersection(other: RDD[T]): RDD[T]
+ subtract(other: RDD[T]): RDD[T] - remove the contents of the other
+ cartesian[U](other: RDD[U]): RDD[(U, T)] - Cartesian product with the other RDD


#### Other useful Actions (Eager)

+ takeSample(withRepl: Boolean, num: Int) - take a random sample of num elements
+ takeOrdered(num: Int)(implicit ord: Ordering[T]): Array[T]
+ saveAsTextFile(path: String): Unit
+ saveAsSequenceFile(path: String): Unit


#### Caching and Persistence 

By default, RDDs are recomputed every time you run an Action on them.
This can be expensive if you have to use the dataset more than once.

```
Spark allows you to control what is cached in memory.
```

To prevent spark from re-computing an RDD, you can call `cache()` or `persist()` on it.

An example would be 

```scala
val logs: RDD[String] = ???
val logsWithErrors = logs.filter(_.contains("ERROR")).persist()
val firstLogsWithErrors = logsWithErrors.take(10)
val totalLogsWithErrors = logsWithErrors.count()
```

If you dont use `persist()` on logsWithErrors above, it would be computed twice,
once when the action `take(10)` is called on it and again when the action `count()`
is called on it.

So this helps in iterative algorithms like Logistic regression (classifying ML Algorithm)
that reuses an RDD (for example the input dataset) many times to prevent it from being computed in every iteration.

Many ways to configure how your data is persisted

+ Java Objects (in memory)
+ Java Objects (on disk)
+ Serialized Java Objects in memory (more compact)
+ Serialized Java Objects on disk (more compact)
+ Both in memory and on disk

`cache()` - Uses the default, which is storing as Java Objects in memory

`persist` - Persistence can be customized with this method, pass the storage level you'd like, without
any arguments it's the same as cache()

```
A common performance bottleneck in Spark faced by noobs is re-evaluation of RDDs which can be
easily avoided by using cache or persist.
```

Spark makes important optimizations to the `chain of operations` before execution.

For instance

```scala
val s = p.map(_.lowercase).filter(_.contains("error")).count()
```
Spark will do map and filter together so that it doesn't have to go through the all
the elements in the RDD twice.

#### Spark Cluster Topology

+ Master (Driver Program) - Contains the Spark Context, so this is the node that
we interact with when writing Spark programs
+ Worker Node (Executor) - These are the nodes that execute a Spark Job

The master node communicates with the worker nodes via the `Cluster Manager`. It allocates
resources, manages Scheduling eg YARN/Mesos.

`Spark Application` - Set of processes running on a cluster which are co-ordinated by the
driver program.

The driver is

+ The process with the `main()` method.
+ the process that creates SparkContext RDDs, and stages up and sends off transformations
and actions.


```scala
case class Person(name: String, age: Int)

val people: RDD[Person] = ???

people.foreach(println)
``` 

In the example above, nothing happens on the driver node since foreach is an Action,
the RDD is printed in the stdout of the worker nodes and won't be visible in the sout
of the driver node.

```scala
case class Person(name: String, age: Int)

val people: RDD[Person] = ???

val res = people.take(10)
``` 
In this next example res: Array[People] will be in the master node because the worker node
will return the result of computing the action back to the master.

```
It's your responsibility to know where your code is executing
```