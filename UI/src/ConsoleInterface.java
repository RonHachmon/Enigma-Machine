import java.nio.file.InvalidPathException;
import java.util.*;

public class ConsoleInterface {
    //for testing puposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager=new MachineManager();
    private final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String temp ="x&amp;";
        String replace = temp.replace("&amp;","Q");
        System.out.println(replace);

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
        //String filePath = getInput("Please enter full xml file path");
        System.out.println("Loading file ");
        try {
            machineManager.createMachineFromXML(PAPER_ENIGMA_XML_FILE_NAME);
            loadedSuccsefully=true;
            System.out.println("\nFile loaded succesfully");
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
        //this.showMachineStructure();
        this.paperEnigmaCheckLoadedFromXml();
    }
    private void showMachineStructure()
    {
        System.out.println("Amount of used Rotor:");

        System.out.println("Current Notch position on each rotor from right to left:");
//        machineManager.addSwitchPlug('A','F');
//        machineManager.setSelectedReflector(0);
//        machineManager.setSelectedRotors(new ArrayList<Integer>(){{add(0);add(1);}});
//        machineManager.setStartingIndex("CC");
        String notchPosition=machineManager.getNotchPosition();
        System.out.println(notchPosition);

        System.out.println("Amount of available reflectors:");
        System.out.println(this.machineManager.availableReflectors());

        System.out.println("Amount of message processed:");
        this.machineManager.getAmountOfProccesedInputs();

        System.out.println("Current Code configuration");
        System.out.println(this.machineManager.fullCodeSetting());

    }


    private void getMachineInput()
    {
        this.getRotors();
        this.getStartingIndexes();
        this.getReflector();
        this.getSwitchPlug();
    }
    private boolean getRotors() {
        boolean validInput=false;
        int amountOfIndexesNeeded=this.machineManager.amountOfRotors();
        int index = 0;
        String input=getInput("Please enter set of " + amountOfIndexesNeeded +
                                        " rotors index separated by a ','.\n");
        List<Integer> indexes = new ArrayList<>();
        try
        {
            for (String rotorIndex : input.split(",")) {
                //remove trailing whitespace just in case
                rotorIndex = rotorIndex.trim();
                index = Integer.parseInt(rotorIndex);
                indexes.add(index - 1);
            }

            Collections.reverse(indexes);
            this.machineManager.setSelectedRotors(indexes);
             validInput=true;
        }
        catch (NumberFormatException e)
        {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(!validInput)
        {
            if(tryAgain()) {
                this.getRotors();
            }
            else
            {
                validInput= false;
            }
        }


        return validInput;

    }





    private boolean getStartingIndexes() {
        boolean validInput=false;
        int amountOfIndexesNeeded=this.machineManager.amountOfRotors();
        StringBuilder input= new StringBuilder(getInput("Please enter set of " + amountOfIndexesNeeded +
                " starting index .\n"));
        try {
            input.reverse();
            this.machineManager.setStartingIndex(input.toString().toUpperCase(Locale.ROOT));
             validInput=true;
        }
        catch ( IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }

        if(!validInput)
        {
            if(tryAgain()) {
                this.getStartingIndexes();
            }
            else
            {
                validInput= false;
            }
        }


        return validInput;
    }
    private boolean getReflector() {
        boolean validInput=false;
        int amountOfAvailableReflectors=this.machineManager.availableReflectors();
        printAvailableReflectors(amountOfAvailableReflectors);
        String input=getInput("");
        try {
            int index = Integer.parseInt(input);
            if(index<1||index>amountOfAvailableReflectors)
            {
                System.out.println("index must be between 1 - "+amountOfAvailableReflectors);
            }
            else {
                this.machineManager.setSelectedReflector(index - 1);
                validInput=true;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(!validInput)
        {
            if(tryAgain()) {
                this.getReflector();
            }
            else
            {
                validInput= false;
            }
        }


        return validInput;
    }



    private boolean getSwitchPlug() {
        boolean validInput=false;
        String input=getInput("Please enter a pair of switch plugs without any separator between them\n," +
                "or enter to skip ");

        if(input.length()%2!=0)
        {
            System.out.println("invalid plugs, each character should be paired ");
        }

        input=input.toUpperCase();
        try {
            //send plugs by pair
            for (int i=0;i<input.length();i+=2)
            {
                this.machineManager.addSwitchPlug(input.charAt(i),input.charAt(i+1));

            }
            validInput=true;

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(!validInput)
        {
            if(tryAgain()) {
                this.getSwitchPlug();
            }
            else
            {
                validInput= false;
            }
        }


        return validInput;
    }




    //for testing purposes only
    private  void sanity_check_loaded_from_xml() {

        machineManager.addSwitchPlug('A','F');
        machineManager.setSelectedReflector(0);
        machineManager.setSelectedRotors(new ArrayList<Integer>(){{add(0);add(1);}});
        machineManager.setStartingIndex("CC");
        String input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CEEFBDFCDAAB");
    }
    //for testing purposes only
    private void paperEnigmaCheckLoadedFromXml() {
        //machineManager.setSelectedReflector(0);
        this.getRotors();
        this.getReflector();
        //machineManager.setSelectedRotors(new ArrayList<Integer>(){{add(2);add(1);add(0);}});
        this.getStartingIndexes();
        this.getSwitchPlug();
        this.showMachineStructure();
        //machineManager.getMachine().setStartingIndex("ODX");
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }

    //maybe with command pattern tryAgain will receive command to run
    private boolean tryAgain()
    {
        String input = getInput("try again Y/N ?");
        input = input.toUpperCase(Locale.ROOT);
        while (!input.equals("Y") && !input.equals("N"))
        {
            System.out.println(input);
             input = getInput("input must be either 'Y' or 'N' ");
        }

        return input.equals("Y");
    }
    private String getInput(String inputRequest) {
        System.out.println(inputRequest);
        return  scanner.nextLine();
    }

    private void printAvailableReflectors(int amountOfAvailableReflectors) {
        RomanNumbers[] romanValues = RomanNumbers.values();
        for (int i = 0; i < amountOfAvailableReflectors; i++) {
            System.out.println("for reflector "+ romanValues[i].name()+" "+ romanValues[i]);

        }
    }

}
