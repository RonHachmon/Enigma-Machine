
import javax.xml.bind.JAXBException;
import java.util.Scanner;

public class Main {
    public static final String XML_FILE_NAME = "test files/ex1-sanity-small.xml";

    public static void main(String[] args) throws JAXBException {
          hard_coded_sanity_check();
          //sanity_check_loaded_from_xml();

        System.out.println("Done");
    }

    //not done yet :)
    private static void sanity_check_loaded_from_xml() {
        Machine Enigma =new Machine();
        Enigma.loadMachineFromFile(XML_FILE_NAME);
        Scanner s =new Scanner(System.in);
//        //sending full string
        String input_string = s.nextLine();
        System.out.println("input char = "+input_string);
        System.out.println(Enigma.run_encrypt_on_string(input_string.toUpperCase()));

    }

    private static void hard_coded_sanity_check() {
        Machine Enigma=new Machine("ABCDEF");
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

        Enigma.Rotor_arr.add(rotor1);
        Enigma.Rotor_arr.add(rotor2);

        Scanner s =new Scanner(System.in);
//        //sending full string
        String input_string = s.nextLine();
        System.out.println("input char = "+input_string);
        System.out.println(Enigma.run_encrypt_on_string(input_string.toUpperCase()));


        //sending char by char
//        char temp='a';
//                while (temp!='p')
//        {
//            temp = s.next().charAt(0);
//            System.out.println("input char = "+temp);
//            Enigma.enter_char(temp);
//        }
    }
}