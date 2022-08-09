
import javax.xml.bind.JAXBException;

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
        //Enigma.loadMachineFromFile(SANITY_SMALL_XML_FILE_NAME);
        Enigma.add_switch_plug('A','F');
        Enigma.setSelectedReflector(0);
        Enigma.setSelected_rotors(0,1);
        Enigma.setStartingIndex();
        String input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+Enigma.run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CEEFBDFCDAAB");
    }
    private static void paper_enigma_check_loaded_from_xml() {
        Machine Enigma =new Machine();
        //Enigma.loadMachineFromFile(PAPER_ENIGMA_XML_FILE_NAME);
        Enigma.setSelectedReflector(0);
        Enigma.setSelected_rotors(2,1,0);
        Enigma.setStartingIndex();
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+Enigma.run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }

}
