def codingProblem002 (given: Array[Int]): Array[Int] = {
  val prod = given.product
  given.map(e => prod / e)
}

codingProblem002(Array(1, 2, 3, 4, 5))