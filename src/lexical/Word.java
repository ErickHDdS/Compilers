package lexical;

public class Word extends Token {

    private String lexeme = "";

    public static final Word and = new Word("&&", Tag.AND);

    public Word(String s, Tag tag) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "" + this.lexeme;
    }
}
