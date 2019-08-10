package DCP;

public class DLLNode {

  // number of "bytes" to allocate for a DLL
  private static final int size = 20;

  public  Object data; // data held by this DLL element
  private String next; // address of next DLL element
  private String prev; // address of previous DLL element
  private String addr; // address of this DLL element

  // constructor with no "next" or "prev" elements
  public DLLNode (Object data) {

    this.data = data;
    this.next = "0x00000000"; // null
    this.prev = "0x00000000"; // null

    // allocate memory for this DLL element
    this.addr = Memory.malloc(size);

    // add to reference table
    Memory.refTable.put(this.addr, this);

  }

  // method to get a "pointer" to this object ("get_pointer")
  public String ptr() { return this.addr; }

  // getters for next and prev
  public String next() { return this.next; }
  public String prev() { return this.prev; }

  // setters for next and prev
  protected void next(String addr) { this.next = addr; }
  protected void prev(String addr) { this.prev = addr; }

}