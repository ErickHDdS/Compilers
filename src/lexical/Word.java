package lexical;

public class Word {

    public Token token;
    public Tag type;

    public Word(Token token, Tag type) {
        this.token = token;
        this.type = type;
    }

}
