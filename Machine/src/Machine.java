
import jaxb_classes.*;

import java.util.*;
import java.util.stream.Collectors;

public class Machine {
    private final  Map <Character, Integer> charMap =new HashMap<>();
    private final Map <Integer,Character> reverseCharMap =new HashMap<>() ;
    private final Map <Character, Character> switchPlug =new HashMap<>();
    private List<Rotor> selectedRotors =new ArrayList<>();
    private List<Rotor> allRotors =new ArrayList<>();
    public final List<Reflector> allReflectors=new ArrayList<>();
    public Reflector selectedReflector;
    private String allChars;
    private int amountOfRotorNeeded;
    public Machine()
    {

        this.initilizeReflector();
    }
    public int getamountOfRotorNeeded()
    {
        return amountOfRotorNeeded;
    }



    private void initilizeReflector() {
        for (int i = 0; i <5; i++) {
            allReflectors.add(null);
        }
    }
    public List<Rotor> getAllRotors()
    {
        return allRotors;
    }
    public int getAmountOfAvailableReflectrors()
    {
        int count =0;
        for (Reflector reflector:this.allReflectors)
        {
            if(reflector!=null)
            {
                count++;
            }
        }
        return count;
    }


    private void setReverseCharMap() {
        charMap.forEach((key, value) -> reverseCharMap.put(value, key));
    }
    public void addSwitchPlug(char firstLetter, char secondLetter)
    {
        if(switchPlug.put(firstLetter,secondLetter)!=null
            || switchPlug.put(secondLetter,firstLetter)!=null)
        {
            //throw duplicate char
        }

    }



    private void setCharMap(String char_set) {
        //maybe change to lambda, not sure how to iterate with index
        for (int i = 0; i <char_set.length() ; i++) {
            if(charMap.put(char_set.charAt(i),i)!=null)
            {
                //throw duplicate char
            }
        }

    }
    public void setSelectedReflector(int reflector_id)
    {

        this.selectedReflector =this.allReflectors.get(reflector_id);
        if( this.selectedReflector ==null)
        {
            //throw reflector does not exist
        }
    }
    public void setSelectedRotors(List<Integer> rotorsID)
    {
        //throw out of bound or does not exist
        for (int id:rotorsID) {
            selectedRotors.add(allRotors.get(id));

        }
    }
    public Character run_encrypt_on_char(char input)
    {
        input = run_char_through_switch_plug(input);
        int running_index = charMap.get(input);
        boolean toRotate=true;
        Character result;

        //run char thought right side of Rotors
        for (int i = 0; i < selectedRotors.size() ; i++) {
            //System.out.println("    Rotor number: "+(i+1));
            if(toRotate)
            {
                selectedRotors.get(i).rotate();
                toRotate = selectedRotors.get(i).is_rotor_on_notch();
            }

            running_index =  selectedRotors.get(i).get_exit_index_from_right(running_index);
        }

        //System.out.println("    Reflector:");
        running_index= selectedReflector.get_exit_index(running_index);

        //run char thought left side of Rotors
        for (int i = selectedRotors.size()-1; i >=0 ; i--) {
            //System.out.println("    Rotor number: "+(i+1));
            running_index = selectedRotors.get(i).get_exit_index_from_left(running_index);

        }

        result = reverseCharMap.get(running_index);
        result =run_char_through_switch_plug(result);
        //System.out.println("output char: "+result);
        return result;
    }


    private char run_char_through_switch_plug(char input) {
        if(switchPlug.get(input)!=null)
        {
            input = switchPlug.get(input);
        }
        return input;
    }


    public String runEncryptOnString(String input) {
        this.isAllCharsExistInCharSet(input);
        String res=new String();
        for (int i = 0; i <input.length() ; i++) {
            res+=this.run_encrypt_on_char(input.charAt(i));
        }
    return res;

    }






