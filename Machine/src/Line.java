public class Line {
    private char leftChar;
    private char rightChar;

    public Line(char right, char left) {
        this.rightChar = right;
        this.leftChar = left;

    }


    public Line(String right, String left) {
        this.rightChar =replaceSpecialXMLchar(right).charAt(0);
        this.leftChar =replaceSpecialXMLchar(left).charAt(0);


    public void setLeftChar(char leftChar) {
        this.leftChar = leftChar;
    }

    public void setRightChar(char rightChar) {
        this.rightChar = rightChar;
    }

    public char getLeftChar() {
        return leftChar;
    }

    public char getRightChar() {
        return rightChar;
    }
    private static String replaceSpecialXMLchar(String string)
    {
        String result =  string.replace("&amp;","&")
                .replace("&lt;","<")
                .replace("&gt;","<")
                .replace("&quot;","")
                .replace("&apos;","'");
        return result;
    }
}
