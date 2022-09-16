package Engine.bruteForce2.utils;

import Engine.EnigmaException.WordNotValidInDictionaryException;

import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
    private final static Set<String> words=new HashSet<>();
    private final static Set<Character> excludeChars = new HashSet<>();


    public void setDictionary(String words, String excludeChars){
        this.excludeChars.addAll(excludeChars.toUpperCase().chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));

        cleanDictionaryFromExcludeChars(Arrays.asList(words.toUpperCase().trim().split(" ")));
    }

    public void cleanDictionaryFromExcludeChars(List<String> unCleanedWords) {
        unCleanedWords.forEach(word -> {
            this.words.add(word.replaceAll("[" + excludeChars + "]", ""));
        });
    }

    public Set<String> getDictionary() {
        return words;
    }

    public List<String> validateWordsAfterCleanExcludeChars(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        List<String> wordsToCheckAfterCleanExcludeChars = new ArrayList<>();

        wordsToCheck.forEach(word -> {
            wordsToCheckAfterCleanExcludeChars.add(word.replaceAll("[" + excludeChars + "]", ""));
        });

        validateWords(wordsToCheckAfterCleanExcludeChars);

        return wordsToCheckAfterCleanExcludeChars;
    }

    public void validateWords(List<String> wordsToCheckAfterCleanExcludeChars) throws WordNotValidInDictionaryException {
        WordNotValidInDictionaryException wordNotValidInDictionary = new WordNotValidInDictionaryException(words);

        wordsToCheckAfterCleanExcludeChars.forEach(word -> {
            if(!words.contains(word)) {
                wordNotValidInDictionary.addIllegalWord(word);
            }
        });

        if(wordNotValidInDictionary.isExceptionNeedToThrown()) {
            throw wordNotValidInDictionary;
        }
    }
}