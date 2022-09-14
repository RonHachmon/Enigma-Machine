package primeengine;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeFinder {

    private int amountOfIntegerToFind;
    private int amountFound=0;

    private List<Integer> primeNumbers=new ArrayList<>();
    public PrimeFinder(int amountOfIntegerToFind)
    {
        this.amountOfIntegerToFind=amountOfIntegerToFind;
    }

    public void findPrimes(Task<Boolean> aTask)
    {
        DaemonThread daemonThreadFactory = new DaemonThread();

        ExecutorService threadExecutor = Executors.newFixedThreadPool(6,daemonThreadFactory);
        int runningNumber=10000;

        while ( amountFound!=amountOfIntegerToFind)
        {
            int finalRunningNumber = runningNumber;
            threadExecutor.execute(()-> {
                if (isPrime(finalRunningNumber)) {
                    System.out.println(finalRunningNumber);
                    this.submit(finalRunningNumber);
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            runningNumber++;
        }
        System.out.println("done");
    }
    private boolean isPrime(Integer runningNumber) {
        for (int i = 2; i <= Math.sqrt(runningNumber); i++) {
            if (runningNumber % i == 0) {
                return false;
            }
        }
        return true;
    }

    synchronized private void submit(Integer runningNumber) {
        amountFound++;
        primeNumbers.add(runningNumber);
    }
     public int listSize() {
        return this.primeNumbers.size();
    }

    public int getAmountFound() {
        return amountFound;
    }

    public List<Integer> getPrimeNumbers() {
        return primeNumbers;
    }
}



