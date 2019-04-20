/*
 * Daily Coding Problem: 001
 *
 * Given a list of numbers and a number k, return whether any two numbers from the list add up to k.
 *
 * For example, given [10, 15, 3, 7] and k of 17, return true since 10 + 7 is 17.
 *
 * Bonus: Can you do this in one pass?
 */

public class DCP001 {

  public static boolean codingProblem001 (int[] given, int k) {

    int len = given.length;

    for (int outer = 0; outer < (len - 1); ++outer) {
      for (int inner = outer + 1; inner < len; ++inner) {

        if (given[outer] + given[inner] == k)
          return true;

      }
    }

    return false;
  }

}
