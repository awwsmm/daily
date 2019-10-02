package DCP;

public class BinaryNode {

  public static class Builder {

    private Integer    value  = null;
    private BinaryNode left   = null;
    private BinaryNode right  = null;

    private BinaryNode parent         = null;
    private Boolean    parentHasLeft  = null;
    private Boolean    parentHasRight = null;

    public Builder (Integer value) {
      this.value = value;
    }

    public Builder left (BinaryNode left) {

      if (left.parent() != null)
        throw new IllegalStateException(
          "ERROR: left node already has a parent");

      this.left = left;
      return this;
    }

    public Builder right (BinaryNode right) {

      if (right.parent() != null)
        throw new IllegalStateException(
          "ERROR: right node already has a parent");

      this.right = right;
      return this;
    }

    public Builder parent (BinaryNode parent) {

      if (parent == null) return this;

      this.parentHasLeft  = (parent.left()  != null);
      this.parentHasRight = (parent.right() != null);

      if (this.parentHasLeft && this.parentHasRight)
        throw new IllegalStateException(
          "ERROR: parent already has two children");

      this.parent = parent;
      return this;
    }

    public BinaryNode build() {

      BinaryNode node = new BinaryNode(this.value);

      node.left   = this.left;
      node.right  = this.right;
      node.parent = this.parent;

      if (this.left  != null) this.left.parent(node);
      if (this.right != null) this.right.parent(node);

      if (this.parent != null) {
        if (this.parentHasLeft) this.parent.right(node);
        else                    this.parent.left(node);
      }

      return node;
    }
  }

  private Integer    value    = null;
  private BinaryNode parent   = null;
  private BinaryNode left     = null;
  private BinaryNode right    = null;
  private Integer    unival   = null;

  private BinaryNode (Integer value) {
    if (value < 0 || value > 1)
      throw new IllegalArgumentException(
        "this is a binary tree... `value` can only be 0 or 1");
    this.value = value;
  }

  public Integer value() {
    return this.value;
  }

  public BinaryNode parent() {
    return this.parent;
  }

  private void parent (BinaryNode parent) {
    this.parent = parent;
  }

  public BinaryNode left() {
    return this.left;
  }

  private void left (BinaryNode left) {
    this.left = left;
  }

  public BinaryNode right() {
    return this.right;
  }

  private void right (BinaryNode right) {
    this.right = right;
  }

  public Integer unival() {
    return this.unival;
  }

  public void unival (Integer unival) {
    this.unival = unival;
  }

  public void valueTree() { tree(false, 10, 10, "", false); }

  public void univalTree() { tree(true, 10, 10, "", false); }

  // some code here based on http://bit.ly/2HgFBRo
  private void tree (boolean unival, int nLevelsUp, int nLevelsDown, String indent, boolean isTail) {

    // start with any node on the tree
    BinaryNode node = this;

    // find eldest allowed node using nLevelsUp
    for (int ii = 0; ii < nLevelsUp; ++ii) {
      if (node.parent != null) {
        node = node.parent;
        ++nLevelsDown;
      } else {
        --nLevelsUp;
      }
    }

    // get number of ancestors of this node
    BinaryNode ptr = this;
    int gen = 0;

    while (ptr.parent() != null) {
      ++gen; ptr = ptr.parent();
    }

    Integer treeValue = unival ? node.unival : node.value;
    String treeLabel = (treeValue == null ? "null" : treeValue.toString());

    System.out.printf("  %3d %s|-- %s%n", gen, indent, treeLabel);

    int nChildren = (this.left == null ? 0 : 1) + (this.right == null ? 0 : 1);
    BinaryNode lastChild = (nChildren > 1 ? this.right : this.left);

    if (nLevelsDown > 0) {
      if (nChildren > 1)
        this.left.tree(unival, 0, nLevelsDown-1, indent + (isTail ? "    " : "|   "), false);

      if (nChildren > 0)
        lastChild.tree(unival, 0, nLevelsDown-1, indent + (isTail ? "    " : "|   "), true);
    }

    return;
  }

}



