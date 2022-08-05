
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    public static void main(String[] args) throws JAXBException {
          //hard_coded_sanity_check();
          //sanity_check_loaded_from_xml();
        paper_enigma_check_loaded_from_xml();

        System.out.println("Done");
    }

    //wont work for now since method set_starting_index should be implemented
    private static void sanity_check_loaded_from_xml() {
        Machine Enigma =new Machine();
        Enigma.loadMachineFromFile(SANITY_SMALL_XML_FILE_NAME);
        Enigma.add_switch_plug('A','F');
        Enigma.setSelected_reflector(0);
        Enigma.setSelected_rotors(0,1);
        Enigma.set_starting_index();
        String input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+Enigma.run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CEEFBDFCDAAB");
    }
    private static void paper_enigma_check_loaded_from_xml() {
        Machine Enigma =new Machine();
        Enigma.loadMachineFromFile(PAPER_ENIGMA_XML_FILE_NAME);
        Enigma.setSelected_reflector(0);
        Enigma.setSelected_rotors(2,1,0);
        Enigma.set_starting_index();
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+Enigma.run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
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
        rotor1.line_array.add(one0);
        rotor1.line_array.add(one1);
        rotor1.line_array.add(one2);
        rotor1.line_array.add(one3);
        rotor1.line_array.add(one4);
        rotor1.line_array.add(one5);

        rotor2.line_array.add(one0_1);
        rotor2.line_array.add(one1_1);
        rotor2.line_array.add(one2_2);
        rotor2.line_array.add(one3_3);
        rotor2.line_array.add(one4_4);
        rotor2.line_array.add(one5_5);

        Enigma.selected_rotors.add(rotor1);
        Enigma.selected_rotors.add(rotor2);
        Enigma.add_switch_plug('A','F');
        String input_string;
//        input though keyboard
//        Scanner s =new Scanner(System.in);
//       sending full string
//        String input_string = s.nextLine();

        input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+Enigma.run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CEEFBDFCDAAB");


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
