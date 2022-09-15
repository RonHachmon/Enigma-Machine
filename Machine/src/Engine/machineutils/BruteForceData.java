package Engine.machineutils;

import Engine.jaxb_classes.CTEDecipher;
import Engine.jaxb_classes.CTEDictionary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BruteForceData {
    private final Set<String> dictionary = new HashSet<>();
    private final int maxAmountOfCharacters;
    private String excludeChars;


    public BruteForceData(CTEDecipher cteDecipher) {

        if (cteDecipher.getAgents() >= 2 && cteDecipher.getAgents() <= 50) {
            maxAmountOfCharacters = cteDecipher.getAgents();
        } else {
            throw new IllegalArgumentException("amount of agent can only be between 2 to 50 ");
        }

        filterDictionary(cteDecipher.getCTEDictionary());

    }

    private void filterDictionary(CTEDictionary cteDictionary) {
        this.excludeChars = cteDictionary.getExcludeChars();
        String[] allWords = cteDictionary.getWords().split(" ");
        /*Arrays.stream(allWords).sequential().forEach(s -> System.out.println(s));*/
        Arrays.stream(cteDictionary.getWords().split(" ")).sequential().forEach(str -> {
                    dictionary.add(str.replaceAll("[" + excludeChars + "]", "").toUpperCase());
                });

    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public int getMaxAmountOfCharacters() {
        return maxAmountOfCharacters;
    }

    public String getExcludeChars() {
        return excludeChars;
    }
}

