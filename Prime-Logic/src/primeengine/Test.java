package primeengine;

import utils.Permutation;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Character> characters=new ArrayList<>();

        characters.add('B');
        characters.add('B');
        characters.add('C');
        /*Permutation permutation =new Permutation("ABCDEFGHIJKLMNOPQRSTUVWXYZ");*/
        Permutation permutation =new Permutation("ABC");
        permutation.increasePermutation(4,characters);
        characters.forEach(character -> System.out.print(character));


    }
}
