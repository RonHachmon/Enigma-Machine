package Engine.machineutils;


import Engine.bruteForce2.utils.Dictionary;
import Engine.jaxb_classes.CTEDecipher;
import Engine.jaxb_classes.CTEDictionary;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BruteForceDataFromXML implements Serializable {

    private final int maxAmountOfAgent;


    public BruteForceDataFromXML(CTEDecipher cteDecipher) {

        if (cteDecipher.getAgents() >= 2 && cteDecipher.getAgents() <= 50) {
            maxAmountOfAgent = cteDecipher.getAgents();
        } else {
            throw new IllegalArgumentException("amount of agent can only be between 2 to 50 ");
        }
    }

    public int getMaxAmountOfAgent() {
        return maxAmountOfAgent;
    }
}

