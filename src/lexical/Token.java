package lexical;

public class Token {
    public final Tag tag;

    public Token(Tag tag) {
        this.tag = tag;
    }

    public String toString() {
        return "" + this.tag.toString();
    }
}
