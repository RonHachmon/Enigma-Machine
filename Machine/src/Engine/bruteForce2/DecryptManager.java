package Engine.bruteForce2;

import DTO.DMData;


import Engine.bruteForce2.utils.CandidateList;
import Engine.bruteForce2.utils.Dictionary;
import Engine.jaxb_classes.CTEDictionary;
import Engine.jaxb_classes.CTEEnigma;
import Engine.machineutils.JAXBClassGenerator;
import Engine.machineutils.MachineManager;

import javax.xml.bind.JAXBException;
import java.util.Set;


public class DecryptManager {

    private long combination;

    private MachineManager machineManager;
    private DMData DMdata;
    private TaskManger tasksManager;
    private Dictionary dictionary=new Dictionary();


    public DecryptManager(MachineManager machineManager,DMData dmData) {
        this.machineManager = machineManager;
        this.DMdata=dmData;
        this.loadDictionary();
    }

    public void startDeciphering() throws Exception{
        tasksManager = new TaskManger(dictionary,DMdata,machineManager,(stop) -> System.out.println("ff"));
        System.out.println(" hey");
        tasksManager.start();



    }
    private void loadDictionary()
    {
        CTEEnigma enigma_machine = null;
        try {
            enigma_machine = JAXBClassGenerator.unmarshall(machineManager.getFilePath(), CTEEnigma.class);
            CTEDictionary cteDictionary = enigma_machine.getCTEDecipher().getCTEDictionary();
            this.dictionary.setDictionary(cteDictionary.getWords(),cteDictionary.getExcludeChars());
        } catch (JAXBException e) {

        }
    }
    public Dictionary getDictionary()
    {
        return dictionary;
    }
    public CandidateList getCandidateList()
    {
        return tasksManager.getCandidateList();
    }
    public int getSizeOfCandidateList()
    {
        return tasksManager.getCandidateList().getSize();
    }

    public long getTotalTaskSize()
    {
        return tasksManager.getTotalWork();
    }
    public long getWorkDone()
    {
        return tasksManager.getWorkDone();
    }


}