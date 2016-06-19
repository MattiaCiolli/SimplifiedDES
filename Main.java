package SimplifiedDES;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by mattia on 03/06/16.
 */
public class Main {

    public static void main(String args[]) throws Exception {

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
        boolean b=true;
        while(b) {

            System.out.println("Select action :\n1)S-DES demo (encrypt, decrypt, brute force attack and differential cryptanalysis)\n2)Encrypt a message\n3)Decrypt a message\n4)Brute force attack\n5)Differential cryptanalysis\n\ninsert q to quit\n");

            switch (inp.readLine()) {
                case "1": {
                    System.out.println("\nS-DES demo\n");
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

                    System.out.println("\nKey found : ");
                    System.out.print(Attack.DifferentialCryptanalysys(S_DES.getM1()));

                    System.out.println("\nKey found : ");
                    System.out.print(Attack.BruteForce(S_DES.getCm(), m));
                    System.out.println("\n");

                    break;
                }

                case "2": {
                    System.out.println("\nEncrypt a message\n");
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
                    System.out.println("\n");

                    break;
                }

                case "3": {
                    System.out.println("\nDecrypt a message\n");
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

                    System.out.println("\nDecrypted message : ");
                    Tools.printBits(Integer.parseInt(S_DES.decrypt(S_DES.getSubK(), m, 4), 2), 12);//decrypt + decrypted message
                    System.out.println("\n");

                    break;
                }

                case "4": {
                    System.out.println("\nBrute force attack\n");
                    System.out.println("Enter the 12 Bits CypherText :");
                    String cm = inp.readLine();
                    while (cm.length() != 12) {
                        System.out.println("CypherText must be of 12 Bits :");
                        cm = inp.readLine();
                    }

                    System.out.println("Enter the 12 Bits original message :");
                    String m = inp.readLine();

                    while (m.length() != 12) {
                        System.out.println("Original message must be of 12 Bits :");
                        m = inp.readLine();
                    }

                    System.out.println("\nKey found : ");
                    System.out.print(Attack.BruteForce(cm, m));
                    System.out.println("\n");

                    break;
                }

                case "5": {
                    System.out.println("\nDifferential CryptAnalysis\n");
                    System.out.println("Enter the 12 Bits first round CypherText:");
                    String cm = inp.readLine();
                    while (cm.length() != 12) {
                        System.out.println("CypherText must be of 12 Bits :");
                        cm = inp.readLine();
                    }

                    System.out.println("Enter the 9 Bits Key of the machine:");
                    String k = inp.readLine();
                    while (k.length() != 9) {
                        System.out.println("Key must be of 9 Bits :");
                        k = inp.readLine();
                    }

                    S_DES.encrypt(S_DES.getSubK(), cm, 4);
                    System.out.println("\nKey found : ");
                    System.out.print(Attack.DifferentialCryptanalysys(S_DES.getM1()));
                    System.out.println("\n");

                    break;
                }

                case "q":
                    b=false;
                    break;

            }
        }
System.out.print("end");
    }
}
