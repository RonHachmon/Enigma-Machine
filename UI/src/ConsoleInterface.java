public class ConsoleInterface {
    private MachineManager machineManager;
    public static void main(String[] args) {
        System.out.println("hey");
        ConsoleInterface game = new ConsoleInterface();
        game.runMachine();
    }

    private void runMachine() {

            this.printMainMenu();
        }

    private void printMainMenu() {
        System.out.println("Please choose an option from the menu:");
        eMainMenuOption[] var1 = eMainMenuOption.values();
        int var2 = var1.length;

    }
}
