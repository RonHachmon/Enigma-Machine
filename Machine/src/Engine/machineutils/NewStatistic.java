package Engine.machineutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewStatistic {
    private final Map<String, ArrayList<NewStatisticInput>> statistic = new HashMap<>();

    public ArrayList<NewStatisticInput> getStatsPerCode(String code)
    {
        return statistic.get(code);

    }
    public void addStatistic(String code, String input,String output,Integer duration)
    {
        statistic.computeIfAbsent(code,s -> new ArrayList<>());
        statistic.get(code).add(new NewStatisticInput(input,output,duration) );

    }
    public void printMap()
    {
        for (String code: statistic.keySet()) {
            String key = code;
            ArrayList<NewStatisticInput> value = statistic.get(code);
            System.out.println(code);
            value.forEach(newStatisticInput -> System.out.println("\t"+newStatisticInput));
        }
    }
}
