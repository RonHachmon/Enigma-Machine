package utils;

public class Utils {
    public static String convertIntToRoman(int number) {
        switch (number) {
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
    public static Integer convertRomanToInt(String number)
    {
        switch (number) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 3;
            case "V":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid Reflector,id must between 1-5 ");
        }

    }
}
