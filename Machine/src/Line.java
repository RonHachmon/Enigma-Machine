public class Line {
    public char left_char;
    public char right_char;
    public Line(char right,char left)
    {
        this.right_char=right;
        this.left_char=left;

    }

    public Line(String right, String left) {
        this.right_char=right.charAt(0);
        this.left_char=left.charAt(0);
    }

    public void setLeft_char(char left_char) {
        this.left_char = left_char;
    }

    public void setRight_char(char right_char) {
        this.right_char = right_char;
    }

    public char getLeftChar() {
        return left_char;
    }

    public char getRightChar() {
        return right_char;
    }
}
