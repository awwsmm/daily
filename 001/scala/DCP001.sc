val ll: List[Int] = List(10, 15, 3, 7)

val k: Int = 17

// with syntactic sugar
ll.combinations(2).exists(_.sum == k)

// de-sugared
ll.combinations(2).exists(
  (innerList: List[Int]) => innerList.sum == k
)