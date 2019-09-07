// package DCP;

public class Message {

  public static int[] digitise (String message) {
    return Stream.of(message.split("")).mapToInt(s -> Integer.parseInt(s)).toArray();
  }

  public static boolean startsWithValidTwoDigitSeq (int[] message) {
    if (message.length < 2) return false;
    if (message[0] > 2 || message[0] < 1) return false;
    if (message[0] == 2 && message[1] > 6) return false;
    return true;
  }

  public static int sum = 0;

  public static int nValidDecodings (int[] digits) {
    sum = 0;
    sumDecodings(digits);
    return sum;
  }

  private static void sumDecodings (int[] digits) {

    // (1) if there are no digits left...
    if (digits.length < 1) return;

    // (2) if there's only 1 digit left...
    if (digits.length < 2) {

      // (2a) ...and it's not a valid encoding of a character
      if (digits[0] == 0) return;

      // (2b) ...and it's a valid encoding of a character
      sum += 1;
      return;
    }

    // (3) if there are only 2 digits left...
    if (digits.length < 3) {

      // (3a) ...and it's a valid encoding of a character
      if (startsWithValidTwoDigitSeq(digits)) sum += 1;

      // (3b) ...fork by removing first digit
      sumDecodings(Arrays.copyOfRange(digits, 1, digits.length));
      return;
    }

    // (4) if there are 3+ digits left...

    // (4a) ...and the first digit is not a valid encoding of a character:
    if (digits[0] == 0) return;

    // (4b) ...and the first two digits are a valid encoding of a character
    if (startsWithValidTwoDigitSeq(digits)) {

      // ...fork with 1-digit number and 2-digit number removed
      sumDecodings(Arrays.copyOfRange(digits, 1, digits.length));
      sumDecodings(Arrays.copyOfRange(digits, 2, digits.length));
      return;

    }

    // (4c) ...and the first digit is a valid encoding of a character:
    sumDecodings(Arrays.copyOfRange(digits, 1, digits.length));
    return;

  }


}
