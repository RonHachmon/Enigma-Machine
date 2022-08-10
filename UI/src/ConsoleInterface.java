import java.nio.file.InvalidPathException;
import java.util.Scanner;

public class ConsoleInterface {
    //for testing puposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager=new MachineManager();
    private final Scanner scanner = new Scanner(System.in);
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
        boolean loadedSuccsefully=false;
        String filePath = getInput("Please enter full xml file path");
        System.out.println("Loading file ");
        try {
            machineManager.createMachineFromXML(SANITY_SMALL_XML_FILE_NAME);
            loadedSuccsefully=true;
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

        if(!loadedSuccsefully)
        {
            if(tryAgain()) {
                this.loadFromXML();
            }
        }
        this.paperEnigmaCheckLoadedFromXml();
    }
    private void showMachineStructure()
    {

    }



    //for testing purposes only
    private  void sanity_check_loaded_from_xml() {

        machineManager.getMachine().add_switch_plug('A','F');
        machineManager.getMachine().setSelectedReflector(0);
        machineManager.getMachine().setSelectedRotors(0,1);
        machineManager.getMachine().setStartingIndex(new String[]{"C", "C"});
        String input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CEEFBDFCDAAB");
    }
    //for testing purposes only
    private void paperEnigmaCheckLoadedFromXml() {
        machineManager.getMachine().setSelectedReflector(0);
        machineManager.getMachine().setSelectedRotors(2,1,0);
        machineManager.getMachine().setStartingIndex(new String[]{"X", "D","O"});
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }

    //maybe with command pattern tryAgain will receive command to run
    private boolean tryAgain()
    {
        String input = getInput("try again y/n ?");
        while (input!="y" && input!="n")
        {
             input = getInput("input must be either 'y' or 'n' ");
        }

        return input=="y";
    }
    private String getInput(String inputRequest) {
        System.out.println(inputRequest);
        return  scanner.nextLine();
    }

}
