package lexical;

public class Number extends Token {
    public final float value;

    public Number(float value, Tag tag) {
        super(tag);
        this.value = value;
    }

    public String toString() {
        Tag tag = this.getTag();

        if(tag == Tag.CONST_INT){
            return "value: " + (int)value + " tag: " + this.getTag();
        }
        return "value: " + this.value + " tag: " + this.getTag();
    }

}
