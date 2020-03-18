import scala.annotation.tailrec
import scala.util.control.Breaks._
import scala.util.Random

val rand = new Random

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
def findMissingMutable (array: Array[Int], debug: Boolean = false): Int = {
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

@tailrec
def randParams(attempt: Int = 1): (Int, Double, Double, Int) = {

  require(attempt < 100, "aborted: > 100 attempts to generate parameters")

  def randN: Int = (rand.nextDouble * 10 * Math.pow(10, rand.nextInt(5))).toInt
  def randScaling: Double = rand.nextDouble * 1.25

  val n = randN
  val fracBad = rand.nextDouble
  val maxScaling = randScaling
  val maxVal = n * maxScaling
  val minVal = 1 - fracBad / (1 - fracBad) * maxVal

  if (maxVal >= minVal) (n, fracBad, maxScaling, attempt)
  else randParams(attempt+1)
}


for (ii <- 1 to 100) {
  println(f"....................\n. test index: $ii%3d")

  val (n, fracBad, maxScaling, _) = randParams()

  println(s".           n: $n")
  println(s".     fracBad: $fracBad")
  println(s".  maxScaling: $maxScaling")

  val arr1 = randArray(n, fracBad, maxScaling)
  val arr2 = arr1.clone
  val arr3 = arr1.clone

  println(". val arr = " + arr1.mkString("Array(", ", ", ")"))

  // must run mutable version last
  val v1 = findMissingMutable(arr1)
  val v2 = findMissingInPlace(arr2)
  val v3 = findMissingSet(arr3)

  println(s". findMissingMutable(arr) == $v1")
  println(s". findMissingInPlace(arr) == $v2")
  println(s". findMissingSet(arr)     == $v3")

  // assert that the return values are equal
  assert(v1 == v2 && v2 == v3, "return values unequal")
}