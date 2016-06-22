package SimplifiedDES;

/**
 * Created by mattia on 08/06/16.
 */
public class Attack {

    private static String M1S, M1SS, M1SSS;

    // performs a brute force attack to find the key starting from k=0
    public static String BruteForce(String encm, String origm) {

        int[] k = new int[4];
        int sk = 0;
        String cm = encm;
        int i = 1;

        // if the decrypted message not equals the original, increase the key of 1 e repeat
        while (!cm.equals(origm)) {

            sk++;
            i++;
            k = S_DES.generateKeys(Tools.adjustLength(Integer.toBinaryString(sk), 9));
            cm = S_DES.decrypt(k, encm, 4);

        }

        System.out.print("\nKey found by brute force in " + i + " iteractions: ");

        return Tools.adjustLength(Integer.toBinaryString(sk), 9);

    }

    public static String DifferentialCryptanalysis(String m) {

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1]; // takes the right part because R1*=R1

        M1S = "100100" + mR1; // L1.R1.
        M1SS = "111000" + mR1; // L1..R1..
        M1SSS = "000001" + mR1; // L1...R1...

        String cm = encryptForDC(S_DES.getSubK(), m, 3);
        parts = Tools.splitText(cm, 6); // encrypt L1R1
        String l4 = parts[0]; // L4
        String r4 = parts[1]; // R4

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1S, 3), 6); // encrypt L1.R1.
        String l4S = parts[0]; // L4.
        String r4S = parts[1]; // R4.

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1SS, 3), 6); // encrypt L1..R1..
        String l4SS = parts[0]; // L4..
        String r4SS = parts[1]; // R4..

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1SSS, 3), 6); // encrypt L1...R1...
        String l4SSS = parts[0]; // L4...
        String r4SSS = parts[1]; // R4...

        // operations on first message
        String El4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4S), 2)), 8); // E(L4')=E(L4) XOR E(L4.), XOR inputs
        String r4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4S, 2)), 6); // R4'=R4*R4.
        String l1Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("100100", 2)), 6); // L1'
        String r4prl1pr = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Prime, 2) ^ Integer.parseInt(l1Prime, 2)), 6); // R4' XOR L1', XOr of outputs

        // operations on second message
        String El4Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4SS), 2)), 8); // E(L4'')=E(L4) XOR E(L4..), XOR inputs
        String r4Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4SS, 2)), 6); // R4''=R4*R4..
        String l1Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("111000", 2)), 6); // L1''
        String r4secl1sec = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Second, 2) ^ Integer.parseInt(l1Second, 2)), 6); // R4'' XOR L1'', XOR of outputs

        // operations on third message
        String El4Third = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4SSS), 2)), 8); // E(L4''')=E(L4) XOR E(L4...), XOR inputs
        String r4Third = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4SSS, 2)), 6); // R4'''=R4*R4...
        String l1Third = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("000001", 2)), 6); // L1'''
        String r4thil1thi = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Third, 2) ^ Integer.parseInt(l1Third, 2)), 6); // R4''' XOR L1''', XOR of outputs


        parts = Tools.splitText(El4Prime, El4Prime.length() / 2); // input XOR of E(L4')
        String xlIN = parts[0]; // S1 input
        String xrIN = parts[1]; // S2 input

        parts = Tools.splitText(r4prl1pr, r4prl1pr.length() / 2); // output XOR of R4' XOR L1'
        String xlOUT = parts[0]; // S1 output
        String xrOUT = parts[1]; // S2 output

        parts = Tools.splitText(El4Second, El4Second.length() / 2); // input XOR of E(L4'')
        String xlINS = parts[0]; // S1 input second message
        String xrINS = parts[1]; // S2 input second message

        parts = Tools.splitText(r4secl1sec, r4secl1sec.length() / 2); // output XOR of R4'' XOR L1''
        String xlOUTS = parts[0]; // S1 output second message
        String xrOUTS = parts[1]; // S2 output second message

        parts = Tools.splitText(El4Third, El4Third.length() / 2); // input XOR of E(L4''')
        String xlINSS = parts[0]; // S1 input third message
        String xrINSS = parts[1]; // S2 input third message

        parts = Tools.splitText(r4thil1thi, r4thil1thi.length() / 2); // output XOR of R4''' XOR L1'''
        String xlOUTSS = parts[0]; // S1 output third message
        String xrOUTSS = parts[1]; // S2 output third message

        Object[] c1 = findCouples(xlIN, xlOUT, true); // couples with the output needed for first bits. Xor with Second message
        Object[] c2 = findCouples(xrIN, xrOUT, false); // couples with the output needed for last bits. Xor with Second message

        Object[] c1S = findCouples(xlINS, xlOUTS, true); // couples with the output needed for first bits. Xor with Third message
        Object[] c2S = findCouples(xrINS, xrOUTS, false); // couples with the output needed for last bits. Xor with Third message

        Object[] c1SS = findCouples(xlINSS, xlOUTSS, true); // couples with the output needed for first bits. Xor with fourth message
        Object[] c2SS = findCouples(xrINSS, xrOUTSS, false); // couples with the output needed for last bits. Xor with fourth message

        // couples left bits 1
        String[] tc1 = (String[]) c1[0];
        String[] tc12 = (String[]) c1[1];

        // couples right bits 1
        String[] tc2 = (String[]) c2[0];
        String[] tc22 = (String[]) c2[1];

        // couples left bits 2
        String[] tc1S = (String[]) c1S[0];
        String[] tc12S = (String[]) c1S[1];

        // couples right bits 2
        String[] tc2S = (String[]) c2S[0];
        String[] tc22S = (String[]) c2S[1];

        // couples left bits 3
        String[] tc1SS = (String[]) c1SS[0];
        String[] tc12SS = (String[]) c1SS[1];

        // couples right bits 3
        String[] tc2SS = (String[]) c2SS[0];
        String[] tc22SS = (String[]) c2SS[1];

        String[] firstBits1 = new String[16];
        String[] lastBits1 = new String[16];
        String[] firstBits2 = new String[16];
        String[] lastBits2 = new String[16];

        // for loops to check if there are some couples in common
        int k = 0;
        for (int i = 0; i < tc1.length; i++) {
            for (int j = 0; j < tc1S.length; j++) {

                if (tc1[i].equals(tc1S[j])) {

                    firstBits1[k] = tc1[i];
                    k++;
                }
            }
        }

        k = 0;
        for (int i = 0; i < tc2.length; i++) {
            for (int j = 0; j < tc2S.length; j++) {

                if (tc2[i].equals(tc2S[j])) {

                    lastBits1[k] = tc2[i];
                    k++;
                }
            }
        }

        k = 0;
        for (int i = 0; i < tc1.length; i++) {
            for (int j = 0; j < tc1SS.length; j++) {

                if (tc1[i].equals(tc1SS[j])) {

                    firstBits2[k] = tc1[i];
                    k++;
                }
            }
        }

        k = 0;
        for (int i = 0; i < tc2.length; i++) {
            for (int j = 0; j < tc2SS.length; j++) {

                if (tc2[i].equals(tc2SS[j])) {

                    lastBits2[k] = tc2[i];
                    k++;
                }
            }
        }

        firstBits1 = Tools.cleanString(firstBits1);
        firstBits2 = Tools.cleanString(firstBits2);
        lastBits1 = Tools.cleanString(lastBits1);
        lastBits2 = Tools.cleanString(lastBits2);

        String[] firsts = Tools.concatenate(firstBits1, firstBits2); // all first bits found
        String[] seconds = Tools.concatenate(lastBits1, lastBits2); // all last bits found

        String Korig = testKey(firsts, seconds, m, cm);

        System.out.print("\nKey found by DifferrentialCryptanalysis: ");

        return Korig;
    }


    // finds couples with input XOR "in" and output XOR "xorNeeded".
    // sbox=true-> S1, sbox=false-> S2
    public static Object[] findCouples(String in, String xorNeeded, boolean sbox) {

        String[] couple1 = new String[16];
        String[] couple2 = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"}; // fixed second element of the couple
        String[] xor = new String[16];

        in = Tools.adjustLength(in, 4);
        xorNeeded = Tools.adjustLength(xorNeeded, 3);

        for (int i = 0; i < 16; i++) {

            couple1[i] = Tools.adjustLength(Integer.toBinaryString((Integer.parseInt(in, 2)) ^ Integer.parseInt(couple2[i], 2)), 4); // first element of the couple= (in XOR couple2[i])

            // S1/S2 coordinates (row first bit, column last 3 bits)
            String[] parts = Tools.splitText(couple1[i], 1);  // splits string
            String S1row = parts[0];
            String S1col = parts[1];

            // second S1/S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(couple2[i], 1);  // splits string
            String S11row = parts[0];
            String S11col = parts[1];

            if (sbox == true) {

                // xor[i]=XOR of the outputs giving couple1 and couple 2 as coordinates
                xor[i] = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(S_DES.getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))), 2) ^ Integer.parseInt(S_DES.getS1((Integer.parseInt(S11row, 2)), (Integer.parseInt(S11col, 2))), 2)), 3); // 3 bits given by S1
            } else {
                if (sbox == false) {
                    xor[i] = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(S_DES.getS2((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))), 2) ^ Integer.parseInt(S_DES.getS2((Integer.parseInt(S11row, 2)), (Integer.parseInt(S11col, 2))), 2)), 3); // 3 bits given by S2
                }
            }

        }

        String[] TCouple1 = new String[16]; // first bits
        String[] TCouple2 = new String[16]; // last bits

        int j = 0;
        for (int i = 0; i < 16; i++) {

            // if xor=xorNeeded save the couple
            if (xor[i].equals(xorNeeded)) {

                TCouple1[j] = couple1[i];
                TCouple2[j] = couple2[i];
                j++;
            }

        }

        TCouple1 = Tools.cleanString(TCouple1); // remove null
        TCouple2 = Tools.cleanString(TCouple2); // remove null

        return new Object[]{TCouple1, TCouple2};

    }

    // SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encryptForDC(int k[], String m, int r) {

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];

        String mrt = mR1;
        int cml = Integer.parseInt(mL1, 2);

        for (int i = 1; i <= r; i++) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]); // right half expanded and XORed with the i-th key

            cmr = Tools.adjustLength(cmr, 8); // fixes eventually zeroes lost in conversion

            parts = Tools.splitText(cmr, cmr.length() / 2); // halves string

            // strings used to calculate the S-BOXES coordinates (left and right half of the expanded and XORed part)
            String XL = parts[0];
            String XR = parts[1];

            // S1 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XL, 1);  // halves string
            String S1row = parts[0];
            String S1col = parts[1];

            // S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XR, 1);  // halves string
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = S_DES.getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + S_DES.getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2))); // 6 bits given by the S-BOXES
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);

            cmr = Tools.adjustLength(cmr, 6); //  fixes eventually  zeroes lost in conversion
            cml = Integer.parseInt(mrt, 2); // swaps the left part with the right one
            mrt = cmr; // swaps the right part with the left one

        }

        return Tools.adjustLength(Integer.toBinaryString(cml) + mrt, 12);
    }

    //SDES-decrypts the cypher-text in input with the key in input. Variable number of rounds
    public static String decryptforDC(int k[], String m, int r) {

        m = Tools.adjustLength(m, 12); // fixes eventually  zeroes lost in conversion
        String[] parts = Tools.splitText(m, m.length() / 2); // halves the cyphertext in input
        String mR1 = parts[0]; // swaps right half with the left one (index 0->left part)
        String mL1 = parts[1]; // swaps the left part with the right one (index 1->right part)

        String mrt = mR1; // initialization
        int cml = Integer.parseInt(mL1, 2);

        for (int i = r - 1; i > 0; i--) {

            String cmr = Integer.toBinaryString(Integer.parseInt(Tools.expand(mrt), 2) ^ k[i]); // left half expanded and XORed with the r-i-th key

            cmr = Tools.adjustLength(cmr, 8); // fixes eventually  zeroes lost in conversion
            parts = Tools.splitText(cmr, cmr.length() / 2); // halves string

            //strings used to calculate the S-BOXES coordinates (left and right half of the expanded and XORed part)
            String XL = parts[0];
            String XR = parts[1];

            //S1 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XL, 1); // halves string
            String S1row = parts[0];
            String S1col = parts[1];

            //S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(XR, 1); // halves string
            String S2row = parts[0];
            String S2col = parts[1];

            String SResult = S_DES.getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))) + S_DES.getS2((Integer.parseInt(S2row, 2)), (Integer.parseInt(S2col, 2))); // 6 bits given by the S-BOXES
            cmr = Integer.toBinaryString(Integer.parseInt(SResult, 2) ^ cml);
            cmr = Tools.adjustLength(cmr, 6); // fixes eventually  zeroes lost in conversion
            cml = Integer.parseInt(mrt, 2); // swaps the left part with the right one
            mrt = cmr; // swaps the right part with the left one

        }

        return mrt + Tools.adjustLength(Integer.toBinaryString(cml), 6); // last swap performed here
    }

    //test every combination of the possible first-last bits found
    public static String testKey(String[] firsts, String[] seconds, String m, String cm) {

        String kp = new String();
        System.out.print("Possible keys:\n");
        for (int i = 0; i < firsts.length; i++) {
            for (int j = 0; j < seconds.length; j++) {
                String key4 = firsts[i] + seconds[j];
                String possibleK = "0" + key4.charAt(7) + key4.charAt(6) + key4.charAt(0) + key4.charAt(1) + key4.charAt(2) + key4.charAt(3) + key4.charAt(4) + key4.charAt(5); // back-shifts k4 to K and add a zero
                String possibleK2 = "1" + key4.charAt(7) + key4.charAt(6) + key4.charAt(0) + key4.charAt(1) + key4.charAt(2) + key4.charAt(3) + key4.charAt(4) + key4.charAt(5); // back-shifts k4 to K and add a 1
                System.out.print(possibleK + " " + possibleK2 + "\n"); // possible keys found
                S_DES.setSubK(S_DES.generateKeys(possibleK));
                String t1 = decryptforDC(S_DES.getSubK(), cm, 4); // tests the keys with the 0 added
                S_DES.setSubK(S_DES.generateKeys(possibleK2));
                String t2 = decryptforDC(S_DES.getSubK(), cm, 4); // tests the keys with the 1 added

                //if some of the decryptions with the found keys matches the original message, return the found key
                if (t1.equals(m)) {
                    kp = possibleK;
                } else if (t2.equals(m)) {
                    kp = possibleK2;
                }
            }
        }

        return kp;
    }
}