package SimplifiedDES;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class S_DES {


    private static String S1[][] = {{"101", "010", "001", "110", "011", "100", "111", "000"}, {"001", "100", "110", "010", "000", "111", "101", "011"}};
    private static String S2[][] = {{"100", "000", "110", "101", "111", "001", "011", "010"}, {"101", "011", "000", "111", "110", "010", "001", "100"}};

    private static int K;
    private static String M, Cm;
    private static int[] SubK = new int[4];

    //getters and setters
    public static int getSubK(int i) {
        return SubK[i];
    }

    public static void setSubK(int v, int p) {
        SubK[p] = v;
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


    //SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encrypt(int k[], String m, int r) {

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = 0; i < r; i++) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]);

            cmr = Tools.adjustLength(cmr, 8);

            parts = Tools.splitText(cmr, cmr.length() / 2); //taglia la stringa a meta
            String XL = parts[0];
            String XR = parts[1];

            parts = Tools.splitText(XL, 1); //taglia la stringa a meta
            String S1row = parts[0];
            String S1col = parts[1];

            parts = Tools.splitText(XR, 1); //taglia la stringa a meta
            String S2row = parts[0];
            String S2col = parts[1];
            String SResult = getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2)));
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);

            cmr = Tools.adjustLength(cmr, 6);
            cml = Integer.parseInt(mrt, 2);
            mrt = cmr;

        }

        return Integer.toBinaryString(cml) + mrt;
    }

    //SDES-decrypts the cypher-text in input with the key in input. Variable number of rounds
    public static String decrypt(int k[], String m, int r) {

        m = Tools.adjustLength(m, 12);
        //System.out.print(m.length()+m);
        String[] parts = Tools.splitText(m, m.length() / 2);
        String mR1 = parts[0];
        String mL1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = r - 1; i >= 0; i--) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]);

            cmr = Tools.adjustLength(cmr, 8);

            parts = Tools.splitText(cmr, cmr.length() / 2);//taglia la stringa a meta
            String XL = parts[0];
            String XR = parts[1];

            parts = Tools.splitText(XL, 1); //taglia la stringa a meta
            String S1row = parts[0];
            String S1col = parts[1];

            parts = Tools.splitText(XR, 1); //taglia la stringa a meta
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2)));
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);

            cmr = Tools.adjustLength(cmr, 6);
            cml = Integer.parseInt(mrt, 2);
            mrt = cmr;

        }

        return mrt + Integer.toBinaryString(cml);
    }

    //generate 4 keys of 8 bits starting from the key of 9 bits inserted
    //performs a circular shift on the subkeys
    public static void generateKeys(String k) {

        String[] partsK = Tools.splitText(k, 8); //taglia la stringa a meta
        String k1 = partsK[0];

        K = Integer.parseInt(k, 2);

        SubK[0] = Integer.parseInt(k1, 2);

        System.out.print("K: ");
        Tools.printBits(K, 9);
        System.out.print("\n");

        System.out.print("K1: ");
        Tools.printBits(SubK[0], 8);
        System.out.print("\n");

        System.out.print("K2: ");
        SubK[1] = (SubK[0] << 1) | (SubK[0] >> 8);
        Tools.printBits(SubK[1], 8);
        System.out.print("\n");

        System.out.print("K3: ");
        SubK[2] = (SubK[1] << 1) | (SubK[1] >> 8);
        Tools.printBits(SubK[2], 8);
        System.out.print("\n");

        System.out.print("K4: ");
        SubK[3] = (SubK[2] << 1) | (SubK[2] >> 8);
        Tools.printBits(SubK[3], 8);
        System.out.print("\n");

    }


    public static void main(String args[]) throws Exception {

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the 9 Bits Key :");
        String k = inp.readLine();
        while (k.length() != 9) {
            System.out.println("Key must be of 9 Bits :");
            k = inp.readLine();
        }
        System.out.println("Enter the 12 Bits message :");
        M = inp.readLine();

        while (M.length() != 12) {
            System.out.println("Message must be of 12 Bits :");
            M = inp.readLine();
        }

        String[] parts = Tools.splitText(getM(), getM().length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        int mR = Integer.parseInt(mR1, 2);
        int mL = Integer.parseInt(mL1, 2);

        System.out.print("mL: ");
        Tools.printBits(mL, 6);
        System.out.print("\n");
        System.out.print("mR: ");
        Tools.printBits(mR, 6);
        System.out.print("\n\n");

        generateKeys(k);

        System.out.print("Encrypted message: ");
        Cm = encrypt(SubK, M, 4);
        Tools.printBits(Integer.parseInt(Cm, 2), 12);
        System.out.print("\n");
        System.out.print("Decrypted message: ");
        Tools.printBits(Integer.parseInt(decrypt(SubK, Cm, 4), 2), 12);

    }
}

