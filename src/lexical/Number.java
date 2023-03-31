package lexical;

public class Number extends Token {
    public final int value;

    public Number(int value) {
        super(Tag.NUMBER);
        this.value = value;
    }

    public String toString() {
        return "" + this.value;
    }

}
