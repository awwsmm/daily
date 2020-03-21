import scala.util.control.Breaks._
import scala.util.Random
import org.scalameter.api._

// see Scaladoc at
//   https://www.javadoc.io/doc/com.storm-enroute/scalameter_2.13/latest/org/scalameter/index.html

object Benchmark extends Bench.OfflineReport {

  private val rand = new Random

  // random array generator
  def randArray (n: Int, fracBad: Double, maxScaling: Double): Array[Int] = {
    if (n == 0) Array()
    else if (fracBad == 1.0) Array.fill(n)(0)
    else {
      val maxVal = n * maxScaling
      val minVal = 1 - fracBad / (1 - fracBad) * maxVal

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

  val lengths = Gen.exponential("n")(10, 10000, 2)
  //  val fracBad = Gen.range("fracBad")(0, 1, 0.05)
  //  val maxScaling = Gen.range("maxScaling")(0.1, 1.25, 0.05)

  val arrays: Gen[IndexedSeq[Array[Int]]] = for {
    length <- lengths
  } yield (1 until length).map(e => randArray(e, 0, 1))

  performance of "Benchmark" in {
    measure method "findMissingMutable" in {
      using(arrays) in {
        arr => arr.foreach(findMissingMutable)
      }
    }
    measure method "findMissingInPlace" in {
      using(arrays) in {
        arr => arr.foreach(findMissingInPlace)
      }
    }
    measure method "findMissingSet" in {
      using(arrays) in {
        arr => arr.foreach(findMissingSet)
      }
    }
  }

}
