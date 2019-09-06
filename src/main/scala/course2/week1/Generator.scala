package course2.week1

trait Generator[+T] {

  self => // an alias of this

  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    // Had we used this.generate it would cause infinite recursion
    // since this would refer to the current anonymous class
    def generate = f(self.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate = f(self.generate).generate
  }
}
