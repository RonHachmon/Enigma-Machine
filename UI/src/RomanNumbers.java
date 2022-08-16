
public enum RomanNumbers {
    I("press 1"),
    II("press 2"),
    III("press 3"),
    IV("press 4"),
    V("press 5");

    public static final int SIZE = values().length;
    //public static final int PIVOT = 2;
    private String message;

    RomanNumbers(String message) {
        this.message = message;
    }

    public String toString() {
        return this.message;
    }
}
