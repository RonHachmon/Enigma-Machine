import java.util.*;

public class Rotor {
    public List<Line> Line_arr=new ArrayList<Line>() ;
    public int notch_index;
    public int rotator_index = 0;

    public void rotate(){
        rotator_index=(rotator_index+1)%Line_arr.size();

    }

    public boolean is_rotor_on_notch(){
        if(notch_index==rotator_index)
        {
            System.out.println("        notch");
        }
        return notch_index==rotator_index;
    }


    public int get_exit_index(int index) {

        int real_index=(index+rotator_index)%Line_arr.size();
        char right_char=Line_arr.get(real_index).right_char;
        System.out.println("        Right char = "+right_char);

        int rotator_exit_index=0;
        for (Line current_line:Line_arr) {
            if(current_line.left_char==right_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= Line_arr.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;

    }
    public int get_exit_index_from_left(int index) {

        int real_index=(index+rotator_index)%Line_arr.size();
        char left_char=Line_arr.get(real_index).left_char;
        System.out.println("        Left char = "+left_char);

        int rotator_exit_index=0;
        for (Line current_line:Line_arr) {
            if(current_line.right_char==left_char)
            {
                rotator_exit_index-=rotator_index;
                if(rotator_exit_index<0)
                {
                    rotator_exit_index+= Line_arr.size();
                }
                break;
            }
            rotator_exit_index++;
        }
        return rotator_exit_index;

    }
}
