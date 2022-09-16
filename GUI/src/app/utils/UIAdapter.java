package app.utils;


import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<PrimeNumberData> addNewPrimeNumber;

    private Consumer<Integer> updateTotalFoundInteger;
    private Runnable updateDistinct;
    public UIAdapter(Consumer<PrimeNumberData> addNewPrimeNumber, Consumer<Integer> updateTotalFoundInteger, Runnable updateDistinct) {
        this.addNewPrimeNumber = addNewPrimeNumber;
        this.updateTotalFoundInteger = updateTotalFoundInteger;
        this.updateDistinct = updateDistinct;
    }

    public void addNewCandidate(PrimeNumberData primeNumber) {
        Platform.runLater(
                () -> {
                    addNewPrimeNumber.accept(primeNumber);
                    updateDistinct.run();
                }
        );
    }



    public void updateTotalProcessedWords(int delta) {
        Platform.runLater(
                () -> updateTotalFoundInteger.accept(delta)
        );
    }

}
