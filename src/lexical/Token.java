package lexical;

public class Token {
    private final Tag tag;

    public Token(Tag tag) {
        this.tag = tag;
    }

    public String toString() {
        return "" + this.tag.toString();
    }

    public Tag getTag() {
        return tag;
    }

}
