package SimplifiedDES;

import java.util.Collections;

/**
 * Created by mattia on 08/06/16.
 */
public class Attack {

    private static String M1$, M1$$;

    private static String[] C1,C2,C1$,C2$;

    public static Object[] getC1() {
        return C1;
    }

    public static void setC1(String c, int i) {
        Attack.C1[i] = c;
    }

    public static Object[] getC2() {
        return C2;
    }

    public static void setC2(String c, int i) {
        Attack.C2[i] = c;
    }


    public static String getM1$() {
        return M1$;
    }

    public static void setM1$(String m1$) {
        M1$ = m1$;
    }



    /*public static String BruteForce(String em, String mo){

        int[] k=new int[4];
        int sk=0;
        String cm=em;

        while(cm!=mo){
            System.out.print(Tools.adjustLength(Integer.toBinaryString(sk),9)+"\n");
            k=S_DES.generateKeys(Tools.adjustLength(Integer.toBinaryString(sk),9));

            cm = S_DES.decrypt(k, em, 4);
            sk++;
        }

            return Tools.adjustLength(Integer.toBinaryString(sk),9);

    }*/

    public static String DifferentialCryptanalysys(String m) {

        String[] parts = Tools.splitText(m, m.length() / 2);
        String mL1 = parts[0];
        String mR1 = parts[1];//takes the right part because R1*=R1

        M1$ = "100100" + mR1;//L1.R1.
        M1$$ = "111000" + mR1;//L1.R1.

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), m, 3), 6);// encrypt L1R1
        String l4 = parts[0];//L4
        String r4 = parts[1];//R4

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1$, 3), 6);// encrypt L1.R1.
        String l4$ = parts[0];//L4.
        String r4$ = parts[1];//R4.

        parts = Tools.splitText(encryptForDC(S_DES.getSubK(), M1$$, 3), 6);// encrypt L1..R1..
        String l4$$ = parts[0];//L4..
        String r4$$ = parts[1];//R4..

        //operations on first message
        String El4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4$), 2)), 8);//E(L4')=E(L4) XOR E(L4.)
        String r4Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4$, 2)), 6);//R4'=R4*R4.
        String l1Prime = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("100100", 2)), 6);//L1'
        String r4prl1pr = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Prime, 2) ^ Integer.parseInt(l1Prime, 2)), 6);//R4' XOR L1'

        //operations on second message
        String El4Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(Tools.expand(l4), 2) ^ Integer.parseInt(Tools.expand(l4$$), 2)), 8);//E(L4'')=E(L4) XOR E(L4..)
        String r4Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4, 2) ^ Integer.parseInt(r4$$, 2)), 6);//R4''=R4*R4..
        String l1Second = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(mL1, 2) ^ Integer.parseInt("111000", 2)), 6);//L1''
        String r4secl1sec = Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(r4Second, 2) ^ Integer.parseInt(l1Second, 2)), 6);//R4'' XOR L1''

        parts = Tools.splitText(El4Prime, El4Prime.length() / 2);//input XOR of E(L4')
        String xlIN = parts[0];//S1 input
        String xrIN = parts[1];//S2 input

        parts = Tools.splitText(r4prl1pr, r4prl1pr.length() / 2);//output XOR of R4' XOR L1'
        String xlOUT = parts[0];//S1 output
        String xrOUT = parts[1];//S2 output

        parts = Tools.splitText(El4Second, El4Second.length() / 2);//input XOR of E(L4'')
        String xlIN$ = parts[0];//S1 input
        String xrIN$ = parts[1];//S2 input

        parts = Tools.splitText(r4secl1sec, r4secl1sec.length() / 2);//output XOR of R4'' XOR L1''
        String xlOUT$ = parts[0];//S1 output
        String xrOUT$ = parts[1];//S2 output

        Object[] c1=findCouples(xlIN, xlOUT);
        Object[] c2=findCouples(xrIN, xrOUT);

        Object[] c1$=findCouples(xlIN$, xlOUT$);
        Object[] c2$=findCouples(xrIN$, xrOUT$);

        String[] tc1=(String[]) c1[0];
        String[] tc12=(String[]) c1[1];
        String[] tc2=(String[]) c2[0];
        String[] tc22=(String[]) c2[1];
        String[] tc1$=(String[]) c1$[0];
        String[] tc12$=(String[]) c1$[1];
        String[] tc2$=(String[]) c2$[0];
        String[] tc22$=(String[]) c2$[1];

        String firstBits=new String();
        String lastBits=new String();

        for(int i=0; i<tc1.length; i++)
        {
            System.out.print(tc1[i]+ " "+ tc12[i]+"\n");
        }

