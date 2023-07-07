package lexical;

public class Token {

    private final Tag tag;
    private Types typeOfTag;

    public Token(Tag tag) {
        this.tag = tag;
        this.typeOfTag = null;
    }

    public String toString() {
        return "" + this.tag.toString();
    }

    public Tag getTag() {
        return tag;
    }

    public void setTypeOfTag(Types typeOfTag) {
        this.typeOfTag = typeOfTag;
    }

    public Types getTypeOfTag() {
        return typeOfTag;
    }
}
