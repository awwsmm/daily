case class Node (value: String, left: Node = null, right: Node = null) {
  require (value != null)
  override def toString = s"""Node("${value.replaceAll("\"", "\\\\\"")}",$left,$right)"""
}

def fromString(serialized: String): Node = {
  val arbitrary = """Node\("((?:[^\"]|\\")*)",(null|Node\(.*\)),(null|Node\(.*\))\)""".r
  serialized match {
    case arbitrary(value,left,right) => Node(
      value.replaceAll("\\\\\"", "\""),
      if (left == "null") null else fromString(left),
      if (right == "null") null else fromString(right))
  }
}

// that's the entire implementation!

// ...now for the tests...

val rock = Node("""Dwayne "The Rock" Johnson""",null,null)

val list = Node("My list is List(1,2,3)",null,null)

val nodes = Node("null",rock,list)

// verify that a serialized Node is equivalent to one that's been
// serialized, then deserialized, then serialized again

assert(rock.toString  == fromString(rock.toString).toString)

assert(list.toString  == fromString(list.toString).toString)

assert(nodes.toString == fromString(nodes.toString).toString)

// run the test from the prompt

val node = Node("root",Node("left",Node("left.left")),Node("right"))

assert(fromString(node.toString).left.left.value == "left.left")
