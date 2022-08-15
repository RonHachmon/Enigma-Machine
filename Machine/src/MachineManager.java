import jaxb_classes.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MachineManager {
    private Machine machine=new Machine();
    private Satistic satistic= new Satistic();
    private Setting setting = new Setting();

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Satistic getSatistic() {
        return satistic;
    }

    public void setSatistic(Satistic satistic) {
        this.satistic = satistic;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }


    //need to be implemented
    public String getAmountOfProccesedInputs()
    {
        this.satistic.getAmountOfProccesedInputs();
        return null;
    }



    //might need to be modified , depends on if rotor comes left to right
    // or right to left. currently from right to left
    public void setSelectedRotors(List<Integer> rotorsID)
    {
        if(rotorsID.size()!=this.amountOfRotors())
        {
            throw new IllegalArgumentException("amount of indexes must be "+this.amountOfRotors() );
        }
        this.machine.setSelectedRotors(rotorsID);

        for (int i =rotorsID.size()-1 ; i >= 0; i--) {
            this.setting.addSettingRotators(rotorsID.get(i)+1);
        }

    }
    public void setSelectedReflector(int reflectorId)
    {
        this.machine.setSelectedReflector(reflectorId);
        this.setting.setSettingReflector(reflectorId+1);
    }
    public void setStartingIndex(String startingCharArray) {
        this.machine.setStartingIndex(startingCharArray);
        this.setting.setSettingStartingChars(startingCharArray);

    }
    public void addSwitchPlug(char firstLetter, char secondLetter) {
        this.machine.addSwitchPlug(firstLetter,secondLetter);
        this.setting.addPlug(firstLetter,secondLetter);

    }
    public String fullCodeSetting()
    {
        return this.setting.getFullMachineCode();
    }

    public int availableReflectors()
    {
        return this.machine.getAmountOfAvailableReflectrors();
    }
    public int amountOfRotors()
    {
        return this.machine.getamountOfRotorNeeded();
    }


    public String getNotchPosition() {
        StringBuilder output= new StringBuilder();
        int rotorIDCounter=1;
        for (Rotor rotor : this.getMachine().getAllRotors()) {
            output.append("Rotor #"+rotorIDCounter+" "+ "notch ");
            output.append((rotor.notchIndex()));
            output.append(" ");
            rotorIDCounter++;
        }
        //output.deleteCharAt(output.length());
        return output.toString();
    }







    //copied from gridler
    public void createMachineFromXML(String filePath) {
        Path path = Paths.get(filePath);
        if (filePath.length() < 4 || !compareFileType(filePath,".xml")) {
            throw new IllegalArgumentException(path.getFileName()+", must be a xml file");
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
    private boolean compareFileType(String fileName,String fileType) {
        if(fileType.length()>=fileName.length())
            return false;
        String file_ending = fileName.substring(fileName.length() - fileType.length()).toLowerCase();
        return file_ending.compareTo(fileType)==0;
    }
    private void loadEnigmaPartFromXMLEnigma(CTEEnigma enigma){
        machine.loadCharSet(enigma);
        machine.loadRotators(enigma);
        machine.loadReflector(enigma);

    }

}
