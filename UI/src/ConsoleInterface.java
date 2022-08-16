import java.io.File;
import java.util.*;

public class ConsoleInterface {
    //for testing purposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager = new MachineManager();
    private final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
//        String temp = "x&amp;";
//        String replace = temp.replace("&amp;", "Q");
//        System.out.println(replace);


        String folderPath="C:\\Temp\\Error Files\\" ;//replace with your own folder path

        File f = new File(folderPath);
        // Populates the array with names of files and directories
        fileNames=f.list();
        assert fileNames != null;
        Arrays.sort(fileNames);

        int i=0;
        // For each pathname in the pathnames array
        for (String fileName : fileNames) {
            try{
                System.out.println((++i) +" # "+fileName+":");


                machineManager1.createMachineFromXML(folderPath+fileName);//enter your implement
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

        }








//        String temp ="x&amp;";
//        String replace = temp.replace("&amp;","Q");
//        System.out.println(replace);
//
//        ConsoleInterface game = new ConsoleInterface();
//        game.runMachine();
    }

    private void runMachine() {

        int userChoice = 0;

        do {
            printMainMenu();
            userChoice = getValidInput();
            handleUserChoice(userChoice);

        }while (userChoice != 8);

        System.out.println("Bye Bye :)");
    }

    private void handleUserChoice(int userChoice) {

    }

    private int getValidInput() {
        int input = 0;

        while (input < 1 || input > 8) {
            try {
                input = Integer.parseInt(this.getInput("Enter a number between 1-8"));
            }
            catch(Exception e){
                System.out.println(e.getMessage());

            }
        }

        return input;
    }
    private void printMainMenu() {
        System.out.println("Please choose an option from the menu:");
        eMainMenuOption[] menuOptions = eMainMenuOption.values();

        int var2 = menuOptions.length;
        for(int i=0; i<menuOptions.length;i++)
        {
            System.out.println(menuOptions[i]+ " press "+(i+1));
        }
        String input=getInput("");
        int index = Integer.parseInt(input);
        
        (menuOptions[index-1]);

    }
    private void doMenuOption(eMainMenuOption menuSelection) {
        switch (menuSelection) {
            case READFILE:
                this.loadFromXML();
                break;
            case MACHINESTRUCTURE:
                this.showMachineStructure();
                break;
            case ZEROINGMACHINE:
                this.getMachineInput();
                break;
            case AUTOZERO:
                //this.playMoveFromUser();
                break;
            case INPUTPROCESSING:
                this.processInput();
                break;
            case RESET:
                this.resetRotor();
                break;
            case HISTORYANDSTATS:
                //this.printStats();
                break;
            case ENDPROGRAM:
                this.exit();
        }

        System.out.println();

        for (int i = 0; i < menuOptions.length; i++) {
            System.out.println(i+1 + " " + menuOptions[i]);
        }

    }

