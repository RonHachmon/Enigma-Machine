package Engine.enigma.machineutils;


import Engine.enigma.jaxb_classes.CTEDecipher;

import java.io.Serializable;

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

