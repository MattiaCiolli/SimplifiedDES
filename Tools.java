package SimplifiedDES;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mattia on 03/06/16.
 */

public class Tools {

    // expands the right part of the message (6 bits) to allow the XOR with the i-th key (8 bits)
    public static String expand(String msg1) {

        String[] ret = new String[8];

        // creates a new string of 8 bits with one of 6 bits
        ret[0] = Character.toString(msg1.charAt(0));
        ret[1] = Character.toString(msg1.charAt(1));
        ret[2] = Character.toString(msg1.charAt(3));
        ret[3] = Character.toString(msg1.charAt(2));
        ret[4] = Character.toString(msg1.charAt(3));
        ret[5] = Character.toString(msg1.charAt(2));
        ret[6] = Character.toString(msg1.charAt(4));
        ret[7] = Character.toString(msg1.charAt(5));

        String ret1 = Arrays.toString(ret);
        ret1 = ret1.substring(1, ret1.length() - 1).replaceAll("\\W", ""); //cleaning

        return ret1;
    }

    // adjust strings length padding with zeroes until the needed length is reached.
    // used to avoid a problem using strings: When parsed, the initial zeroes are suppressed.
    public static String adjustLength(String s, int l) {
        int lengthNeeded = l - s.length();

        if (s.length() < l) {

            while (lengthNeeded > 0) {
                s = "0" + s; // adds zeroes at the left of the string
                lengthNeeded--;
            }
        } else if (s.length() > l) {

            s = splitText(s, s.length() - l)[1]; // takes the needed length string
        }

        return s;
    }

    // prints the value passed in input in bits applying a mask to show only the bits needed
    public static void printBits(int x, int n) {
        int mask = 1 << (n - 1);
        while (mask > 0) {
            System.out.print(((x & mask) == 0) ? '0' : '1'); // applies a mask
            mask >>= 1;
        }
    }

    // splits the text passed in input
    public static String[] splitText(String s, int pos) {

        String[] parts = {s.substring(0, pos), s.substring(pos)}; // splits the string

        return parts;

    }

    // cleans null elements in strings
    public static String[] cleanString(String[] a) {

        ArrayList<String> removed = new ArrayList<String>();
        for (String str : a)
            if (str != null)
                removed.add(str);
        return removed.toArray(new String[0]);
    }

    // concatenates 2 strings
    public static String[] concatenate(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

}
