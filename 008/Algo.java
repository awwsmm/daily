import DCP.BinaryNode;

BinaryNode root  = (new BinaryNode.Builder(0)).build();
BinaryNode nodeA = (new BinaryNode.Builder(0).parent(root)).build();
BinaryNode nodeB = (new BinaryNode.Builder(1).parent(root)).build();
BinaryNode nodeC = (new BinaryNode.Builder(0).parent(nodeB)).build();
BinaryNode nodeD = (new BinaryNode.Builder(1).parent(nodeB)).build();
BinaryNode nodeE = (new BinaryNode.Builder(1).parent(nodeD)).build();
BinaryNode nodeF = (new BinaryNode.Builder(1).parent(nodeD)).build();

root.valueTree();

BinaryNode thisnode = root;

while (true) {

  // (1a)
  if (thisnode.unival() == null) {

    // (2a)
    if (thisnode.left() == null && thisnode.right() == null) {
      thisnode.unival(thisnode.value());

    // (2b)
    } else if (thisnode.left() != null ^ thisnode.right() != null) {
      thisnode = (thisnode.left() == null ? thisnode.right() : thisnode.left());

    // (2c)
    } else {
      if (thisnode.left() != null && thisnode.left().unival() != null) {

        if (thisnode.right() != null && thisnode.right().unival() != null)
          thisnode.unival((
            thisnode.left().value() == thisnode.right().value() &&
            thisnode.left().unival() >= 0  &&
            thisnode.left().value() == thisnode.value()
              ? thisnode.left().value()
              : -1
          ));

        else thisnode = thisnode.right();

      } else thisnode = thisnode.left();
    }

  // (1b)
  } else {
    BinaryNode parent = thisnode.parent();

    // (1bi)
    if (parent != null) thisnode = parent;
    else break;
  }

}

System.out.println();
root.univalTree();

public int countUnivalSubtrees (BinaryNode node) {

  boolean hasLeft  = (node.left() != null);
  boolean hasRight = (node.right() != null);

  int sumLeft  = hasLeft  ? countUnivalSubtrees(node.left()) : 0;
  int sumRight = hasRight ? countUnivalSubtrees(node.right()) : 0;

  return((node.unival() >= 0 ? 1 : 0) + sumLeft + sumRight);
}

System.out.println("\nNumber of unival subtrees: " + countUnivalSubtrees(root));