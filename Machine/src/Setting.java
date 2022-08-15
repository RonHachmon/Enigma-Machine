import java.util.Collections;

public class Setting {

    // sperated by index
    // 0 - rotors
    // 1 - starting index for rotor
    // 2 - reflector
    // 3 plug
    //EXAMPLE :    0      1    2      3
    //        <45,27,94><AO!><III><A|Z,D|E>
    private String [] currentCodeSettings = new String [4] ;


    public String getFullMachineCode()
    {
        String code="";
        for(String string:currentCodeSettings)
        {
            if(string!=null) {
                if (string.charAt(string.length() - 1) == ',') {
                    string = string.substring(0, string.length() - 1);
                }
                code += '<' + string + '>';
            }

        }
        return code;
    }


    public void setSettingRotators(Integer [] selectedRotators)
    {
        int i=0;
        //reset
        currentCodeSettings[0]="";
        for ( i = 0; i <selectedRotators.length-1 ; i++) {

            currentCodeSettings[0]+=selectedRotators[i].toString();
            currentCodeSettings[0]+=',';

        }
        //last one without ','
        currentCodeSettings[0]+=selectedRotators[i].toString();
    }
    public void addSettingRotators(Integer oneRotor)
    {
        initializeStringIfNull(0);

        currentCodeSettings[0]+=oneRotor.toString()+',';

    }
    //gets full set of characters for example "AO!"
    public void setSettingStartingChars(String startingCharArray)
    {
        initializeStringIfNull(1);
        StringBuilder array = new StringBuilder(startingCharArray);
        array.reverse();
        currentCodeSettings[1]=array.toString();
    }

    //gets chars one by one
    public void addSettingStartingChars(String oneCharacter)
    {
        initializeStringIfNull(1);

        currentCodeSettings[1]+=oneCharacter;

    }
    public void setSettingReflector(int selectedReflector)
    {

        currentCodeSettings[2]=Setting.converteIntToRoman(selectedReflector);

    }
    public void addPlug(Character firstChar,Character secondChar)
    {
        initializeStringIfNull(3);

        currentCodeSettings[3]+=firstChar.toString()+'|'+secondChar.toString()+',';

    }

    private void initializeStringIfNull(int index) {
        if(currentCodeSettings[index]==null)
        {
            currentCodeSettings[index]="";
        }
    }

    private static String converteIntToRoman(int number)
    {
        switch (number)
        {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");

        }
    }

}
