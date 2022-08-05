
import jaxb_classes.*;

import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.stream.Collectors;

public class Machine {
    public final  Map <Character, Integer> char_map =new HashMap<>();
    public final Map <Integer,Character> reverse_char_map =new HashMap<>() ;
    public final Map <Character, Character> switch_plug=new HashMap<>();
    public List<Rotor> selected_rotors=new ArrayList<>();
    private List<Rotor> all_rotors=new ArrayList<>();
    public final List<Reflector> reflector_array=new ArrayList<>();
    public Reflector selected_reflector;
    private int amount_of_rotor_needed;
    public Machine()
    {

        this.initilize_reflector();
    }



    public Machine(String char_set)
    {
        set_char_map(char_set);
        set_reverse_char_map();
        selected_reflector = new Reflector();
        System.out.println("Char Set: "+ char_map);
        System.out.println("Reflector:"+ selected_reflector);
    }

    private void initilize_reflector() {
        for (int i = 0; i <5; i++) {
            reflector_array.add(null);
        }
    }


    private void set_reverse_char_map() {
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



    private void set_char_map(String char_set) {
        //maybe change to lambda, not sure how to iterate with index
        for (int i = 0; i <char_set.length() ; i++) {
            if(char_map.put(char_set.charAt(i),i)!=null)
            {
                //throw duplicate char
            }
        }

    }
    public void setSelected_reflector(int reflector_id)
    {

        this.selected_reflector=this.reflector_array.get(reflector_id);
        if( this.selected_reflector==null)
        {
            //throw reflector does not exist
        }
    }
    public void setSelected_rotors(int ...rotors_id)
    {
        //throw out of bound or does not exist
        for (int id:rotors_id) {
            selected_rotors.add(all_rotors.get(id));

        }
    }
    public Character run_encrypt_on_char(char input)
    {
        input = run_char_through_switch_plug(input);
        int running_index = char_map.get(input);
        boolean toRotate=true;
        Character result;

        //run char thought right side of Rotors
        for (int i =0; i <selected_rotors.size() ; i++) {
            System.out.println("    Rotor number: "+(i+1));
            if(toRotate)
            {
                selected_rotors.get(i).rotate();
                toRotate = selected_rotors.get(i).is_rotor_on_notch();
            }

            running_index =  selected_rotors.get(i).get_exit_index_from_right(running_index);
        }

        System.out.println("    Reflector:");
        running_index= selected_reflector.get_exit_index(running_index);

        //run char thought left side of Rotors
        for (int i = selected_rotors.size()-1; i >=0 ; i--) {
            System.out.println("    Rotor number: "+(i+1));
            running_index = selected_rotors.get(i).get_exit_index_from_left(running_index);

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
    public void loadMachineFromFile(String file_name)  {
            if (file_name.length() < 4 || !compare_file_type(file_name,".xml")) {
                //throw new GameLoadException("File is not an xml");
            }

            CTEEnigma enigma_machine = null;
            try {
                enigma_machine = JAXBClassGenerator.unmarshall(file_name, CTEEnigma.class);
            } catch (JAXBException var5) {
//                String msg;
//                if (var5.getLinkedException() instanceof FileNotFoundException) {
//                    msg = "File Not Found";
//                } else {
//                    msg = "JAXB exception";
//                }
//
//                throw new GameLoadException(var5, msg);
            }

            if (enigma_machine == null) {
                //throw new GameLoadException("Failed to load JAXB class");
            }

            this.init_members_from_enigma_machine(enigma_machine);
            //this.m_GameStatus = GriddlerLogic.eGameStatus.LOADED;
    }

    private void init_members_from_enigma_machine(CTEEnigma enigma_machine) {
        this.load_char_set(enigma_machine);
        this.load_rotators(enigma_machine);
        this.load_reflector(enigma_machine);

    }

    private void load_rotators(CTEEnigma enigma_machine) {
        List<CTERotor> xml_rotors_arr = enigma_machine.getCTEMachine().getCTERotors().getCTERotor();
        amount_of_rotor_needed = enigma_machine.getCTEMachine().getRotorsCount();
        xml_rotors_arr = xml_rotors_arr.stream().
                                            sorted(Comparator.comparing(CTERotor::getId)).
                                            collect(Collectors.toList());
        Rotor current_rotor;
        for (CTERotor xml_rotor: xml_rotors_arr)
        {
            current_rotor = Rotor.create_rotor_from_XML(xml_rotor);
            all_rotors.add(current_rotor);
            System.out.println("Rotor:");
            System.out.println(current_rotor);
            System.out.println( );
        }

    }

    private void load_reflector(CTEEnigma enigma_machine) {
        CTEReflectors xml_reflextor_arr = enigma_machine.getCTEMachine().getCTEReflectors();
        Reflector current_reflector;
        for (CTEReflector xml_reflector: xml_reflextor_arr.getCTEReflector())
        {
            current_reflector = Reflector.create_reflector_from_XML(xml_reflector,char_map.size());
            int position =  Machine.converte_roman_to_int(xml_reflector.getId());
            System.out.println("Reflector:"+ current_reflector);
            reflector_array.set(position,current_reflector);
        }

    }
    private static int converte_roman_to_int(String roman_number)
    {
        switch (roman_number)
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
                //throw .. 
                
        }
        return -1;
    }

    private void load_char_set(CTEEnigma enigma_machine) {
        String char_collection=enigma_machine.getCTEMachine().getABC();
        char_collection=char_collection.replaceAll("[\\n\t]", "");
        this.set_char_map(char_collection);
        this.set_reverse_char_map();

    }

    private boolean compare_file_type(String file_name,String file_type) {
        if(file_type.length()>=file_name.length())
            return false;
        String file_ending = file_name.substring(file_name.length() - file_type.length()).toLowerCase();
        return file_ending.compareTo(file_type)==0;
    }


    //currently hard coded, should recive an array of char for each rotor and set rotor start position based on the char
    //Example {O , D ,X } O on most left array (last array), D middle , x most right array (first array) on selected rotors
    //should receive 10 index for X , 2 for D and 12 for O
    public void set_starting_index() {
//        this.selected_rotors.get(0).set_starting_index(2);
//        this.selected_rotors.get(1).set_starting_index(2);
        this.selected_rotors.get(0).set_starting_index(10);
        this.selected_rotors.get(1).set_starting_index(2);
        this.selected_rotors.get(2).set_starting_index(12);
    }
}
