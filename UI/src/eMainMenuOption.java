
public enum eMainMenuOption {
    READFILE("Read file"),
    MACHINESTRUCTURE("Show machine structure"),
    ZEROINGMACHINE("Set machine"),
    AUTOZERO("Set machine automatically"),
    INPUTPROCESSING("Process input"),
    RESET("Reset machine"),
    HISTORYANDSTATS("Show machine statistics"),
    SAVEMACHINESTATE("Save machine to file"),
    LOADMACHINESTATE("Load machine from file"),
    ENDPROGRAM("End program");

    public static final int SIZE = values().length;
    //public static final int PIVOT = 2;
    private String message;

    private eMainMenuOption(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}


