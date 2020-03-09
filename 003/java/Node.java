/*
 * Daily Coding Problem: 003
 *
 * Given the root to a binary tree, implement serialize(root), which serializes
 * the tree into a string, and deserialize(s), which deserializes the string
 * back into the tree.
 *
 * For example, given the following Node class
 *
 * class Node:
 *     def __init__(self, val, left=None, right=None):
 *         self.val = val
 *         self.left = left
 *         self.right = right
 *
 * The following test should pass:
 *
 * node = Node('root', Node('left', Node('left.left')), Node('right'))
 * assert deserialize(serialize(node)).left.left.val == 'left.left'
 */

public class Node {

  private String val;
  private Node left;
  private Node right;

  public Node (String val, Node left, Node right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  public Node (String val, Node left) {
    this(val, left, null);
  }

  public Node (String val) {
    this(val, null, null);
  }

  public Node left() { return this.left; }

  public Node right() { return this.right; }

  public String val() { return this.val; }

  public String toString() {
    return toString(0);
  }

  public String toString (int indent) {

    String spacer = "  ";
    String bump = String.join("", Collections.nCopies(indent, spacer));

    StringBuilder sb = new StringBuilder(bump);
    sb.append("Node: ");

    bump = bump + spacer;

    if (val == null) {
      sb.append("null");

    } else {
      sb.append("\"");
      sb.append(val);
      sb.append("\"");
    }

    if (this.left == null) {
      sb.append("\n");
      sb.append(bump);
      sb.append("null");

    } else {
      sb.append("\n");
      sb.append(this.left.toString(indent+1));
    }

    if (this.right == null) {
      sb.append("\n");
      sb.append(bump);
      sb.append("null");

    } else {
      sb.append("\n");
      sb.append(this.right.toString(indent+1));
    }

    return sb.toString();

  }

  public static Node fromString (String serialized) {

    // is this a non-null Node?
    String marker = "Node: ";
    int valStart = serialized.indexOf(marker);

    // if null, return a null Node
    if (valStart < 0) return null;

    // otherwise, get the `val` of the Node
    valStart += marker.length();
    int valEnd = serialized.indexOf("\n");
    String val = serialized.substring(valStart, valEnd);

    // is `val` null?
    if (val.charAt(0) == '"')
      val = val.substring(1, val.length()-1);
    else val = null;

    // de-dent the serialized representation and look for children
    String modified = serialized.replaceAll("\n  ", "\n");
    modified = modified.substring(valEnd+1);

    // at what character does the `right` child start?
    int rightStart = Math.max(
        modified.indexOf("\nN"),
        modified.indexOf("\nn")
      ) + 1;

    // child node `left`
    Node left = null;

    // if `left` is not `null`
    if (modified.substring(0, 4) != "null")
      left = fromString(modified.substring(0, rightStart));

    // child node `right`
    Node right = null;

    // if `right` is not `null`
    if (modified.substring(rightStart, rightStart+4) != "null")
      right = fromString(modified.substring(rightStart));

    return new Node(val, left, right);
  }

}
