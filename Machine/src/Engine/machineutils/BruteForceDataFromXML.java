package Engine.machineutils;

import Engine.BruteForce.Dictionary;
import Engine.jaxb_classes.CTEDecipher;
import Engine.jaxb_classes.CTEDictionary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BruteForceDataFromXML {
    private final Dictionary dictionary=new Dictionary();
    /*private final Set<String> dictionary = new HashSet<>();*/
    private final int maxAmountOfAgent;
    private String excludeChars;


    public BruteForceDataFromXML(CTEDecipher cteDecipher) {

        if (cteDecipher.getAgents() >= 2 && cteDecipher.getAgents() <= 50) {
            maxAmountOfAgent = cteDecipher.getAgents();
        } else {
            throw new IllegalArgumentException("amount of agent can only be between 2 to 50 ");
        }

        filterDictionary(cteDecipher.getCTEDictionary());

    }

    private void filterDictionary(CTEDictionary cteDictionary) {
        dictionary.setDictionary(cteDictionary.getWords(),cteDictionary.getExcludeChars());

    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public int getMaxAmountOfAgent() {
        return maxAmountOfAgent;
    }

    public String getExcludeChars() {
        return excludeChars;
    }
}

