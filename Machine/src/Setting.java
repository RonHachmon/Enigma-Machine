import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Setting {
    public static final String EMPTY = "";
    private String setOfPlugs = EMPTY;
    private List<Integer> selectedRotorsIndexes = new ArrayList<>();
    private String chosenReflector;
    private String initialRotorIndexes = EMPTY;
    private String initialRotorDistanceFromNotch = EMPTY;

    public String getCurrentMachineCode(Machine machine) {
        String code = "<" + RotorsAndDistanceFromNotch(machine) + ">" +
                "<" + currentRotorsIndexes(machine) + ">" +
                "<" + chosenReflector + ">" +
                getAddPlugs();

        return code;
    }

    public String getInitialFullMachineCode() {
        String code = "<" + initialRotorDistanceFromNotch + ">" +
                "<" + initialRotorIndexes + ">" +
                "<" + chosenReflector + ">" +
                getAddPlugs();

        return code;
    }

    public void resetPlugs()
    {
        setOfPlugs=EMPTY;
    }

    public String getInitialRotorIndexes() {
        return this.initialRotorIndexes;
    }

    public void setInitialRotorsAndDistanceFromNotch(Machine machine) {
        initialRotorDistanceFromNotch = this.RotorsAndDistanceFromNotch(machine);
    }

    public void setSettingRotators(List<Integer> RotorsID) {
        selectedRotorsIndexes = RotorsID;
    }

    //gets full set of characters for example "AO!"
    public void setSettingStartingChar(String startingCharArray) {
        StringBuilder string = new StringBuilder(startingCharArray);
        initialRotorIndexes = string.reverse().toString();
    }

    public void setSettingReflector(int selectedReflector) {
        this.chosenReflector = Setting.convertIntToRoman(selectedReflector);
    }

    public void addPlug(Character firstChar, Character secondChar) {
        if (this.setOfPlugs.isEmpty()) {
            this.setOfPlugs += firstChar.toString() + '|' + secondChar.toString();
        } else {
            this.setOfPlugs += ',' + firstChar.toString() + '|' + secondChar.toString();
        }
    }

    private String currentRotorsIndexes(Machine machine) {
        String result = EMPTY;
        for (int i = selectedRotorsIndexes.size() - 1; i >= 0; i--) {
            int currentRotorIndex = selectedRotorsIndexes.get(i);
            result += machine.getAllRotors().get(currentRotorIndex).currentStartingChar();
        }
        return result;
    }

    private String RotorsAndDistanceFromNotch(Machine machine) {
        String result = EMPTY;
        for (int i = selectedRotorsIndexes.size() - 1; i >= 0; i--) {
            int currentRotorIndex = selectedRotorsIndexes.get(i);
            result += (currentRotorIndex + 1) +
                    "(" + machine.getAllRotors().get(currentRotorIndex).distanceFromNotch() + ")";
        }
        return result;
    }

    private String getAddPlugs() {
        if (setOfPlugs.isEmpty()) {
            return EMPTY;
        }
        return "<" + setOfPlugs + ">";
    }

    private static String convertIntToRoman(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }
    }
}
