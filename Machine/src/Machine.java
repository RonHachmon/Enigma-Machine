
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
        set_char_map();
        set_reverse_char_map();
        ref = new Reflector();
        System.out.println("Char Set: "+ char_map);
        System.out.println("Reflector:"+ ref);
    }


    private void set_reverse_char_map() {
        for (Map.Entry<Character,Integer> entry : char_map.entrySet()) {
            reverse_char_map.put(entry.getValue(), entry.getKey());
        }
    }


    private void set_char_map() {

        char first_char='A';
        for (int i = 0; i <6 ; i++) {
            char_map.put(first_char,i);
            first_char++;
        }
    }
    public Character enter_char(char input)
    {
        Character res;
        input = run_char_through_switch_plug(input);
        int running_index = char_map.get(input);
        int count_index=1;
        boolean toRotate=true;
        //run char thought right side of Rotors
        for (Rotor rotor:Rotor_arr) {

            System.out.println("    Rotor number: "+count_index);
            if(toRotate)
            {
                rotor.rotate();
                toRotate = rotor.is_rotor_on_notch();

            }
            running_index = rotor.get_exit_index(running_index);
            count_index++;

        }
        System.out.println("    Reflector:");
        running_index= ref.get_exit_index(running_index);

        //run char thought left side of Rotors
        for (int i = Rotor_arr.size()-1; i >=0 ; i--) {
            System.out.println("    Rotor number: "+(i+1));
            running_index = Rotor_arr.get(i).get_exit_index_from_left(running_index);

        }
        res = reverse_char_map.get(running_index);
        res =run_char_through_switch_plug(res);
        System.out.println("output char: "+res);
        return res;
    }


    private char run_char_through_switch_plug(char input) {
        if(switch_plug.get(input)!=null)
        {
            input =switch_plug.get(input);
        }
        return input;
    }


    public String enter_string(String input) {
        String res=new String();
        for (int i = 0; i <input.length() ; i++) {
            res+=this.enter_char(input.charAt(i));
        }
    return res;

    }
}
