package primeengine;

import utils.ListPermutation;
import utils.Permutation;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String input="CBB";


        /*Permutation permutation =new Permutation("ABCDEFGHIJKLMNOPQRSTUVWXYZ");*/
        Permutation permutation =new Permutation("ABC");
        System.out.println(permutation.increasePermutation(4,input));
        if(permutation.getOverFlow())
        {
            System.out.println("Ywes");
        }
/*        characters.forEach(character -> System.out.print(character));
        List<Integer> allRotors=new ArrayList<>();
        allRotors.add(0);
        allRotors.add(1);
        allRotors.add(2);
        allRotors.add(3);
        allRotors.add(4);*/


/*        List<Integer> selectedRotors=new ArrayList<>();

        selectedRotors.add(0);
        selectedRotors.add(2);
        selectedRotors.add(4);

        ListPermutation listPermutation=new ListPermutation(selectedRotors,allRotors);
        List<Integer> integers3=new ArrayList<>();
        int count=0;
        while (!listPermutation.done())
        {
            System.out.println();
            integers3=listPermutation.increasePermutation();
            integers3.forEach(integer -> System.out.print(integer));
            count++;

        }
        System.out.println();
        System.out.println(count);*/

    }
}
