import jaxb_classes.CTEPositioning;
import jaxb_classes.CTERotor;

import java.util.*;

public class Rotor {
    public final List<Line> line_array=new ArrayList<Line>() ;
    public int notch_index;
    public int rotator_index = 0;

    public void rotate(){
        rotator_index=(rotator_index+1)%line_array.size();

    }

    public boolean is_rotor_on_notch(){
        if(notch_index==rotator_index)
        {
            System.out.println("        notch");
        }
        return notch_index==rotator_index;
    }


    public int get_exit_index_from_right(int index) {

        int real_index=(index+rotator_index)%line_array.size();
        char right_char=line_array.get(real_index).right_char;
        System.out.println("        Right char = "+right_char);

        int rotator_exit_index=0;
        for (Line current_line:line_array) {
            if(current_line.left_char==right_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= line_array.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;

    }
    public int get_exit_index_from_left(int index) {

        int real_index=(index+rotator_index)%line_array.size();
        char left_char=line_array.get(real_index).left_char;
        System.out.println("        Left char = "+left_char);

        int rotator_exit_index=0;
        for (Line current_line:line_array) {
            if(current_line.right_char==left_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= line_array.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;

    }

    public static Rotor create_rotor_from_XML(CTERotor xml_rotor) {
        Rotor rotor =new Rotor();
        rotor.notch_index=xml_rotor.getNotch()-1;
        for (CTEPositioning positioning : xml_rotor.getCTEPositioning()) {
            //check validity of char
            // 1. char included in char_set of the machine options:
            //      a.delegate with the machine as the listener
            //      b.check validity after Rotor is created on the machine
            //      c.send char_set as parameter (probably best option)
            // 2. char not already exist on rotor.
            Line current_line=new Line(positioning.getRight(),positioning.getLeft());
            rotor.line_array.add(current_line);
        }
        //3. check all char_set is included on rotor
        return rotor;
    }
    public void set_starting_index(int starting_index)
    {
        this.rotator_index=starting_index;
    }

    public String toString() {
        String res=new String();
        for (Line line:line_array)
        {
            res+=line.getLeft_char()+" , "+line.getRight_char()+"\n";
        }
        return res;
    }



}
