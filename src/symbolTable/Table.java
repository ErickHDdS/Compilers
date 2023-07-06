package symbolTable;

import java.util.HashMap;
import java.util.Map;

import lexical.Tag;
import lexical.Token;

public class Table {
    public Map<String, Token> table;

    public Table() {
        this.table = new HashMap<String, Token>();

        // SYMBOLS
        this.table.put(".", new Token(Tag.DOT));
        this.table.put(";", new Token(Tag.SEMI_COLON));
        this.table.put(",", new Token(Tag.COMMA));
        this.table.put("(", new Token(Tag.OPEN_PAR));
        this.table.put(")", new Token(Tag.CLOSE_PAR));
        this.table.put("{", new Token(Tag.OPEN_BRACKET));
        this.table.put("}", new Token(Tag.CLOSE_BRACKET));

        // OPERATORS
        this.table.put("=", new Token(Tag.ASSIGN));
        this.table.put("!", new Token(Tag.NOT));
        this.table.put("==", new Token(Tag.EQUALS));
        this.table.put(">", new Token(Tag.GREATER));
        this.table.put(">=", new Token(Tag.GREATER_EQ));
        this.table.put("<", new Token(Tag.LOWER));
        this.table.put("<=", new Token(Tag.LOWER_EQ));
        this.table.put("!=", new Token(Tag.NOT_EQUALS));
        this.table.put("+", new Token(Tag.ADD));
        this.table.put("-", new Token(Tag.SUB));
        this.table.put("||", new Token(Tag.OR));
        this.table.put("*", new Token(Tag.MUL));
        this.table.put("/", new Token(Tag.DIV));
        this.table.put("&&", new Token(Tag.AND));
        this.table.put("_", new Token(Tag.UNDERLINE));

        // KEYWORDS
        this.table.put("program", new Token(Tag.PROGRAM));
        this.table.put("begin", new Token(Tag.BEGIN));
        this.table.put("end", new Token(Tag.END));
        this.table.put("is", new Token(Tag.IS));
        this.table.put("int", new Token(Tag.INT));
        this.table.put("float", new Token(Tag.FLOAT));
        this.table.put("char", new Token(Tag.CHAR));
        this.table.put("if", new Token(Tag.IF));
        this.table.put("else", new Token(Tag.ELSE));
        this.table.put("then", new Token(Tag.THEN));
        this.table.put("repeat", new Token(Tag.REPEAT));
        this.table.put("until", new Token(Tag.UNTIL));
        this.table.put("while", new Token(Tag.WHILE));
        this.table.put("do", new Token(Tag.DO));
        this.table.put("read", new Token(Tag.READ));
        this.table.put("write", new Token(Tag.WRITE));

    }

    public Token findToken(String str) {
        return this.containsString(str) ? this.table.get(str) : new Token(Tag.ID);
    }

    public boolean containsString(String str) {
        return this.table.containsKey(str);
    }

    public void put(String lexeme, Tag tag) {
        this.table.put(lexeme, new Token(tag));
    }

    public void printTable() {
        for (Map.Entry<String, Token> entry : this.table.entrySet()) {
            String key = entry.getKey();
            Token tg = entry.getValue();
            System.out.println("{" + key + "}" + " --> " + tg.getTag() + "|" + tg.getTypeOfTag());
        }
    }
}