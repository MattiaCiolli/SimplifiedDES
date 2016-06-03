import java.io.DataInputStream;
import java.util.Arrays;

public class SimplifiedDES {
    public static final String S1[][] = {{"101", "010", "001", "110", "011", "100", "111", "000"}, {"001", "100", "110", "010", "000", "111", "101", "011"}};
    public static final String S2[][] = {{"100", "000", "110", "101", "111", "001", "011", "010"}, {"101", "011", "000", "111", "110", "010", "001", "100"}};

    public static int K, mL, mR;
    public static int[] SubK;


    //expands the right part of the message (6 bits) to allow the XOR with the i-th key (8 bits)
    public static String expand(String msg1) {

        String[] ret = new String[8];

        ret[0] = Character.toString(msg1.charAt(0));
        ret[1] = Character.toString(msg1.charAt(1));
        ret[2] = Character.toString(msg1.charAt(3));
        ret[3] = Character.toString(msg1.charAt(2));
        ret[4] = Character.toString(msg1.charAt(3));
        ret[5] = Character.toString(msg1.charAt(2));
        ret[6] = Character.toString(msg1.charAt(4));
        ret[7] = Character.toString(msg1.charAt(5));

        String ret1 = Arrays.toString(ret);
        ret1 = ret1.substring(1, ret1.length() - 1).replaceAll("\\W", "");

        return ret1;
    }

    //adjust strings length padding with zeroes until the needed length is reached.
    //Used to avoid a problem using strings: When parsed, the initial zeroes are suppressed.
    public static String adjustLength(String s, int l) {
        int lengthNeeded = l - s.length();
        while (lengthNeeded > 0) {
            s = "0" + s;
            lengthNeeded--;
        }
        return s;
    }

    //prints the value passed in input in bits applying a mask to show only the bits needed
    public static void printBits(int x, int n) {
        int mask = 1 << (n - 1);
        while (mask > 0) {
            System.out.print(((x & mask) == 0) ? '0' : '1');
            mask >>= 1;
        }
    }

    //splits the text passed in input
    public static String[] splitText(String s, int pos) {

        String[] parts = {s.substring(0, pos), s.substring(pos)}; //taglia la stringa a meta

        return parts;

    }

    //SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encrypt(int k[], String m, int r) {

        String[] parts =SimplifiedDES.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);
        for (int i = 0; i < r; i++) {
            String cmr = Integer.toBinaryString(Integer.parseInt(expand(mrt), 2) ^ k[i]);

            cmr = SimplifiedDES.adjustLength(cmr, 8);

            parts = SimplifiedDES.splitText(cmr, cmr.length() / 2); //taglia la stringa a meta
            String XL = parts[0];
            String XR = parts[1];


            parts = SimplifiedDES.splitText(XL, 1); //taglia la stringa a meta
            String S1row = parts[0];
            String S1col = parts[1];

            parts = SimplifiedDES.splitText(XR, 1); //taglia la stringa a meta
            String S2row = parts[0];
            String S2col = parts[1];
            String Sresult = S1[Integer.parseInt(S1row, 2)][Integer.parseInt(S1col, 2)] + S2[Integer.parseInt(S2row, 2)][Integer.parseInt(S2col, 2)];
            cmr = Integer.toBinaryString(Integer.parseInt(Sresult, 2) ^ cml);

            cmr = SimplifiedDES.adjustLength(cmr, 6);
            cml = Integer.parseInt(mrt, 2);
            mrt = cmr;
        }
        return Integer.toBinaryString(cml) + mrt;
    }

    //generate 4 keys of 8 bits starting from the key of 9 bits inserted
    //performs a circular shift on the subkeys
    public static void generateKeys(String k) {

        String[] partsK = SimplifiedDES.splitText(k, 8); //taglia la stringa a meta
        String k1 = partsK[0];

        SubK = new int[4];

        K = Integer.parseInt(k, 2);

        SubK[0] = Integer.parseInt(k1, 2);

        System.out.print("K: ");
        SimplifiedDES.printBits(K, 9);
        System.out.print("\n");

        System.out.print("K1: ");
        SimplifiedDES.printBits(SubK[0], 8);
        System.out.print("\n");

        System.out.print("K2: ");
        SubK[1] = (SubK[0] << 1) | (SubK[0] >> 8);
        SimplifiedDES.printBits(SubK[1], 8);
        System.out.print("\n");

        System.out.print("K3: ");
        SubK[2] = (SubK[1] << 1) | (SubK[1] >> 8);
        SimplifiedDES.printBits(SubK[2], 8);
        System.out.print("\n");

        System.out.print("K4: ");
        SubK[3] = (SubK[2] << 1) | (SubK[2] >> 8);
        SimplifiedDES.printBits(SubK[3], 8);
        System.out.print("\n");
    }


    public static void main(String args[]) throws Exception {

        DataInputStream inp = new DataInputStream(System.in);

        System.out.println("Enter the 9 Bit Key :");
        String k = inp.readLine();

        System.out.println("Enter the 12 Bit message :");
        String m = inp.readLine();

        String[] parts = SimplifiedDES.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        mR = Integer.parseInt(mR1, 2);
        mL = Integer.parseInt(mL1, 2);

        System.out.print("mL: ");
        SimplifiedDES.printBits(mL, 6);
        System.out.print("\n");
        System.out.print("mR: ");
        SimplifiedDES.printBits(mR, 6);
        System.out.print("\n\n");

        SimplifiedDES.generateKeys(k);

        System.out.print("Encrypted message: ");
        SimplifiedDES.printBits(Integer.parseInt(SimplifiedDES.encrypt(SubK, m, 1), 2), 12);



    }
}

