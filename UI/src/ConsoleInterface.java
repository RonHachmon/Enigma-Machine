import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Scanner;

public class ConsoleInterface {
    //for testing puposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager=new MachineManager();
    public static void main(String[] args) {
        System.out.println("hey");
        ConsoleInterface game = new ConsoleInterface();
        game.runMachine();
    }

    private void runMachine() {
            this.loadFromXML();
            //this.printMainMenu();
        }

    private void printMainMenu() {
        System.out.println("Please choose an option from the menu:");
        eMainMenuOption[] var1 = eMainMenuOption.values();
        int var2 = var1.length;
        for(int i=0; i<var1.length;i++)
        {
            System.out.println(var1[i]);
        }

    }
    private void loadFromXML(){
        System.out.println("Please enter full xml file path");
        LoadingScreen loadingScreen = new LoadingScreen("Loading file");
        System.out.println("Loading file..");
        //Scanner scanner = new Scanner(System.in);
        //String filePath = scanner.nextLine();
        //System.out.println(filePath);

        System.out.println("Loading file ");
        //loadingScreen.start();

        try {
            machineManager.createMachineFromXML(PAPER_ENIGMA_XML_FILE_NAME);
            System.out.println("\nFile loaded succesfully");
        }
        catch (InvalidPathException e) {
            System.out.println(e.getMessage());
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally {
            //loadingScreen.stop();
        }

//        //this.paperEnigmaCheckLoadedFromXml();

    }

    //for testing purposes only
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
    //for testing purposes only
    private void paperEnigmaCheckLoadedFromXml() {
        Machine Enigma =new Machine();
        //Enigma.loadMachineFromFile(PAPER_ENIGMA_XML_FILE_NAME);
        machineManager.getMachine().setSelectedReflector(0);
        machineManager.getMachine().setSelectedReflector(0);
        machineManager.getMachine().setSelected_rotors(2,1,0);
        machineManager.getMachine().setStartingIndex();
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }

}
