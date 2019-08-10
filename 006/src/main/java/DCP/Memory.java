package DCP;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Memory {

  private static Random random = new Random();

  // return any address except "0x00000000", the "NULL" address
  public static String randomAddress() {
    int value = random.ints(1L, Integer.MIN_VALUE, Integer.MAX_VALUE).toArray()[0] + 1;
    return intToAddress(value);
  }

  // convert a hex address (ex. "0x4F5E8A90") to an int
  public static int addressToInt(String address) {

    // throw error if not hex-formatted
    if (!address.startsWith("0x"))
      throw new IllegalArgumentException(
        String.format("address '%s' must begin with \"0x\"", address));

    // throw error if any non-hex characters
    if (!address.substring(2).matches("[0-9A-Fa-f]*"))
      throw new IllegalArgumentException(
        String.format("address '%s' must only contain hex characters", address));

    // throw error if illegal length
    if (address.length() < 3 || address.length() > 10)
      throw new IllegalArgumentException(
        String.format("address '%s' must contain be within range [0x00000000, 0xFFFFFFFF]", address));

    // if no problems, attempt to parse
    return (int)(Long.parseLong(address.substring(2), 16) + Integer.MIN_VALUE);
  }

  // convert an int to a hex address
  public static String intToAddress(int value) {
    long longValue = (long)value - (long)Integer.MIN_VALUE;
    return String.format("0x%8s", Long.toString(longValue, 16).toUpperCase()).replaceAll(" ", "0");
  }

  // return the first address of a free block of memory with given size
  public static String malloc(long size) {

    // if object has zero size, return NULL address
    // https://stackoverflow.com/a/2022369/2925434

    if (size < 1) return "0x00000000";

    // if object cannot fit in memory, throw error
    // leave one empty memory address at 0x00000000 for "NULL"
    // https://stackoverflow.com/a/19750838/2925434

    if (size > (long)Integer.MAX_VALUE - (long)Integer.MIN_VALUE )
      throw new IllegalArgumentException("insufficient memory");

    // if object can fit in memory, get largest possible initial memory address

    long first = Integer.MIN_VALUE;
    long last  = (long)Integer.MAX_VALUE - size;

    // if only one possible memory address, return that one
    if (first == last) return "0x00000001";

    // ...else, randomise over valid range
    int value = random.ints(1L, (int)first, (int)last).toArray()[0] + 1;

    // ...register memory as "occupied"
    occupied.addAll(IntStream.range(value, (int)(value+size)).boxed().collect(Collectors.toList()));

    // ...and return as address
    return intToAddress(value);

  }

  // keep track of which int-indexed blocks are occupied by data
  private static HashSet<Integer> occupied = new HashSet<>(Arrays.asList(Integer.MIN_VALUE));

  // free memory within a certain range
  public static void free(String iAddress, String fAddress) {
    int iAdd = addressToInt(iAddress);
    int fAdd = addressToInt(fAddress);

    // remove all addresses in range
    occupied.removeAll(IntStream.range(iAdd, fAdd).boxed().collect(Collectors.toList()));

    // check that "NULL" is still "occupied"
    occupied.add(Integer.MIN_VALUE);
  }

  // free all memory
  public static void free() {
    free("0x00000001", "0xFFFFFFFF");
  }

  // list of objects in memory
  protected static HashMap<String, Object> refTable = new HashMap<>();
  static { refTable.put("0x00000000", null); }

  // dereference object
  public static Object dereference(String address) {
    return refTable.get(address);
  }


}



