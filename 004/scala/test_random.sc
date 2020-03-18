import scala.util.Random

val rand = new Random

// random array generator
def randArray (n: Int, fracBad: Double, maxScaling: Double): Array[Int] = {
  require(fracBad >= 0 && fracBad <= 1, "fraction of \"bad\" elements must be between 0 and 1")
  require(maxScaling >= 0, "maxScaling must be >= 0")
  require(n >= 0, "array length must be >= 0")

  if (n == 0) Array()
  else if (fracBad == 1.0) Array.fill(n)(0)
  else {
    val maxVal = n * maxScaling
    val minVal = 0.5 - fracBad / (1 - fracBad) * maxVal

    val range = f"range [$minVal%.3f, $maxVal%.3f]"
    require(n == 0 || minVal <= maxVal, s"invalid $range")
    println(f"range [${minVal.round}, ${maxVal.round}]") // uncomment to see ranges below

    if (minVal == maxVal) Array.fill(n)(minVal.toInt)
    else (1 to n).map(_ =>
      rand.between(minVal, maxVal).round.toInt).toArray
  }
}

println("// n == 0 always returns empty Array")
randArray(0, 0, 0)

println("// fracBad == 1 always returns Array full of zeroes")
randArray(10, 1, 1)

println("// non-linear scaling of range of values to satisfy fracBad")
randArray(10, 0.009, 1)
randArray(10, 0.09, 1)
randArray(10, 0.9, 1)
randArray(10, 0.99, 1)
randArray(10, 0.999, 1)

println("// long arrays with low maxScaling and low fracBad should have many duplicates")
randArray(10, 0, 0.3)
randArray(20, 0, 0.4)
randArray(50, 0, 0.5)

print("// example from article should give range [-2, 30]")
randArray(20, 1.0/11.0, 1.5)

import scala.util.control.Breaks._

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

            if (debug) {
              println(array.mkString("[", ", ", "]"))
              println(s"...swapping the $index-th and $swapIndex-th values (1-based indexing)\n")
            }

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

      if (debug)
        println("array[ii]: " + array(ii) + ", smallestMissing: " + smallestMissing)

      if (array(ii) >= 1) {
        if (array(ii) != smallestMissing) return smallestMissing
        else smallestMissing += 1
      }
    }

    smallestMissing
  }
}

// long example to see sorting process
findMissingMutable(Array[Int](1, 0, 3, 0, 4, 0, 2, 0, 5, 0, 6, 0, 2, 0, 0, 0, 7, 8, 9, 0), debug = true)

// examples from prompt
findMissingMutable(Array(3, 4, -1, 1))
findMissingMutable(Array(1, 2, 0))