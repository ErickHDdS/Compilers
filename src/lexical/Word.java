package lexical;

public class Word extends Token {

    private String lexeme;

    // SYMBOLS
    public static final Word DOT = new Word(".", Tag.DOT);
    public static final Word SEMI_COLON = new Word(";", Tag.SEMI_COLON);
    public static final Word COLON = new Word(":", Tag.COLON);
    public static final Word COMMA = new Word(",", Tag.COMMA);
    public static final Word OPEN_PAR = new Word("(", Tag.OPEN_PAR);
    public static final Word CLOSE_PAR = new Word(")", Tag.CLOSE_PAR);
    public static final Word OPEN_BRACKET = new Word("{", Tag.OPEN_BRACKET);
    public static final Word CLOSE_BRACKET = new Word("}", Tag.CLOSE_BRACKET);

    // OPERATORS
    public static final Word ASSIGN = new Word("=", Tag.ASSIGN);
    public static final Word NOT = new Word("!", Tag.NOT);
    public static final Word EQUALS = new Word("==", Tag.EQUALS);
    public static final Word GREATER = new Word(">", Tag.GREATER);
    public static final Word GREATER_EQ = new Word(">=", Tag.GREATER_EQ);
    public static final Word LOWER = new Word("<", Tag.LOWER);
    public static final Word LOWER_EQ = new Word("<=", Tag.LOWER_EQ);
    public static final Word NOT_EQUALS = new Word("!=", Tag.NOT_EQUALS);
    public static final Word ADD = new Word("+", Tag.ADD);
    public static final Word SUB = new Word("-", Tag.SUB);
    public static final Word OR = new Word("||", Tag.OR);
    public static final Word MUL = new Word("*", Tag.MUL);
    public static final Word DIV = new Word("/", Tag.DIV);
    public static final Word AND = new Word("&&", Tag.AND);
    public static final Word UNDERLINE = new Word("_", Tag.UNDERLINE);

    // KEYWORDS
    public static final Word PROGRAM = new Word("program", Tag.PROGRAM);
    public static final Word BEGIN = new Word("begin", Tag.BEGIN);
    public static final Word END = new Word("end", Tag.END);
    public static final Word IS = new Word("is", Tag.IS);
    public static final Word INT = new Word("int", Tag.INT);
    public static final Word FLOAT = new Word("float", Tag.FLOAT);
    public static final Word CHAR = new Word("char", Tag.CHAR);
    public static final Word IF = new Word("if", Tag.IF);
    public static final Word ELSE = new Word("else", Tag.ELSE);
    public static final Word THEN = new Word("then", Tag.THEN);
    public static final Word REPEAT = new Word("repeat", Tag.REPEAT);
    public static final Word UNTIL = new Word("until", Tag.UNTIL);
    public static final Word WHILE = new Word("while", Tag.WHILE);
    public static final Word DO = new Word("do", Tag.DO);
    public static final Word READ = new Word("read", Tag.READ);
    public static final Word WRITE = new Word("write", Tag.WRITE);

    public Word(String lexeme, Tag type) {
        super(type);
        this.lexeme = lexeme;
    }

    public String toString() {
        return "Lexeme: " + this.lexeme + " | Tag:" + this.getTag();
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
