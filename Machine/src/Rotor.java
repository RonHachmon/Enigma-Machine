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

    public static Rotor createRotorFromXML(CTERotor xml_rotor, String allChars) {
        Rotor rotor =new Rotor();
        rotor.notch_index=xml_rotor.getNotch()-1;
        for (CTEPositioning positioning : xml_rotor.getCTEPositioning()) {

            checkValidChar(rotor,allChars, positioning);
            Line current_line=new Line(positioning.getRight(),positioning.getLeft());
            rotor.line_array.add(current_line);
        }
        if(rotor.line_array.size()!=allChars.length())
        {
            throw new IllegalArgumentException("not all charaters are included in the rotor");
        }
        return rotor;
    }

    private static void checkValidChar(Rotor rotor, String allChars, CTEPositioning xmlLine) {

            if(!allChars.contains(xmlLine.getRight()))
            {
                throw new IllegalArgumentException("Invalid rotor, since"+xmlLine.getRight()+"on rotor but isn't on char collection");
            }
            if(!allChars.contains(xmlLine.getLeft()))
            {
                throw new IllegalArgumentException("Invalid rotor, since '" + xmlLine.getLeft() + "' on rotor but isn't on char collection");
            }
            for(Line line: rotor.line_array)
            {
             if(xmlLine.getRight().charAt(0)==line.getRightChar())
                {
                    throw new IllegalArgumentException(xmlLine.getRight().charAt(0)+" appears twice on rotor");
                }
             if(xmlLine.getLeft().charAt(0)==line.getLeftChar())
                {
                    throw new IllegalArgumentException(xmlLine.getLeft().charAt(0)+ " appears twice on rotor");
                }
            }
    }

    public void set_starting_index(int starting_index)
    {
        this.rotator_index=starting_index;
    }

    public String toString() {
        String res=new String();
        for (Line line:line_array)
        {
            res+=line.getLeftChar()+" , "+line.getRightChar()+"\n";
        }
        return res;
    }



}
