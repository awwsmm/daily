/*
 * Daily Coding Problem: 002
 *
 * Given an array of integers, return a new array such that each element at index i of the new array is the product of all the numbers in the original array except the one at i.
 *
 * For example, if our input was [1, 2, 3, 4, 5], the expected output would be [120, 60, 40, 30, 24]. If our input was [3, 2, 1], the expected output would be [2, 3, 6].
 *
 * Follow-up: what if you can't use division?
 */

public class DCP002 {

  public static int[] codingProblem002 (int[] given) {

    int product = 1;
    final int len = given.length;
    int[] retval = new int[len];

    for (int element : given) {
      product *= element;
      retval[i] = element;
    }

    for (int i = 0; i < len; ++i)
      retval[i] = product / retval[i];

    return retval;

  }

}
