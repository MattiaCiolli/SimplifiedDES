package SimplifiedDES;

/**
 * Created by mattia on 08/06/16.
 */
public class Attack {

    private static String M1, M1$;
    private static String[] E1;
    private static String[] E1$;

    public static String getM1$() {
        return M1$;
    }

    public static void setM1$(String m1$) {
        M1$ = m1$;
    }

    public static String getM1() {
        return M1;
    }

    public static void setM1(String m1) {
        M1 = m1;
    }

    public static String findKey(int k[])
    {
        String[] parts = Tools.splitText(M1, M1.length() / 2);
        String mR1 = parts[1];

        M1$="100100"+mR1;

        encryptForDC(S_DES.getSubK(), M1, 3, true);
        encryptForDC(S_DES.getSubK(), M1$, 3, false);

    }

    //SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encryptForDC(int k[], String m, int r, boolean b) {

        System.out.print("\nEncryptionDC: \n");

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = 1; i <= r; i++) {

if(b==true)
{
    E1[i-1]=Tools.expand(mrt);
}else {
    if (b == false) {
        E1$[i-1]=Tools.expand(mrt);
    }
}

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]);//right half expanded and XORed with the i-th key

            cmr = Tools.adjustLength(cmr, 8);// fixes eventually  zeroes lost in conversion

            parts = Tools.splitText(cmr, cmr.length() / 2); //halves string

            //strings used to calculate the S-BOXES coordinates (left and right half of the expanded and XORed part)
            String XL = parts[0];
            String XR = parts[1];

            //S1 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XL, 1); //halves string
            String S1row = parts[0];
            String S1col = parts[1];

            //S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XR, 1); //halves string
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = S_DES.getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + S_DES.getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2)));//6 bits given by the S-BOXES
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);

            cmr = Tools.adjustLength(cmr, 6);// fixes eventually  zeroes lost in conversion
            cml = Integer.parseInt(mrt, 2);//swaps the left part with the right one
            mrt = cmr;//swaps the right part with the left one

            System.out.print("Round "+(i)+": ");
            Tools.printBits(Integer.parseInt(Integer.toBinaryString(cml) + mrt, 2), 12);
            System.out.print("\n");

        }

        System.out.println("\nEncrypted message DC : ");
        return Integer.toBinaryString(cml) + mrt;
    }
}
