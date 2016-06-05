package SimplifiedDES;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by mattia on 03/06/16.
 */
public class Main {

    public static void main(String args[]) throws Exception {

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the 9 Bits Key :");
        String k = inp.readLine();
        while (k.length() != 9) {
            System.out.println("Key must be of 9 Bits :");
            k = inp.readLine();
        }

        System.out.println("Enter the 12 Bits message :");
        String m = inp.readLine();

        while (m.length() != 12) {
            System.out.println("Message must be of 12 Bits :");
            m = inp.readLine();
        }

        String[] parts = Tools.splitText(m, m.length() / 2);
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

        S_DES.generateKeys(k);

        S_DES.setCm(S_DES.encrypt(S_DES.getSubK(), m, 4));//encrypt

        Tools.printBits(Integer.parseInt(S_DES.getCm(), 2), 12);//encrypted message

        Tools.printBits(Integer.parseInt(S_DES.decrypt(S_DES.getSubK(), S_DES.getCm(), 4), 2), 12);//decrypt + decrypted message

    }
}
