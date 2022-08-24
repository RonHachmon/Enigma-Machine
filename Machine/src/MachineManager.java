import jaxb_classes.CTEEnigma;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachineManager {
    private Machine machine = new Machine();
    private Statistic statistic = new Statistic();
    private Setting setting = new Setting();
    private MachineInformation machineInformation = null;
    private int processedInputCounter = 0;
    private boolean isMachineExists = false;

    public String getStatistic() {
        return statistic.historyAndStatistic();
    }

    public void addCodeToStatistic() {
        this.statistic.addCodeFormats(this.setting.getInitialFullMachineCode());
    }

    public int getAmountOfProcessedInputs() {
        return this.statistic.getAmountOfProcessedInputs();
    }

    public String encryptSentence(String sentence) {
        Instant timeStart = Instant.now();
        String output = this.machine.runEncryptOnString(sentence);
        StringBuilder timeAndStatistic = new StringBuilder(++processedInputCounter + ". ");
        Duration duration = Duration.between(timeStart, Instant.now());

        buildHistoryAndStatistic(sentence, timeAndStatistic, output, duration);
        this.statistic.addProcessedInput(timeAndStatistic.toString());

        return output;
    }

    public void commitChangesToMachine() {
        processedInputCounter = 0;
        machine.setTheInitialCodeDefined(true);
        this.addCodeToStatistic();
    }

    //might need to be modified , depends on if rotor comes left to right
    // or right to left. currently from right to left
    public void setSelectedRotors(List<Integer> rotorsID) {
        if (rotorsID.size() != machineInformation.getAmountOfRotorsRequired()) {
            throw new IllegalArgumentException("amount of indexes must be " + machineInformation.getAmountOfRotorsRequired());
        }
        this.machine.setSelectedRotors(rotorsID);
        this.setting.setSettingRotators(rotorsID);
    }

    public void setSelectedReflector(int reflectorId) {
        this.machine.setSelectedReflector(reflectorId);
        this.setting.setSettingReflector(reflectorId + 1);
    }

    public void setStartingIndex(String startingCharArray) {
        this.machine.setStartingIndex(startingCharArray);
        this.setting.setSettingStartingChar(startingCharArray,this.machine);
    }

    public void setIsMachineExists(boolean isMachineExists){
        this.isMachineExists = isMachineExists;
    }

    public void setSwitchPlug(String plugs) {
        if (plugs.length() % 2 != 0) {
            throw new IllegalArgumentException("invalid plugs, each character should be paired ");
        }
        this.machine.resetSwitchPlug();
        this.setting.resetPlugs();

        for (int i = 0; i < plugs.length(); i += 2) {
            this.addSwitchPlug(plugs.charAt(i), plugs.charAt(i + 1));
        }
    }

    private void addSwitchPlug(char firstLetter, char secondLetter) {
        this.machine.addSwitchPlug(firstLetter, secondLetter);
        this.setting.addPlug(firstLetter, secondLetter);
    }

    public String getInitialFullMachineCode() {
        return this.setting.getInitialFullMachineCode();
    }

    public String getCurrentCodeSetting() {
        return this.setting.getCurrentMachineCode(this.machine);
    }

    public void resetMachineCode() {
        StringBuilder stringBuilder = new StringBuilder(this.setting.getInitialRotorIndexes());
        stringBuilder.reverse();
        this.setStartingIndex(stringBuilder.toString());
    }

    public MachineInformation getMachineInformation() {
        return machineInformation;
    }

    //copied from gridler
    public void createMachineFromXML(String filePath) {
        Path path = Paths.get(filePath);
        if (filePath.length() < 4 || !compareFileType(filePath, ".xml")) {
            throw new IllegalArgumentException(path.getFileName() + ", must be a xml file");
        }

        CTEEnigma enigma_machine = null;
        try {
            enigma_machine = JAXBClassGenerator.unmarshall(filePath, CTEEnigma.class);
        } catch (JAXBException e) {
            String msg;
            if (e.getLinkedException() instanceof FileNotFoundException) {
                msg = "File Not Found";
            } else {
                msg = "JAXB exception";
            }
            throw new IllegalArgumentException(msg);
        }

        if (enigma_machine == null) {
            throw new IllegalArgumentException("Failed to load JAXB class");
        }

        this.loadEnigmaPartFromXMLEnigma(enigma_machine);
        //this.m_GameStatus = GriddlerLogic.eGameStatus.LOADED;
    }

    private boolean compareFileType(String fileName, String fileType) {
        if (fileType.length() >= fileName.length())
            return false;
        String file_ending = fileName.substring(fileName.length() - fileType.length()).toLowerCase();
        return file_ending.compareTo(fileType) == 0;
    }

    private void loadEnigmaPartFromXMLEnigma(CTEEnigma enigma) {
        Machine tempMachine = new Machine();
        tempMachine.loadCharSet(enigma);
        tempMachine.loadRotators(enigma);
        tempMachine.loadReflector(enigma);
        machine = tempMachine;
        isMachineExists = true;
        this.machineInformation = new MachineInformation(machine);
    }

    public void autoZeroMachine() {
        Random rand = new Random();
        List<Integer> indexes = new ArrayList<>();
        String startingCharArray = new String();

        // Set rotors & starting indexes
        for (int i = 0; i < machineInformation.getAmountOfRotorsRequired(); i++) {
            int rotorIndex = rand.nextInt(machineInformation.getAmountOfRotors());
            while (indexes.contains(rotorIndex)) {
                rotorIndex = rand.nextInt(machineInformation.getAmountOfRotors());
            }
            int randomInt = rand.nextInt(machineInformation.getAvailableChars().length());
            char randomChar = machineInformation.getAvailableChars().charAt(randomInt);

            indexes.add(rotorIndex);
            startingCharArray += randomChar;
        }

        setSelectedRotors(indexes);
        setStartingIndex(startingCharArray);
        setSelectedReflector(rand.nextInt(machineInformation.getAvailableReflectors()));
        commitChangesToMachine();
    }

    private void buildHistoryAndStatistic(String sentence, StringBuilder timeAndStatistic, String output, Duration duration) {
        timeAndStatistic.append('<' + sentence + '>');
        timeAndStatistic.append("-->");
        timeAndStatistic.append('<' + output + '>');
        timeAndStatistic.append("(" + duration.getNano() + " nano-seconds)");
    }

    public boolean isMachineExists() {
        return this.isMachineExists;
    }

    public boolean isMachineSettingInitialized() {
        return machine.isTheInitialCodeDefined();
    }


    public void saveMachineToFile(String path) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(path))) {
            out.writeObject(machine);
            out.writeObject(statistic);
            out.writeObject(setting);
            out.writeObject(processedInputCounter);
            out.flush();
        }
    }

    public void loadMachineFromFile(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(path))) {
            machine = (Machine) in.readObject();
            statistic = (Statistic) in.readObject();
            setting = (Setting) in.readObject();
            processedInputCounter = (int) in.readObject();
            this.machineInformation = new MachineInformation(machine);
        }
    }

    public void reset()
    {
        this.statistic.reset();
        this.processedInputCounter=0;

    }
}
