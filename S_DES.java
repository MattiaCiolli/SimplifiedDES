package SimplifiedDES;

/**
 * Created by mattia on 03/06/16.
 */
public class S_DES {


    private static String S1[][] = {{"101", "010", "001", "110", "011", "100", "111", "000"}, {"001", "100", "110", "010", "000", "111", "101", "011"}}; // S-Box 1

    private static String S2[][] = {{"100", "000", "110", "101", "111", "001", "011", "010"}, {"101", "011", "000", "111", "110", "010", "001", "100"}}; // S-Box 2

    private static int K;
    private static String M, Cm, M1;
    private static int[] SubK = new int[4];

    public static int[] getSubK() {
        return SubK;
    }


    //getters and setters

    public static void setSubK(int[] sk) {
        SubK = sk;
    }

    public static int getSubK(int i) {
        return SubK[i];
    }

    public static String getCm() {
        return Cm;
    }

    public static void setCm(String cm) {
        Cm = cm;
    }

    public static String getM() {
        return M;
    }

    public static void setM(String m) {
        M = m;
    }

    public static String getM1() {
        return M1;
    }

    public static int getK() {
        return K;
    }

    public static void setK(int k) {
        K = k;
    }

    public static String getS2(int i, int j) {
        return S2[i][j];
    }

    public static String getS1(int i, int j) {
        return S1[i][j];
    }


    // SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encrypt(int k[], String m, int r) {

        System.out.print("\nEncryption: \n");

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = 0; i < r; i++) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]); // right half expanded and XORed with the i-th key

            cmr = Tools.adjustLength(cmr, 8); // fixes eventually  zeroes lost in conversion

            parts = Tools.splitText(cmr, cmr.length() / 2); // halves string

            // strings used to calculate the S-BOXES coordinates (left and right half of the expanded and XORed part)
            String XL = parts[0];
            String XR = parts[1];

            // S1 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XL, 1); //halves string
            String S1row = parts[0];
            String S1col = parts[1];

            // S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XR, 1); //halves string
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2))); // 6 bits given by the S-BOXES
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);

            cmr = Tools.adjustLength(cmr, 6); // fixes eventually  zeroes lost in conversion
            cml = Integer.parseInt(mrt, 2); // swaps the left part with the right one
            mrt = cmr; // swaps the right part with the left one

            if (i == 1) {
                M1 = Tools.adjustLength(Integer.toBinaryString(cml) + mrt, 12);
            }

            System.out.print("Round " + (i + 1) + ": ");
            Tools.printBits(Integer.parseInt(Integer.toBinaryString(cml) + mrt, 2), 12);
            System.out.print("\n");

        }

        return Integer.toBinaryString(cml) + mrt;
    }

    // SDES-decrypts the cypher-text in input with the key in input. Variable number of rounds
    public static String decrypt(int k[], String m, int r) {

        // System.out.print("\n\nDecryption: \n");
        m = Tools.adjustLength(m, 12); // fixes eventually  zeroes lost in conversion
        String[] parts = Tools.splitText(m, m.length() / 2);//halves the cyphertext in input
        String mR1 = parts[0]; // swaps right half with the left one (index 0->left part)
        String mL1 = parts[1]; // swaps the left part with the right one (index 1->right part)

        String mrt = mR1; // initial initialization
        int cml = Integer.parseInt(mL1, 2);

        for (int i = r - 1; i >= 0; i--) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]); // left half expanded and XORed with the r-i-th key

            cmr = Tools.adjustLength(cmr, 8); // fixes eventually  zeroes lost in conversion
            parts = Tools.splitText(cmr, cmr.length() / 2); // halves string

            // strings used to calculate the S-BOXES coordinates (left and right half of the expanded and XORed part)
            String XL = parts[0];
            String XR = parts[1];

            // S1 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XL, 1); //halves string
            String S1row = parts[0];
            String S1col = parts[1];

            // S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XR, 1); // halves string
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2))); // 6 bits given by the S-BOXES
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);
            cmr = Tools.adjustLength(cmr, 6); // fixes eventually  zeroes lost in conversion
            cml = Integer.parseInt(mrt, 2); // swaps the left part with the right one
            mrt = cmr; // swaps the right part with the left one

        }

        return mrt + Tools.adjustLength(Integer.toBinaryString(cml), 6); // last swap performed here
    }

    // generate 4 keys of 8 bits starting from the key of 9 bits inserted
    // performs a circular shift on the subkeys
    public static int[] generateKeys(String k) {

        int[] sk = new int[4];
        String[] partsK = Tools.splitText(k, 8); // takes the first 8 bits of K
        String k1 = partsK[0];

        sk[0] = Integer.parseInt(k1, 2);

        partsK = Tools.splitText(k, 1);
        k1 = partsK[1];
        sk[1] = Integer.parseInt(k1, 2); // K2=K from second bit

        partsK = Tools.splitText(k, 2);
        k1 = partsK[1] + k.charAt(0);
        sk[2] = Integer.parseInt(k1, 2); // K3=K from third bit+first bit

        partsK = Tools.splitText(k, 3);
        k1 = partsK[1] + k.charAt(0) + k.charAt(1);
        sk[3] = Integer.parseInt(k1, 2); // K4=K from fourth bit+first bit+second bit

        return sk;

    }

}

