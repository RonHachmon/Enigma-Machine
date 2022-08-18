import jaxb_classes.CTEReflector;


import java.util.ArrayList;
import java.util.List;

public class Reflector {
    public static final int EMPTY = -1;
    public List<Integer> integerList = new ArrayList<>();

    public Reflector() {
        for (int i = 0; i < 3; i++) {
            integerList.add(i);
        }
        for (int i = 0; i < 3; i++) {
            integerList.add(i);
        }
    }

    public Reflector(int size) {
        for (int i = 0; i < size; i++) {
            integerList.add(EMPTY);
        }
    }

    public int getExitIndex(int index) {
        int value = integerList.get(index);
        //System.out.println("        Reflector value: "+value);
        for (int i = 0; i < integerList.size(); i++) {
            if (i != index) {
                if (value == integerList.get(i)) {
                    //System.out.println("        Reflector exit index: "+i);
                    return i;
                }
            }
        }
        return -1;
    }

    public static Reflector createReflectorFromXML(CTEReflector xmlReflector, int size) {
        Reflector reflector = new Reflector(size);

        xmlReflector.getCTEReflect().forEach(reflect -> {
            if (reflect.getInput() > size || reflect.getOutput() > size) {
                throw new IllegalArgumentException("Invalid reflector, input/output out of char collection bounds");
            }
            if (reflect.getInput() <= 0 || reflect.getOutput() <= 0) {
                throw new IllegalArgumentException("Invalid reflector, input/output out of char collection bounds");
            }
            if (reflector.integerList.set(reflect.getInput() - 1, reflect.getInput() - 1) != EMPTY) {
                throw new IllegalArgumentException("Invalid reflector, duplicate input/output slot");
            }
            if (reflector.integerList.set(reflect.getOutput() - 1, reflect.getInput() - 1) != EMPTY) {
                throw new IllegalArgumentException("Invalid reflector, duplicate input/output slot");
            }
        });
        if (reflector.emptyReflectorsInput()) {
            throw new IllegalArgumentException("Invalid reflector, reflector must contain a set of " + (reflector.integerList.size() / 2) +
                    " of input output");
        }
        return reflector;
    }

    private boolean emptyReflectorsInput() {
        return integerList.contains(EMPTY);
    }

    @Override
    public String toString() {
        String res = new String();
        for (int value : integerList) {
            res += value + ", ";
        }
        return res;
    }
}
