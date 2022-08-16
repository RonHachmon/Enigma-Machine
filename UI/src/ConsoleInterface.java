import java.util.*;

public class ConsoleInterface {
    //for testing purposes only
    public static final String SANITY_SMALL_XML_FILE_NAME = "test files/ex1-sanity-small.xml";
    public static final String PAPER_ENIGMA_XML_FILE_NAME = "test files/ex1-sanity-paper-enigma.xml";

    private MachineManager machineManager = new MachineManager();
    private final Scanner scanner = new Scanner(System.in);
    private boolean runMachine = true;

    public static void main(String[] args) {
        ConsoleInterface game = new ConsoleInterface();
        game.runMachine();
    }

    private void runMachine() {
        int userChoice = 0;
        eMainMenuOption[] menuOptions = eMainMenuOption.values();

        do {
            userChoice = getValidInput();
            doMenuOption(menuOptions[userChoice - 1]);
        } while (userChoice != 8);

        System.out.println("Bye Bye :)");
    }

    private int getValidInput() {
        int input = 0;

        while (input < 1 || input > 8) {
            try {
                printMainMenu();

                input = Integer.parseInt(getInput(""));
                //input = Integer.parseInt(this.getInput("Enter a number between 1-8"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return input;
    }

    private void printMainMenu() {
        System.out.println("Please choose an option (1-8) from the menu:");
        eMainMenuOption[] menuOptions = eMainMenuOption.values();
        for (int i = 0; i < menuOptions.length; i++) {
            System.out.println((i + 1) + " " + menuOptions[i]);
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
    }

    private void showMachineStructure() {
        System.out.println("Amount of total rotors: " + this.machineManager.amountOfRotors());
        System.out.println("Amount of required rotors: " + this.machineManager.amountOfRotorsRequired());
        System.out.println("Amount of available reflectors: " + this.machineManager.availableReflectors());
        System.out.println("Amount of message processed: " + this.machineManager.getAmountOfProcessedInputs());

        System.out.println("Initial Code configuration");
        System.out.println(this.machineManager.getInitialFullMachineCode());
        System.out.println("Current Code configuration");
        System.out.println(this.machineManager.getCurrentCodeSetting());

        getInput("To continue press enter");
    }

    private void autoZeroMachine() {
        this.machineManager.autoZeroMachine();
        showMachineStructure();
    }

    private void getMachineInput() {
        this.getRotors();
        this.getStartingIndexes();
        this.getReflector();
        this.getSwitchPlug();
        this.machineManager.addCodeToStatistic();
    }

    private void processInput() {
        boolean validInput = false;
        //String input="WOWCANTBELIEVEITACTUALLYWORKS";
        String input = this.getInput("Please enter a sentence for encryption");
        input = input.toUpperCase();
        System.out.println("input: " + input);

        try {
            System.out.println("Output");
            System.out.println(this.machineManager.encryptSentence(input));
            validInput = true;
//            System.out.println("Expected Output");
//            System.out.println("CVRDIZWDAWQKUKBVHJILPKRNDXWIY");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!validInput) {
            if (tryAgain()) {
                this.processInput();
            }
        }
    }

    private void resetRotor() {
        this.machineManager.resetMachine();
    }

    private void showHistoryAndStatistic() {
        System.out.println(this.machineManager.getStatistic());
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

        boolean validInput = false;
        int amountOfIndexesNeeded = this.machineManager.amountOfRotorsRequired();
        StringBuilder input = new StringBuilder(getInput("Please enter set of " + amountOfIndexesNeeded +

                " starting index .\n"));
        try {
            input.reverse();
            this.machineManager.setStartingIndex(input.toString().toUpperCase());
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

        boolean validInput = false;
        int amountOfAvailableReflectors = this.machineManager.availableReflectors();
        printAvailableReflectors();
        String input = getInput("");
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
        while (!input.equals("Y") && !input.equals("N")) {
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
        int amountOfAvailableReflectors = this.machineManager.availableReflectors();
        for (int i = 0; i < amountOfAvailableReflectors; i++) {
            System.out.println("for reflector " + romanValues[i].name() + " " + romanValues[i]);
        }
    }

    private void exit() {
        this.runMachine = false;
    }

    //for testing purposes only
    private void sanity_check_loaded_from_xml() {

        machineManager.addSwitchPlug('A', 'F');
        machineManager.setSelectedReflector(0);
        machineManager.setSelectedRotors(new ArrayList<Integer>() {{
            add(0);
            add(1);
        }});
        machineManager.setStartingIndex("CC");
        String input_string = "AABBCCDDEEFF";
        System.out.println("input char = " + input_string);
        //System.out.println("Machine output  "+machineManager.getMachine().runEncryptOnString(input_string.toUpperCase()));
        System.out.println("Expected output " + "CEEFBDFCDAAB");
    }

    // for testing purposes only
    /*private void paperEnigmaCheckLoadedFromXml() {
        machineManager.setSelectedReflector(0);
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
