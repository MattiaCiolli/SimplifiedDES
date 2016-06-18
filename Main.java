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

        S_DES.setK(Integer.parseInt(k, 2));

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

        S_DES.setSubK(S_DES.generateKeys(k));

        System.out.print("K: ");
        Tools.printBits(S_DES.getK(), 9);
        System.out.print("\n");

        System.out.print("K1: ");
        Tools.printBits(S_DES.getSubK(0), 8);
        System.out.print("\n");

        System.out.print("K2: ");
        Tools.printBits(S_DES.getSubK(1), 8);
        System.out.print("\n");

        System.out.print("K3: ");
        Tools.printBits(S_DES.getSubK(2), 8);
        System.out.print("\n");

        System.out.print("K4: ");
        Tools.printBits(S_DES.getSubK(3), 8);
        System.out.print("\n");

        S_DES.setCm(S_DES.encrypt(S_DES.getSubK(), m, 4));//encrypt

        System.out.println("\nEncrypted message : ");
        Tools.printBits(Integer.parseInt(S_DES.getCm(), 2), 12);//encrypted message

        System.out.println("\nDecrypted message : ");
        Tools.printBits(Integer.parseInt(S_DES.decrypt(S_DES.getSubK(), S_DES.getCm(), 4), 2), 12);//decrypt + decrypted message

        //Tools.printBits(Integer.parseInt(S_DES.decrypt(S_DES.getSubK(), "000111011011", 4), 2), 12);
        //Tools.printBits(Integer.parseInt(Attack.encryptForDC(S_DES.getSubK(), "000111011011", 3), 2), 12);
        System.out.println("\n"+S_DES.getM1());
        System.out.println("\nKey found : ");
        System.out.print(Attack.DifferentialCryptanalysys(S_DES.getM1()));

        System.out.println("\nKey found : ");
        System.out.print(Attack.BruteForce(S_DES.getCm(), m));
    }
}
