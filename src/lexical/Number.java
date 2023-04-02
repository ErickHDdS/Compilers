package lexical;

public class Number extends Token {
    public final float value;

    public Number(float value) {
        super(Tag.NUMBER);
        this.value = value;
    }

    public String toString() {
        return "" + this.value;
    }

}
