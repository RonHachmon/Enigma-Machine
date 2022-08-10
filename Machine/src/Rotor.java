import jaxb_classes.CTEPositioning;
import jaxb_classes.CTERotor;

import java.util.*;

public class Rotor {
    public final List<Line> lineArray =new ArrayList<Line>() ;
    public int notch_index;
    public int rotator_index = 0;

    public void rotate(){
        rotator_index=(rotator_index+1)% lineArray.size();

    }

    public boolean is_rotor_on_notch(){
        if(notch_index==rotator_index)
        {
            System.out.println("        notch");
        }
        return notch_index==rotator_index;
    }


    public int get_exit_index_from_right(int index) {

        int real_index=(index+rotator_index)% lineArray.size();
        char right_char= lineArray.get(real_index).getRightChar();
        System.out.println("        Right char = "+right_char);

        int rotator_exit_index=0;
        for (Line current_line: lineArray) {
            if(current_line.getLeftChar()==right_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= lineArray.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;

    }
    public int get_exit_index_from_left(int index) {

        int real_index=(index+rotator_index)% lineArray.size();
        char left_char= lineArray.get(real_index).getLeftChar();
        System.out.println("        Left char = "+left_char);

        int rotator_exit_index=0;
        for (Line current_line: lineArray) {
            if(current_line.getRightChar() ==left_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= lineArray.size();
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
            rotor.lineArray.add(current_line);
        }
        if(rotor.lineArray.size()!=allChars.length())
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
            for(Line line: rotor.lineArray)
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

    public void setStartingIndex(int startingIndex)
    {
        this.rotator_index=startingIndex;
    }
    public void setStartingIndex(char characterToLook)
    {
        for (int i = 0; i <this.lineArray.size() ; i++) {
            if(this.lineArray.get(i).getRightChar() == characterToLook)
            {
                this.setStartingIndex(i);
                return;
            }

        }
        throw new IllegalArgumentException("starting rotor char "+characterToLook+" not on rotor")an
    }

    public String toString() {
        String res=new String();
        for (Line line: lineArray)
        {
            res+=line.getLeftChar()+" , "+line.getRightChar()+"\n";
        }
        return res;
    }



}