    private void loadFromXML() {
        boolean loadedSuccessfully = false;
        //String filePath = getInput("Please enter full xml file path");
        System.out.println("Loading file ");
        try {
            machineManager.createMachineFromXML(PAPER_ENIGMA_XML_FILE_NAME);
            loadedSuccessfully = true;
            System.out.println("\nFile loaded successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!loadedSuccessfully) {
            if (tryAgain()) {
                this.loadFromXML();
            }
        }
        //this.showMachineStructure();
        //this.paperEnigmaCheckLoadedFromXml();
    }

    private void showMachineStructure()
    {
        System.out.println("Total amount of rotors: "+this.machineManager.amountOfRotors() );
        System.out.println("Required amount of rotors: "+this.machineManager.amountOfRotorsRequired() );


        System.out.println("Amount of available reflectors:");
        System.out.println(this.machineManager.availableReflectors());

        System.out.println("Amount of message processed:");
        this.machineManager.getAmountOfProccesedInputs();

        System.out.println("Initial Code configuration");
        System.out.println(this.machineManager.getInitialFullMachineCode());

        System.out.println("Current Code configuration");

        System.out.println(this.machineManager.getCurrentCodeSetting());

        getInput("To continue press enter");


    }

    private void getMachineInput() {
        this.getRotors();
        this.getStartingIndexes();
        this.getReflector();
        this.getSwitchPlug();
    }


    private void processInput()
    {
        boolean validInput =false;
        String input =this.getInput("Please enter a sentence for encryption");
        input=input.toUpperCase();
        System.out.println("intput: "+input);

        try {
            System.out.println("output: "+this.machineManager.encryptSentence(input));
            validInput= true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(!validInput)
        {
            if(tryAgain()) {
                this.processInput();
            }
        }


    }

    private void resetRotor()
    {
        this.machineManager.resetMachine();
    }


    private boolean getRotors() {
        boolean validInput = false;
        int amountOfIndexesNeeded = this.machineManager.amountOfRotors();
        int index = 0;
        String input = getInput("Please enter set of " + amountOfIndexesNeeded +
                " rotors index separated by a ','.\n");
        List<Integer> indexes = new ArrayList<>();
        try {
            for (String rotorIndex : input.split(",")) {
                //remove trailing whitespace just in case
                rotorIndex = rotorIndex.trim();
                index = Integer.parseInt(rotorIndex);
                indexes.add(index - 1);
            }

            Collections.reverse(indexes);
            this.machineManager.setSelectedRotors(indexes);
            validInput = true;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.getRotors();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    private boolean getStartingIndexes() {

        boolean validInput=false;
        int amountOfIndexesNeeded=this.machineManager.amountOfRotorsRequired();
        StringBuilder input= new StringBuilder(getInput("Please enter set of " + amountOfIndexesNeeded +

                " starting index .\n"));
        try {
            input.reverse();
            this.machineManager.setStartingIndex(input.toString().toUpperCase(Locale.ROOT));
            validInput = true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.getStartingIndexes();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    private boolean getReflector() {

        boolean validInput=false;
        int amountOfAvailableReflectors=this.machineManager.availableReflectors();
        printAvailableReflectors();
        String input=getInput("");
        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > amountOfAvailableReflectors) {
                System.out.println("index must be between 1 - " + amountOfAvailableReflectors);
            } else {
                this.machineManager.setSelectedReflector(index - 1);
                validInput = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.getReflector();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }


    private boolean getSwitchPlug() {
        boolean validInput = false;
        String input = getInput("Please enter a pair of switch plugs without any separator between them\n," +
                "or enter to skip ");

        if (input.length() % 2 != 0) {
            System.out.println("invalid plugs, each character should be paired ");
        }

        input = input.toUpperCase();
        try {
            //send plugs by pair
            for (int i = 0; i < input.length(); i += 2) {
                this.machineManager.addSwitchPlug(input.charAt(i), input.charAt(i + 1));
            }
            validInput = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.getSwitchPlug();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }


    //maybe with command pattern tryAgain will receive command to run
    private boolean tryAgain() {
        String input = getInput("try again Y/N ?");

        input = input.toUpperCase(Locale.ENGLISH);
        while (!input.equals("Y") && !input.equals("N"))
        {
            System.out.println(input);
            input = getInput("input must be either 'Y' or 'N' ");
        }

        return input.equals("Y");
    }

    private String getInput(String inputRequest) {
        System.out.println(inputRequest);
        return scanner.nextLine();
    }

    private void printAvailableReflectors() {
        RomanNumbers[] romanValues = RomanNumbers.values();
        int amountOfAvailableReflectors=this.machineManager.availableReflectors();
        for (int i = 0; i < amountOfAvailableReflectors; i++) {
            System.out.println("for reflector " + romanValues[i].name() + " " + romanValues[i]);
        }
    }


    private void exit()
    {
        this.runMachine=false;
    }

    //for testing purposes only
    private  void sanity_check_loaded_from_xml() {

        machineManager.addSwitchPlug('A','F');
        machineManager.setSelectedReflector(0);
        machineManager.setSelectedRotors(new ArrayList<Integer>(){{add(0);add(1);}});
        machineManager.setStartingIndex("CC");
        String input_string="AABBCCDDEEFF";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().runEncryptOnString(input_string.toUpperCase()));
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
//        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
//        System.out.println("input char = "+input_string);
//        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
//        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }

}
