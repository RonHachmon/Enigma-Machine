import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Line one0 =new Line('C','D');
        Line one1 =new Line('D','C');
        Line one2 =new Line('E','B');
        Line one3 =new Line('F','A');
        Line one4 =new Line('A','F');
        Line one5 =new Line('B','E');

        Line one0_1 =new Line('C','D');
        Line one1_1 =new Line('D','F');
        Line one2_2 =new Line('E','C');
        Line one3_3 =new Line('F','A');
        Line one4_4 =new Line('A','E');
        Line one5_5 =new Line('B','B');

        Rotor rotor1 =new Rotor();
        Rotor rotor2 =new Rotor();
        rotor1.notch_index=1;
        rotor2.notch_index=4;
        rotor1.Line_arr.add(one0);
        rotor1.Line_arr.add(one1);
        rotor1.Line_arr.add(one2);
        rotor1.Line_arr.add(one3);
        rotor1.Line_arr.add(one4);
        rotor1.Line_arr.add(one5);

        rotor2.Line_arr.add(one0_1);
        rotor2.Line_arr.add(one1_1);
        rotor2.Line_arr.add(one2_2);
        rotor2.Line_arr.add(one3_3);
        rotor2.Line_arr.add(one4_4);
        rotor2.Line_arr.add(one5_5);


        Machine Enigma =new Machine();
        Enigma.Rotor_arr.add(rotor1);
        Enigma.Rotor_arr.add(rotor2);
        Scanner s =new Scanner(System.in);
        char temp='0';
        String temp2 = s.nextLine();
        System.out.println("input char = "+temp2);
        System.out.println(Enigma.enter_string(temp2));


//        while (temp!='p')
//        {
//            temp = s.next().charAt(0);
//            System.out.println("input char = "+temp);
//            Enigma.enter_char(temp);
//            // Print the read value
//
//        }
        System.out.println("Done");


    }
}
