package DCP;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoublyLinkedList<T> {

  // List of Nodes
  private List<Node<T>> Nodes;

  // get number of Nodes in this List
  public int size() { return this.Nodes.size(); }

  // constructor
  public DoublyLinkedList() {
    this.Nodes = new ArrayList<>();
  }

  // add a Node to the end of the List
  public DoublyLinkedList<T> add(T t) {
    Node<T> newNode = new Node<>(t);

    // if this List already has Nodes
    if (this.size() > 0) {

      // get Node which previously was last Node
      Node<T> oldLastNode = this.Nodes.get(this.size()-1);

      // edit last Node in List to point to _new_ last Node
      oldLastNode.next = newNode.ptr();

      // edit new last Node to point to _old_ last Node
      newNode.prev(oldLastNode.ptr());
    }

    // add new last Node to end of List
    this.Nodes.add(newNode);

    // so add() can be chained
    return this;
  }

  @Override
  public String toString() {
    return this.Nodes.stream().map(n -> n.toString()).collect(Collectors.joining("\n"));
  }

  // nested inner class Node
  class Node<U> {

    // number of "bytes" to allocate for a DLL
    static final int size = 20;
  
    U      data; // data held by this DLL element
    String next; // address of next DLL element
    String prev; // address of previous DLL element
    String addr; // address of this DLL element
  
    // constructor with no "next" or "prev" elements
    public Node (U data) {
  
      this.data = data;
      this.next = "0x00000000"; // null
      this.prev = "0x00000000"; // null
  
      // allocate memory for this DLL element
      this.addr = Memory.malloc(size);
  
      // add to reference table
      Memory.refTable.put(this.addr, this);
  
    }
  
    // method to get a "pointer" to this object ("get_pointer")
    String ptr() { return this.addr; }
  
    // getters for next and prev
    String next() { return this.next; }
    String prev() { return this.prev; }
  
    // setters for next and prev
    void next(String addr) { this.next = addr; }
    void prev(String addr) { this.prev = addr; }

    // toString
    @Override
    public String toString() {

      String prevData = ("0x00000000".equals(this.prev)) ? "null" :
        ((Node)Memory.dereference(this.prev)).data.toString();

      String nextData = ("0x00000000".equals(this.next)) ? "null" :
        ((Node)Memory.dereference(this.next)).data.toString();

      return String.format("%n%10s <- %10s -> %10s%n%10s <- %10s -> %10s",
        this.prev, this.addr, this.next, prevData, this.data, nextData);

    }
  
  }
  
}