//System.out.print(C1[0]+" "+C1[1]+" "+C2[0]+" "+C2[1]+" "+C1$[0]+" "+C1$[1]+" "+C2$[0]+" "+C2$[1]);

        for(int i=0; i<tc1.length; i++)
        {
            for (int j=0; j<tc1$.length; j++)
            {
                if (tc1[i].equals(tc1$[j]))
                {
                    if (tc12[i].equals(tc12$[j]))
                    {
                        firstBits = tc1[i];
                    }

                } else {
                    if (tc12[i].equals(tc1$[j]))
                    {
                        if (tc1[i].equals(tc12$[j]))
                        {
                            firstBits = tc12[i];
                        }
                    }
                }
            }
        }


        for(int i=0; i<tc2.length; i++)
        {
            for (int j=0; j<tc2$.length; j++)
            {

                if (tc2[i].equals(tc2$[j])) {
                    if (tc22[i].equals(tc22$[j])) {
                        lastBits = tc1[i];
                    }

                } else {
                    if (tc22[i].equals(tc2$[j])) {
                        if (tc2[i].equals(tc22$[j])) {
                            lastBits = tc22[i];
                        }
                    }
                }
            }
        }




        return firstBits+lastBits;
    }


    public static Object[] findCouples(String s, String xorNeeded) {

        String[] couple1=new String[16];
        String[] couple2={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};
        String[] xor=new String[16];

        s=Tools.adjustLength(s, 4);

        for(int i=0; i<16; i++)
        {
            couple1[i]=Tools.adjustLength(Integer.toBinaryString((Integer.parseInt(s, 2)) ^ Integer.parseInt(couple2[i], 2)),4);

            //S1 coordinates (row first bit, column last 3 bits)
            String [] parts = Tools.splitText(couple1[i], 1); //halves string
            String S1row = parts[0];
            String S1col = parts[1];

            //S2 coordinates (row first bit, column last 3 bits)
            parts = Tools.splitText(couple2[i], 1); //halves string
            String S11row = parts[0];
            String S11col = parts[1];

            xor[i]= Tools.adjustLength(Integer.toBinaryString(Integer.parseInt(S_DES.getS1((Integer.parseInt(S1row, 2)), (Integer.parseInt(S1col, 2))), 2)^ Integer.parseInt(S_DES.getS1((Integer.parseInt(S11row, 2)), (Integer.parseInt(S11col, 2))), 2)), 3);//3 bits given by S1

        }

        String[] TCouple1=new String[couple1.length];
        String[] TCouple2=new String[couple1.length];
        //System.out.print("s   "+s);
        //System.out.print("xor    "+xorNeeded);
        int j=0;
        for(int i=0; i<couple1.length; i++)
        {//System.out.print("\n"+xor[i]);

            if(xor[i].equals(xorNeeded)){

                TCouple1[j]=couple1[i];
                //System.out.print("\n"+couple[0]);
                TCouple2[j]=couple2[i];

                j++;
            }

        }

        TCouple1=Tools.cleanString(TCouple1);
        TCouple2=Tools.cleanString(TCouple2);

        return new Object[]{TCouple1, TCouple2};

    }

    //SDES-encrypts the message in input with the key in input. Variable number of rounds
    public static String encryptForDC(int k[], String m, int r) {

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


        }

        return Integer.toBinaryString(cml) + mrt;
    }
}