    //XML function
    public void loadRotators(CTEEnigma enigma_machine) {
        List<CTERotor> xmlRotorsArr = enigma_machine.getCTEMachine().getCTERotors().getCTERotor();
        amountOfRotorNeeded = enigma_machine.getCTEMachine().getRotorsCount();
        if(amountOfRotorNeeded<2)
        {
            throw new IllegalArgumentException("amount of rotors on the machine must be at least 2");
        }
        xmlRotorsArr = sortXMLRotors(xmlRotorsArr);
        loadArrayOfRotators(xmlRotorsArr);
        if(allRotors.size()<amountOfRotorNeeded)
        {
            throw new IllegalArgumentException("amount of rotors should be at least "+ amountOfRotorNeeded);
        }

    }

    private void loadArrayOfRotators(List<CTERotor> xmlRotorsArr) {
        Rotor currentRotor=new Rotor();
        for(int i = 0; i< xmlRotorsArr.size(); i++)
        {
            //check if the id are sequential and unique
            if(xmlRotorsArr.get(i).getId()!=i+1)
            {
                throw new IllegalArgumentException("rotor id must be unique and in a sequential order," +
                                                    "starting from 1");
            }

            currentRotor = Rotor.createRotorFromXML(xmlRotorsArr.get(i),this.allChars);
            allRotors.add(currentRotor);
        }
        //System.out.println(currentRotor);
    }

    private List<CTERotor> sortXMLRotors(List<CTERotor> xmlRotorsArr) {
        xmlRotorsArr = xmlRotorsArr.stream().
                                            sorted(Comparator.comparing(CTERotor::getId)).
                                            collect(Collectors.toList());
        return xmlRotorsArr;
    }

    public void loadReflector(CTEEnigma enigma_machine) {
        CTEReflectors xmlReflextorArr = enigma_machine.getCTEMachine().getCTEReflectors();
        Reflector currentReflector;
        for (CTEReflector xmlReflector: xmlReflextorArr.getCTEReflector())
        {
            currentReflector = Reflector.createReflectorFromXML(xmlReflector, charMap.size());
            int position =  Machine.converteRomanToInt(xmlReflector.getId());
            //System.out.println("Reflector:"+ currentReflector);
            allReflectors.set(position,currentReflector);
        }

    }


    public void loadCharSet(CTEEnigma enigma_machine) {
        String charCollection=enigma_machine.getCTEMachine().getABC();
        charCollection=charCollection.replaceAll("[\\n\t]", "");
        charCollection=replaceSpecialXMLchar(charCollection);
        if(charCollection.length()%2==1)
        {
            throw new IllegalArgumentException("amount of characters must be even");
        }
        this.allChars=charCollection.toUpperCase(Locale.ROOT);
        //System.out.println("all char: "+this.allChars);
        this.setCharMap(charCollection);
        this.setReverseCharMap();
    }
    private String replaceSpecialXMLchar(String string)
    {
        String result =  string.replace("&amp;","&")
                            .replace("&lt;","<")
                            .replace("&gt;","<")
                            .replace("&quot;","")
                            .replace("&apos;","'");
        return result;
    }




    public void setStartingIndex(String startingCharArray) {
        if(startingCharArray.length() !=selectedRotors.size())
        {
            throw new IllegalArgumentException("please choose starting index for all "+selectedRotors.size()+" rotors");
        }

        for (int i = 0; i <startingCharArray.length(); i++) {
            this.selectedRotors.get(i).setStartingIndex(startingCharArray.charAt(i));
        }

    }

    private static int converteRomanToInt(String romanNumber)
    {
        switch (romanNumber)
        {
            case "I":
                return 0;
            case("II"):
                return 1;
            case("III"):
                return 2;
            case("IV"):
                return 3;
            case("V"):
                return 4;
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must be I,II,III,IV,V");

        }
    }

    private void isAllCharsExistInCharSet(String inputString)
    {
        Set <Character> allChar =new HashSet<>();
        Set <Character> notFoundChars =new HashSet<>();
        StringBuilder nonExistingChar=new StringBuilder();

        allChars.chars().forEach(character->allChar.add((char)character));

        inputString.chars().forEach(c->
        {
            //for new char in set add return true
            if(allChar.add((char)c))
            {
                notFoundChars.add((char) c);
            }
        });


        if(!notFoundChars.isEmpty())
        {
            notFoundChars.forEach(character ->nonExistingChar.append(character+" , ") );
            throw new IllegalArgumentException("machine does not contain letter: "+notFoundChars);

        }



    }

}
