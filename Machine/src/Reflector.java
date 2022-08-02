import jaxb_classes.CTEReflect;
import jaxb_classes.CTEReflector;


import java.util.ArrayList;
import java.util.List;

public class Reflector {
    public static final int EMPTY = -1;
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
    public Reflector(int size)
    {
        for (int i = 0; i < size; i++) {
            int_arr.add(EMPTY);
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
    public static Reflector create_reflector_from_XML(CTEReflector xml_reflector, int size)
    {
        Reflector reflector =new Reflector(size);
        xml_reflector.getCTEReflect().forEach(reflect -> {
            if(reflect.getInput()>size||reflect.getOutput()>size)
            {
                //throw outOfBound
            }
            if(reflect.getInput()<=0||reflect.getOutput()<=0)
            {
                //throw outOfBound
            }
            if( reflector.int_arr.set(reflect.getInput() - 1, reflect.getInput() - 1)!= EMPTY)
            {
                //throw duplicate_index
            }
            if( reflector.int_arr.set(reflect.getOutput() - 1, reflect.getInput() - 1)!= EMPTY)
            {
                //throw duplicate_index
            }
        });
        return reflector;
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
