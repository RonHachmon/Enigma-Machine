package app.utils;

import javafx.beans.property.SimpleIntegerProperty;

public class PrimeNumberData {
    protected SimpleIntegerProperty number;

    public PrimeNumberData(Integer primeNumber)
    {
        number=new SimpleIntegerProperty(primeNumber);
    }

    public Integer getInteger() {
        return number.get();
    }

}

/*import java.util.ArrayList;
        import java.util.List;
        import java.lang.Math;

// PrintTask class sleeps for a random times
class Task implements Runnable {


    private String name;
    private Integer numberToCrack;
    private Integer initalnumberToCrack;

    private List<Integer> numbers=new ArrayList<>();

    // assign name to thread
    public Task(int numberToCrack) {
        this.numberToCrack=numberToCrack;
        initalnumberToCrack=numberToCrack;
    }

    @Override
    public void run() {

        System.out.println(initalnumberToCrack + " going to run ");
        while (numberToCrack!=1)
        {
            for (int i = 2; i <=numberToCrack ; i++) {
                if(numberToCrack%i==0)
                {
                    numbers.add(i);
                    numberToCrack=numberToCrack/i;
                    break;
                }
            }
        }
        System.out.println(initalnumberToCrack + " done = "+numbers);;
    }
}*/

/*
    Random random=new Random();
    List<Task> tasksList = new ArrayList<>();
            for (int i=0 ; i < 6 ; i++) {
        tasksList.add(new Task(random.nextInt(1000)));
        }
        // create ExecutorService to manage threads
        ExecutorService threadExecutor = Executors.newFixedThreadPool(6);

        // start threads and place in runnable state
        for (Task printTask : tasksList) {
        threadExecutor.execute(printTask);
        }

        threadExecutor.shutdown(); // shutdown worker threads

        System.out.println("Threads started, main ends\n");
        } // end main*/
