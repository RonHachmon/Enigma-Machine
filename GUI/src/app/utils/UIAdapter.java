package app.utils;


import DTO.DecryptionCandidate;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<DecryptionCandidate> addNewCandidate;

    private Consumer<Integer> updateTotalFoundInteger;
    private Runnable updateDistinct;
    public UIAdapter(Consumer<DecryptionCandidate> addNewCandidate, Consumer<Integer> updateTotalFoundWords, Runnable updateDistinct) {
        this.addNewCandidate = addNewCandidate;
        this.updateTotalFoundInteger = updateTotalFoundWords;
        this.updateDistinct = updateDistinct;
    }
    public void addNewCandidate(DecryptionCandidate decryptionCandidate) {
        Platform.runLater(
                () -> {
                    addNewCandidate.accept(decryptionCandidate);
                    updateDistinct.run();
                }
        );
    }

/*    public void addNewCandidate(PrimeNumberData primeNumber) {
        Platform.runLater(
                () -> {
                    addNewPrimeNumber.accept(primeNumber);
                    updateDistinct.run();
                }
        );
    }*/



    public void updateTotalProcessedWords(int delta) {
        Platform.runLater(
                () -> updateTotalFoundInteger.accept(delta)
        );
    }

}
