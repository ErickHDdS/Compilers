package lexical;

public class Token {

    private final Tag tag;
    private Tag typeOfTag;

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

    public void setTypeOfTag(Tag typeOfTag) {
        this.typeOfTag = typeOfTag;
    }

    public Tag getTypeOfTag() {
        return typeOfTag;
    }

}
