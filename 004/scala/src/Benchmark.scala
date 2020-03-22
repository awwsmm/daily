import scala.util.control.Breaks._
import scala.util.Random
import org.scalameter.api._
import org.scalameter.picklers.Implicits._

// see Scaladoc at
//   https://www.javadoc.io/doc/com.storm-enroute/scalameter_2.13/latest/org/scalameter/index.html

// at the moment, multiple benchmarks cannot be run with Bench.OfflineReport
//   https://github.com/scalameter/scalameter/issues/159
// ...so it's best to just put everything into a single benchmark object

object DCP004 {

  private val rand = new Random

  // random array generator
  def randArray (n: Int, fracBad: Double, maxScaling: Double): Array[Int] = {
    if (n == 0) Array()
    else if (fracBad == 1.0) Array.fill(n)(0)
    else {
      val maxVal = n * maxScaling
      val minVal = 0.5 - fracBad / (1 - fracBad) * maxVal

      if (minVal == maxVal) Array.fill(n)(minVal.toInt)
      else (1 to n).map(_ =>
        rand.between(minVal, maxVal).round.toInt).toArray
    }
  }

  // mutable array solution
  def findMissingMutable (array: Array[Int]): Int = {
    if (array.length < 1) 1
    else {
      val length = array.length
      var smallestMissing = 1

      for (index <- 1 to length) {
        var value = array(index - 1)

        // if this value is valid, and not at the appropriate index...
        if ((value != index) && (value > 0) && (value <= length)) {
          breakable {

            // ...we will try to swap it
            while (value != index) {

              // get the value at this value's index
              val swapIndex = value
              val swapValue = array(swapIndex - 1)

              // if value of target index is equal to this value, skip
              if (value == swapValue) break

              // swap the values at the indices
              array(swapIndex - 1) = value
              array(index - 1) = swapValue

              // re-apply conditions in outer if loop
              if (swapValue < 1 || swapValue > length) break
              if (swapValue == swapIndex) break

              // get newly-swapped value
              value = array(index - 1)
            }
          }
        }
      }

      for (ii <- 0 until length) {
        if (array(ii) >= 1) {
          if (array(ii) != smallestMissing) return smallestMissing
          else smallestMissing += 1
        }
      }

      smallestMissing
    }
  }

  // in-place functional solution
  def findMissingInPlace (arr: Array[Int]): Int = {
    val goodVals = arr.filter(_ > 0).distinct.sortInPlace
    val missing = goodVals.zipWithIndex.dropWhile({ case (x, i) => x == i + 1 }).headOption
    missing match {
      case Some((_, i)) => i + 1
      case None => goodVals.length + 1
    }
  }

  // Set solution
  def findMissingSet (arr: Array[Int]): Int = {
    val goodVals = arr.filter(_ > 0).toSet
    val nVals = goodVals.size
    val missing = (1 to nVals).dropWhile(goodVals.contains).headOption
    missing match {
      case Some(x) => x
      case None => nVals + 1
    }
  }

}

object Benchmark extends Bench.OfflineReport {
  import DCP004._

  /* Gen.single example
      -> generates a single, specified value
      -> must import org.scalameter.picklers.Implicits._
   */

  // every `using { ... }` block invokes a new JVM, so no need to `clone` arrays

  performance of "Example #1: randArray(5, 0, 1)" in {

    val arrayLength = Gen.single("n")(5)

    val arrays = for (n <- arrayLength) yield randArray(n, 0, 1)

    using(arrays) curve "findMissingMutable" in findMissingMutable
    using(arrays) curve "findMissingInPlace" in findMissingInPlace
    using(arrays) curve "findMissingSet" in findMissingSet
  }

  /* Gen.range example
    -> generates a range of values with a min, max, and interval
   */

  performance of "Example #2: randArray(n, 0, 1) -- linear" in {

    val arrayLength = Gen.range("n")(100, 1000, 100)

    val arrays: Gen[Array[Int]] = for {
      length <- arrayLength
    } yield randArray(length, 0, 1)

    using(arrays) curve "findMissingMutable" in { findMissingMutable }
    using(arrays) curve "findMissingInPlace" in { findMissingInPlace }
    using(arrays) curve "findMissingSet" in { findMissingSet }
  }

  /* Gen.exponential example
    -> generates an exponentially-spaced range of values
    -> uses a starting value and a multiplicative factor
   */

  performance of "Example #3: randArray(n, 0, 1) -- exponential" in {

    val arrayLength = Gen.exponential("n")(2, Math.pow(2, 16).toInt, 2)

    val arrays: Gen[Array[Int]] = for {
      length <- arrayLength
    } yield randArray(length, 0, 1)

    using(arrays) curve "findMissingMutable" in { findMissingMutable }
    using(arrays) curve "findMissingInPlace" in { findMissingInPlace }
    using(arrays) curve "findMissingSet" in { findMissingSet }
  }

  performance of "Example #4: randArray(a, b, c)" in {

    val arrayLength = Gen.exponential("n")(2, Math.pow(2, 20).toInt, 2)
    val pctBad = Gen.range("fracBad")(0, 100, 5)
    val pctScaling = Gen.range("maxScaling")(25, 300, 5)

    val arrays: Gen[Array[Int]] = for {
      length <- arrayLength
      bad <- pctBad
      scale <- pctScaling
    } yield randArray(length, bad / 100.0, scale / 100.0)

    using(arrays) curve "findMissingMutable" in { findMissingMutable }
    using(arrays) curve "findMissingInPlace" in { findMissingInPlace }
    using(arrays) curve "findMissingSet" in { findMissingSet }
  }

}