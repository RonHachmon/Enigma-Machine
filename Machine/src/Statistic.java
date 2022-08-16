import java.util.ArrayList;
import java.util.List;

public class Statistic {


    private final List<String> codeFormats = new ArrayList<>();
    private final List<StatisticInput> processedInput = new ArrayList<>();
    private int intCurrentCodeFormatIndex = -1;


    public String historyAndStatistic() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < codeFormats.size(); i++) {
            stringBuilder.append(codeFormats.get(i) + '\n');
            for (int j = 0; j < processedInput.size(); j++) {
                if (processedInput.get(j).getCodeIndex() == i) {
                    stringBuilder.append('\t' + processedInput.get(j).getProcessedInput() + '\n');
                }
            }

        }
        return stringBuilder.toString();
    }

    public int getAmountOfProcessedInputs() {
        return processedInput.size();
    }

    public void addProcessedInput(String input) {
        StatisticInput statisticInput = new StatisticInput();
        statisticInput.setCodeIndex(intCurrentCodeFormatIndex);
        statisticInput.setProcessedInput(input);
        processedInput.add(statisticInput);
    }

    public void addCodeFormats(String codeFormat) {
        intCurrentCodeFormatIndex++;
        codeFormats.add(codeFormat);
    }
}
