import java.io.ObjectOutputStream;
import java.util.*;

import static java.lang.System.out;

public class ConsoleInterface {
    //for testing purposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private final Scanner scanner = new Scanner(System.in);
    private static MachineManager machineManager = new MachineManager();
    private MachineInformation machineInformation;
    private boolean runMachine = true;

    public static void main(String[] args) {
        ConsoleInterface game = new ConsoleInterface();
        game.runMachine();
    }

    private void runMachine() {
        eMainMenuOption userChoice = null;

        printWelcomeMsg();
        while (userChoice != eMainMenuOption.ENDPROGRAM) {
            printMainMenu();
            try {
                userChoice = getValidInput();
                doMenuOption(userChoice);
            } catch (NumberFormatException e) {
                out.println("input is not a number, please try again");
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

        out.println("Bye Bye :)");
    }

    private eMainMenuOption getValidInput() {
        int input = Integer.parseInt(getInput(""));
        eMainMenuOption[] menuOptions = eMainMenuOption.values();

        while (input < 1 || input > menuOptions.length) {
            input = Integer.parseInt(getInput("Please choose an option (1-" + menuOptions.length +"):"));
        }

        return menuOptions[input - 1];
    }

    private void printMainMenu() {
        out.println(System.lineSeparator() + "Please choose an option from the menu (1-" + eMainMenuOption.values().length +"):");
        eMainMenuOption[] menuOptions = eMainMenuOption.values();

        for (int i = 0; i < menuOptions.length; i++) {
            out.println((i + 1) + " " + menuOptions[i]);
        }
    }

    private void doMenuOption(eMainMenuOption menuSelection) {
        if (menuSelection != eMainMenuOption.READFILE && menuSelection != eMainMenuOption.ENDPROGRAM
            && menuSelection != eMainMenuOption.LOADMACHINESTATE) {
            handleMachineDoesNotExist();
        }

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
                this.autoZeroMachine();
                break;
            case INPUTPROCESSING:
                this.processInput();
                break;
            case RESET:
                this.resetRotor();
                break;
            case HISTORYANDSTATS:
                this.showHistoryAndStatistic();
                break;
            case SAVEMACHINESTATE:
                this.saveMachineToFile();
                break;
            case LOADMACHINESTATE:
                this.loadMachineFromFile();
                break;
            case ENDPROGRAM:
                this.exit();
        }
    }

    private void loadFromXML() {
        boolean loadedSuccessfully = false;
        String filePath = getInput("Please enter full xml file path");
        out.println("Loading file...");
        try {
            machineManager.createMachineFromXML(filePath);
            loadedSuccessfully = true;
            out.println("File loaded successfully");
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        if (!loadedSuccessfully) {
            if (tryAgain()) {
                this.loadFromXML();
            }
        }
        if(loadedSuccessfully)
        {
            machineManager.reset();
            machineInformation = machineManager.getMachineInformation();

        }

    }

    private void showMachineStructure() {


        out.println("Amount of total rotors: " + this.machineInformation.getAmountOfRotors());
        out.println("Amount of required rotors: " + this.machineInformation.getAmountOfRotorsRequired());
        out.println("Amount of available reflectors: " + this.machineInformation.getAvailableReflectors());
        out.println("Amount of message processed: " + this.machineManager.getAmountOfProcessedInputs());
        handleMachineHasNotBeenSet();
        out.println("Initial Code configuration");
        out.println(this.machineManager.getInitialFullMachineCode());
        out.println("Current Code configuration");
        out.println(this.machineManager.getCurrentCodeSetting());

        getInput("To continue press enter");
    }

    private void autoZeroMachine() {
        this.machineManager.autoZeroMachine();
        out.println("Auto machine settings has been deployed");
        showMachineStructure();
    }

    private void getMachineInput() {
        boolean validInput = this.getRotors();

        if (validInput) {
            System.out.println("Machine known characters: "+ machineInformation.getAvailableChars());
            validInput = this.getStartingIndexes();
            if (validInput) {
                validInput = this.getReflector();
                if (validInput) {
                    validInput = this.getSwitchPlug();
                }
            }
        }
        if (validInput) {
            this.machineManager.commitChangesToMachine();
        }
    }

    private void processInput() {
        boolean validInput = false;

        handleMachineHasNotBeenSet();
        System.out.println("Machine known characters: "+ machineInformation.getAvailableChars());
        String input = this.getInput("Please enter a sentence for encryption").toUpperCase();
        out.println("Input: " + input);

        try {
            out.println("Output: " + this.machineManager.encryptSentence(input));
            validInput = true;
//            System.out.println("Expected Output");
//            System.out.println("CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.processInput();
            }
        }
    }

    private void resetRotor() {
        handleMachineHasNotBeenSet();
        this.machineManager.resetMachineCode();
        out.println("The machine is reset");
    }

    private void showHistoryAndStatistic() {
        handleMachineHasNotBeenSet();
        out.println("History & Statistics:");
        out.println(this.machineManager.getStatistic());
        getInput("To continue press enter");
    }

    public void saveMachineToFile() {
        handleMachineHasNotBeenSet();
        String msg = "Please enter the full path where you want to save the file including name of file\nFor example: C:\\Academic\\Java Rules\\NameOfMachine";
        String filePath = getInput(msg);

        try {
            machineManager.saveMachineToFile(filePath);
            out.println("Machine Saved");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void loadMachineFromFile() {
        String filePath = getInput("Please enter full path of the machine file\nFor example: C:\\Academic\\Java Rules\\NameOfMachine");
        try {
            machineManager.loadMachineFromFile(filePath);
            machineManager.setIsMachineExists(true);
            machineInformation = machineManager.getMachineInformation();
            out.println("Machine Loaded");
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private boolean getRotors() {
        boolean validInput = false;
        int amountOfIndexesNeeded = this.machineInformation.getAmountOfRotorsRequired();
        int index = 0;
        String currentRotorIndex="";
        out.println("Available rotors 1-"+machineInformation.getAmountOfRotors());
        String input = getInput("Please enter set of " + amountOfIndexesNeeded +
                " unique rotors index separated by a ','.\nFor example: 2,3,1");
        List<Integer> indexes = new ArrayList<>();
        try {
            for (String rotorIndex : input.split(",")) {
                //remove trailing whitespace just in case
                rotorIndex = rotorIndex.trim();
                currentRotorIndex=rotorIndex;
                index = Integer.parseInt(rotorIndex);
                indexes.add(index - 1);
            }

            Collections.reverse(indexes);
            this.machineManager.setSelectedRotors(indexes);
            validInput = true;
        } catch (NumberFormatException e) {
            out.println(currentRotorIndex+", is not a number");
        } catch (Exception e) {
            out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                validInput = this.getRotors();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    private boolean getStartingIndexes() {

        boolean validInput = false;
        int amountOfIndexesNeeded = this.machineInformation.getAmountOfRotorsRequired();
        StringBuilder input = new StringBuilder(getInput("Please enter set of " + amountOfIndexesNeeded +
                " starting characters\n"));

        try {
            input.reverse();
            this.machineManager.setStartingIndex(input.toString().toUpperCase());
            validInput = true;
        } catch (IllegalArgumentException e) {
            out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                validInput = this.getStartingIndexes();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    private boolean getReflector() {
        boolean validInput = false;
        int index=0;
        int amountOfAvailableReflectors = this.machineInformation.getAvailableReflectors();
        printAvailableReflectors();
        String input = getInput("");
        try {
             index = Integer.parseInt(input);
            if (index < 1 || index > amountOfAvailableReflectors) {
                out.println("index must be between 1 - " + amountOfAvailableReflectors);
            } else {
                this.machineManager.setSelectedReflector(index - 1);
                validInput = true;
            }
        } catch (NumberFormatException e) {
            out.println(index+", is not a number");
        } catch (Exception e) {
            out.println(e.getMessage());
        }


        if (!validInput) {
            if (tryAgain()) {
                validInput = this.getReflector();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    private boolean getSwitchPlug() {
        boolean validInput = false;
        String input = getInput("Enter a pair of switch plugs without any separator between them\n" +
                "Press enter to skip");

        if (input.length() % 2 != 0) {
            out.println("invalid plugs, each character should be paired ");
        } else {
            input = input.toUpperCase();
            try {
                this.machineManager.setSwitchPlug(input);
                validInput = true;
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

        if (!validInput) {
            if (tryAgain()) {
                validInput = this.getSwitchPlug();
            } else {
                validInput = false;
            }
        }

        return validInput;
    }

    //maybe with command pattern tryAgain will receive command to run
    private boolean tryAgain() {
        String input = getInput("try again Y/N ?");

        input = input.toUpperCase();
        while (!input.equals("Y") && !input.equals("N")) {
            out.println(input);
            input = getInput("input must be either 'Y' or 'N' ").toUpperCase();
        }

        return input.equals("Y");
    }

    private String getInput(String inputRequest) {
        out.println(inputRequest);
        return scanner.nextLine();
    }

    private void printAvailableReflectors() {
        RomanNumbers[] romanValues = RomanNumbers.values();
        int amountOfAvailableReflectors = this.machineInformation.getAvailableReflectors();
        for (int i = 0; i < amountOfAvailableReflectors; i++) {
            out.println("for reflector " + romanValues[i].name() + " " + romanValues[i]);
        }
    }

    private void handleMachineHasNotBeenSet() {

        //return machineManager.isMachineSettingInitialized();
        if (!machineManager.isMachineSettingInitialized()){
            throw new RuntimeException("Machine has not been initialized");
        }
    }

    private void handleMachineDoesNotExist() {
        if (!machineManager.isMachineExists()){
            throw new NullPointerException("Machine does not exist, make sure to load XML file");
        }
    }

    private void printWelcomeMsg() {
        out.println("Welcome ! You're about to crack the Enigma :O");
    }

    private void exit() {
        this.runMachine = false;
    }

    //for testing purposes only
    private void sanity_check_loaded_from_xml() {
        //machineManager.addSwitchPlug('A', 'F');
        machineManager.setSelectedReflector(0);
        machineManager.setSelectedRotors(new ArrayList<Integer>() {{
            add(0);
            add(1);
        }});
        machineManager.setStartingIndex("CC");
        String input_string = "AABBCCDDEEFF";
        out.println("input char = " + input_string);
        //System.out.println("Machine output  "+machineManager.getMachine().runEncryptOnString(input_string.toUpperCase()));
        out.println("Expected output " + "CEEFBDFCDAAB");
    }
    // for testing purposes only
    /*private void paperEnigmaCheckLoadedFromXml() {
        machineManager.setSelectedReflector(0);
=======
    //for testing purposes only
    private void paperEnigmaCheckLoadedFromXml() {
        //machineManager.setSelectedReflector(0);
>>>>>>> 314fe36 (changes to get input)
        this.getRotors();
        this.getReflector();
        machineManager.setSelectedRotors(new ArrayList<Integer>(){{add(2);add(1);add(0);}});
        this.getStartingIndexes();
        this.getSwitchPlug();
        this.showMachineStructure();
        machineManager.getMachine().setStartingIndex("ODX");
        String input_string="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("input char = "+input_string);
        System.out.println("Machine output  "+machineManager.getMachine().run_encrypt_on_string(input_string.toUpperCase()));
        System.out.println("Expected output "+"CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
    }*/
}
