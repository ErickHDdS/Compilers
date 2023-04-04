package lexical;

public class Number extends Token {
    public final float value;

    public Number(float value, Tag tag) {
        super(tag);
        this.value = value;
    }

    public String toString() {
        Tag tag = this.getTag();

        if (tag == Tag.CONST_INT) {
            return "Value: " + (int) value + " Tag: " + this.getTag();
        }
        return "Value: " + this.value + " Tag: " + this.getTag();
    }

}
