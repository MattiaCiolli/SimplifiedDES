package SimplifiedDES;

/**
 * Created by mattia on 08/06/16.
 */
public class Attack {

    private static String M1, M1$, E1, E1$;

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


    /*public static String BruteForce(String m, String mo){

        int[] k=new int[512];;
        int i=0;

        while(m!=mo){

            k=S_DES.generateKeys();

            String cm = S_DES.decrypt(k, mo, 4);
            i++;
        }
    }*/

    public static String findKey(int k[]) {
        String[] parts = Tools.splitText(M1, M1.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];//takes the right part because R1*=R1

        M1$ = "100100" + mR1;//L1.R1.

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1, 3), 6);// encrypt L1R1
        String l4 = parts[0];//L4
        String r4 = parts[1];//R4

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1$, 3), 6);// encrypt L1.R1.
        String l4$ = parts[0];//L4.
        String r4$ = parts[1];//R4.

        String El4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4$), 2)), 8);//E(L4')=E(L4) XOR E(L4.)
        String r4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4$, 2)), 6);//R4'=R4*R4.
        String l1Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("100100", 2)), 6);//L1'
        String r4prl1pr = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Prime, 2) ^ Integer.parseInt(l1Prime, 2)), 6);//R4' XOR L1'

        parts = Tools.splitText(El4Prime, El4Prime.length() / 2);//input XOR of E(L4')
        String xlIN = parts[0];//S1 input
        String xrIN = parts[1];//S2 input

        parts = Tools.splitText(r4prl1pr, r4prl1pr.length() / 2);//output XOR of R4' XOR L1'
        String xlOUT = parts[0];//S1 output
        String xrOUT = parts[1];//S2 output


        return "developing";
    }

    //SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encryptForDC(int k[], String m, int r) {

        System.out.print("\nEncryptionDC: \n");

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = 1; i <= r; i++) {


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

            System.out.print("Round " + (i) + ": ");
            Tools.printBits(Integer.parseInt(Integer.toBinaryString(cml) + mrt, 2), 12);
            System.out.print("\n");

        }

        System.out.println("\nEncrypted message DC : ");
        return Integer.toBinaryString(cml) + mrt;
    }
}
