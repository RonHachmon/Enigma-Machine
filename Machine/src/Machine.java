
import jaxb_classes.*;

import java.util.*;
import java.util.stream.Collectors;

public class Machine {
    private final  Map <Character, Integer> char_map =new HashMap<>();
    private final Map <Integer,Character> reverse_char_map =new HashMap<>() ;
    private final Map <Character, Character> switch_plug=new HashMap<>();
    private List<Rotor> selectedRotors =new ArrayList<>();
    private List<Rotor> allRotors =new ArrayList<>();
    public final List<Reflector> selectedRotorsArray=new ArrayList<>();
    public Reflector selected_reflector;
    private String allChars;
    private int amountOfRotorNeeded;
    public Machine()
    {

        this.initilize_reflector();
    }



    public Machine(String char_set)
    {
        setCharMap(char_set);
        setReverseCharMap();
        selected_reflector = new Reflector();
        System.out.println("Char Set: "+ char_map);
        System.out.println("Reflector:"+ selected_reflector);
    }

    private void initilize_reflector() {
        for (int i = 0; i <5; i++) {
            selectedRotorsArray.add(null);
        }
    }


    private void setReverseCharMap() {
        char_map.forEach((key, value) -> reverse_char_map.put(value, key));
    }
    public void add_switch_plug(char first_letter,char second_letter)
    {
        if(switch_plug.put(first_letter,second_letter)!=null
            ||switch_plug.put(second_letter,first_letter)!=null)
        {
            //throw duplicate char
        }

    }



    private void setCharMap(String char_set) {
        //maybe change to lambda, not sure how to iterate with index
        for (int i = 0; i <char_set.length() ; i++) {
            if(char_map.put(char_set.charAt(i),i)!=null)
            {
                //throw duplicate char
            }
        }

    }
    public void setSelectedReflector(int reflector_id)
    {

        this.selected_reflector=this.selectedRotorsArray.get(reflector_id);
        if( this.selected_reflector==null)
        {
            //throw reflector does not exist
        }
    }
    public void setSelectedRotors(int ...rotors_id)
    {
        //throw out of bound or does not exist
        for (int id:rotors_id) {
            selectedRotors.add(allRotors.get(id));

        }
    }
    public Character run_encrypt_on_char(char input)
    {
        input = run_char_through_switch_plug(input);
        int running_index = char_map.get(input);
        boolean toRotate=true;
        Character result;

        //run char thought right side of Rotors
        for (int i = 0; i < selectedRotors.size() ; i++) {
            System.out.println("    Rotor number: "+(i+1));
            if(toRotate)
            {
                selectedRotors.get(i).rotate();
                toRotate = selectedRotors.get(i).is_rotor_on_notch();
            }

            running_index =  selectedRotors.get(i).get_exit_index_from_right(running_index);
        }

        System.out.println("    Reflector:");
        running_index= selected_reflector.get_exit_index(running_index);

        //run char thought left side of Rotors
        for (int i = selectedRotors.size()-1; i >=0 ; i--) {
            System.out.println("    Rotor number: "+(i+1));
            running_index = selectedRotors.get(i).get_exit_index_from_left(running_index);

        }

        result = reverse_char_map.get(running_index);
        result =run_char_through_switch_plug(result);
        System.out.println("output char: "+result);
        return result;
    }


    private char run_char_through_switch_plug(char input) {
        if(switch_plug.get(input)!=null)
        {
            input =switch_plug.get(input);
        }
        return input;
    }


    public String run_encrypt_on_string(String input) {
        String res=new String();
        for (int i = 0; i <input.length() ; i++) {
            res+=this.run_encrypt_on_char(input.charAt(i));
        }
    return res;

    }


    public void loadRotators(CTEEnigma enigma_machine) {
        List<CTERotor> xmlRotorsArr = enigma_machine.getCTEMachine().getCTERotors().getCTERotor();
        amountOfRotorNeeded = enigma_machine.getCTEMachine().getRotorsCount();
        xmlRotorsArr = xmlRotorsArr.stream().
                                            sorted(Comparator.comparing(CTERotor::getId)).
                                            collect(Collectors.toList());
        Rotor currentRotor;
        for (CTERotor xmlRotor: xmlRotorsArr)
        {
            currentRotor = Rotor.createRotorFromXML(xmlRotor,this.allChars);
            allRotors.add(currentRotor);
            System.out.println("Rotor:");
            System.out.println(currentRotor);
            System.out.println( );
        }

    }

    public void loadReflector(CTEEnigma enigma_machine) {
        CTEReflectors xmlReflextorArr = enigma_machine.getCTEMachine().getCTEReflectors();
        Reflector currentReflector;
        for (CTEReflector xmlReflector: xmlReflextorArr.getCTEReflector())
        {
            currentReflector = Reflector.createReflectorFromXML(xmlReflector,char_map.size());
            int position =  Machine.converteRomanToInt(xmlReflector.getId());
            System.out.println("Reflector:"+ currentReflector);
            selectedRotorsArray.set(position,currentReflector);
        }

    }


    public void loadCharSet(CTEEnigma enigma_machine) {
        String charCollection=enigma_machine.getCTEMachine().getABC();
        charCollection=charCollection.replaceAll("[\\n\t]", "");
        if(charCollection.length()%2==1)
        {
            throw new IllegalArgumentException("amount of characters must be even");
        }
        this.allChars=charCollection;
        this.setCharMap(charCollection);
        this.setReverseCharMap();
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




    //currently hard coded, should recive an array of char for each rotor and set rotor start position based on the char
    //Example {O , D ,X } O on most left array (last array), D middle , x most right array (first array) on selected rotors
    //should receive 10 index for X , 2 for D and 12 for O
    public void setStartingIndex(String [] startingCharArray) {
        if(startingCharArray.length!=selectedRotors.size())
        {
            throw new IllegalArgumentException("please choose starting index for all "+selectedRotors.size()+" rotors");
        }

        for (int i = 0; i <startingCharArray.length; i++) {
            this.selectedRotors.get(i).setStartingIndex(startingCharArray[i].charAt(0));
        }

    }


}
