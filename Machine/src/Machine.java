
import jaxb_classes.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine {
    public final  Map <Character, Integer> char_map;
    public final Map <Integer,Character> reverse_char_map;
    public   Map <Character, Character> switch_plug;
    public List<Rotor> Rotor_arr=new ArrayList<>();
    public final Reflector ref;
    public Machine()
    {

        char_map = new HashMap<>();
        reverse_char_map = new HashMap<>();
        switch_plug = new HashMap<>();
        switch_plug.put('A','F');
        switch_plug.put('F','A');
        ref = new Reflector();
        //System.out.println("Char Set: "+ char_map);
        System.out.println("Reflector:"+ ref);
    }
    public Machine(String char_set)
    {

        char_map = new HashMap<>();
        reverse_char_map = new HashMap<>();
        switch_plug = new HashMap<>();
        switch_plug.put('A','F');
        switch_plug.put('F','A');
        set_char_map(char_set);
        set_reverse_char_map();
        ref = new Reflector();
        System.out.println("Char Set: "+ char_map);
        System.out.println("Reflector:"+ ref);
    }



    private void set_reverse_char_map() {
        char_map.forEach((key, value) -> reverse_char_map.put(value, key));
    }


//    private void set_char_map() {
//
//        char first_char='A';
//        for (int i = 0; i <6 ; i++) {
//            char_map.put(first_char,i);
//            first_char++;
//        }
//    }

    private void set_char_map(String char_set) {
        //maybe change to lambda, not sure how to iterate with index
        for (int i = 0; i <char_set.length() ; i++) {
            if(char_map.put(char_set.charAt(i),i)!=null)
            {
                //throw duplicate char
            }

        }

    }
    public Character run_encrypt_on_char(char input)
    {
        input = run_char_through_switch_plug(input);
        int running_index = char_map.get(input);
        boolean toRotate=true;
        Character result;

        //run char thought right side of Rotors
        for (int i =0; i <Rotor_arr.size() ; i++) {
            System.out.println("    Rotor number: "+(i+1));
            if(toRotate)
            {
                Rotor_arr.get(i).rotate();
                toRotate = Rotor_arr.get(i).is_rotor_on_notch();
            }

            running_index =  Rotor_arr.get(i).get_exit_index_from_right(running_index);
        }

        System.out.println("    Reflector:");
        running_index= ref.get_exit_index(running_index);

        //run char thought left side of Rotors
        for (int i = Rotor_arr.size()-1; i >=0 ; i--) {
            System.out.println("    Rotor number: "+(i+1));
            running_index = Rotor_arr.get(i).get_exit_index_from_left(running_index);

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
                enigma_machine = JAXBClassGenerator.unmarshall("test files/ex1-sanity-small.xml", CTEEnigma.class);
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
    }

    private void load_reflector(CTEEnigma enigma_machine) {

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
}
