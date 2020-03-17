public class DCP004 {

  public static void main (String[] args) {
    System.out.println(findSmallestMissing(new int[]{  3,  4, -1,  1 }));
    System.out.println(findSmallestMissing(new int[]{  1,  2,  0 }));
  }

  public static int findSmallestMissing (int[] array) {

    // if array is null or empty, 1 is the smallest missing number
    if (array == null || array.length < 1) return 1;

    // get the length of the array
    int len = array.length;

    // assume 1 is smallest missing number until proven otherwise
    int smallestMissing = 1;

    // loop over input array (steps 1 and 5)
    for (int ii = 0; ii < len; ++ii) {

      // step 3 (make sure to use 1-based indexing)
      if (array[ii] == (ii+1) || array[ii] < 1 || array[ii] > len)
        continue;

      // step 4
      while (array[ii] != (ii+1)) {

        // DEBUG: print array
        System.out.println(Arrays.toString(array));

        // index of the element to swap with
        int swap = array[ii]-1;

        // value of the element to swap with
        int temp = array[swap];

        // swap the values
        array[swap] = array[ii];
        array[ii] = temp;

        // if the new value is < 1 or > len, move to next one
        if (temp < 1 || temp > len) break;

        // if new value equals old value, move to next one
        if (temp == array[ii]) break;

      }
    }

    // loop over modified array
    for (int ii = 0; ii < len; ++ii) {

      // DEBUG: see search process
      System.out.println("array[ii]: " + array[ii] + ", smallestMissing: " + smallestMissing);

      if (array[ii] < 1) continue;
      if (array[ii] != smallestMissing) return smallestMissing;
      ++smallestMissing;
    }

    return smallestMissing;
  }

}
