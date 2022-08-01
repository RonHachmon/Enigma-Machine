import java.util.ArrayList;
import java.util.List;

public class Reflector {
    public List<Integer> int_arr= new ArrayList<>();
    public Reflector()
    {
        for (int i = 0; i <3 ; i++) {

            int_arr.add(i);
        }
        for (int i = 0; i <3 ; i++) {

            int_arr.add(i);
        }
    }
    public int get_exit_index(int index)
    {
        int value=int_arr.get(index);
        System.out.println("        Reflector value: "+value);
        for (int i = 0; i <int_arr.size() ; i++) {

            if(i!=index)
            {
                if(value==int_arr.get(i))
                {
                    System.out.println("        Reflector exit index: "+i);
                    return i;
                }
            }

        }
        return -1;

    }

    @Override
    public String toString() {
        String res=new String();
        for (int value:int_arr)
        {
            res+=value+", ";
        }
        return res;
    }
}
