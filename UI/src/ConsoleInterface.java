import java.util.*;

import static java.lang.System.out;

public class ConsoleInterface {
    //for testing purposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager = new MachineManager();
    private final Scanner scanner = new Scanner(System.in);
    private boolean runMachine = true;

    private MachineInformation machineInformation;

    public static void main(String[] args) {
        ConsoleInterface game = new ConsoleInterface();
        game.runMachine();
    }

    private void runMachine() {
        int userChoice = 0;
        eMainMenuOption[] menuOptions = eMainMenuOption.values();

        printWelcomeMsg();
        while (userChoice != 8) {
            printMainMenu();
            try {
                userChoice = getValidInput();
                doMenuOption(menuOptions[userChoice - 1]);
            } catch (NumberFormatException e) {
                out.println("input is not a number , please try again");
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

        out.println("Bye Bye :)");
    }

    private int getValidInput() {
        int input = Integer.parseInt(getInput(""));

        while (input < 1 || input > 8) {
            input = Integer.parseInt(getInput("Please choose an option (1-8)"));
        }

        return input;
    }

    private void printMainMenu() {
        out.println(System.lineSeparator() + "Please choose an option (1-8) from the menu:");
        eMainMenuOption[] menuOptions = eMainMenuOption.values();
        for (int i = 0; i < menuOptions.length; i++) {
            out.println((i + 1) + " " + menuOptions[i]);
        }
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
            case ENDPROGRAM:
                this.exit();
        }
    }

    private void loadFromXML() {
        boolean loadedSuccessfully = false;
        //String filePath = getInput("Please enter full xml file path");
        out.println("Loading file...");
        try {
            machineManager.createMachineFromXML(PAPER_ENIGMA_XML_FILE_NAME);
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
        machineInformation=machineManager.getMachineInformation();
    }

    private void showMachineStructure() {

        out.println("Amount of total rotors: " + this.machineInformation.getAmountOfRotors());
        out.println("Amount of required rotors: " + this.machineInformation.getAmountOfRotorsRequired());
        out.println("Amount of available reflectors: " + this.machineInformation.getAvailableReflectors());
        out.println("Amount of message processed: " + this.machineManager.getAmountOfProcessedInputs());


        out.println("Initial Code configuration");
        out.println(this.machineManager.getInitialFullMachineCode());
        out.println("Current Code configuration");
        out.println(this.machineManager.getCurrentCodeSetting());

        getInput("To continue press enter");
    }

    private void autoZeroMachine() {
        out.println("Auto machine settings has been deployed");
        this.machineManager.autoZeroMachine();
        showMachineStructure();
    }

    private void getMachineInput() {
        boolean validInput;

        validInput = this.getRotors();
        if (validInput) {
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
        //String input="WOWCANTBELIEVEITACTUALLYWORKS";
        System.out.println("Machine known characters: "+ machineInformation.getAvailableChars());
        String input = this.getInput("Please enter a sentence for encryption");
        input = input.toUpperCase();
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
        this.machineManager.resetMachine();
        out.println("The machine is reset");
    }

    private void showHistoryAndStatistic() {
        out.println("History & Statistics:");
        out.println(this.machineManager.getStatistic());
    }

    private boolean getRotors() {
        boolean validInput = false;
        int amountOfIndexesNeeded = this.machineInformation.getAmountOfRotorsRequired();
        int index = 0;
        String currentRotorIndex="";
        out.println("Available rotors 1-"+machineInformation.getAmountOfRotors());
        String input = getInput("Please enter set of " + amountOfIndexesNeeded +
                " unique rotors index separated by a ','.\n");
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

                " starting index .\n"));

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
            input = getInput("input must be either 'Y' or 'N' ");
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

    private void printWelcomeMsg() {
        out.println("Welcome ! You're about to crack the Enigma :O");
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
