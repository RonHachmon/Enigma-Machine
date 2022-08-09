import jaxb_classes.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MachineManager {
    private Machine machine=new Machine();
    private Satistic satistic;
    private Setting setting;

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
    private void loadEnigmaPartFromXMLEnigma(CTEEnigma enigma){
        machine.loadCharSet(enigma);
        machine.loadRotators(enigma);
        machine.loadReflector(enigma);

    }
    private boolean compareFileType(String fileName,String fileType) {
        if(fileType.length()>=fileName.length())
            return false;
        String file_ending = fileName.substring(fileName.length() - fileType.length()).toLowerCase();
        return file_ending.compareTo(fileType)==0;
    }
}
