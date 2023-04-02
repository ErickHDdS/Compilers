package symbolTable;

import java.util.HashMap;
import java.util.Map;

import lexical.Tag;
import lexical.Word;

public class Table {
    public Map<String, Tag> table;

    public Table() {
        this.table = new HashMap<String, Tag>();

        // SYMBOLS
        this.table.put(".", Tag.DOT);
        this.table.put(";", Tag.SEMI_COLON);
        this.table.put(",", Tag.COMMA);
        this.table.put("(", Tag.OPEN_PAR);
        this.table.put(")", Tag.CLOSE_PAR);
        this.table.put("{", Tag.OPEN_KEY);
        this.table.put("}", Tag.CLOSE_KEY);

        // OPERATORS
        this.table.put("=", Tag.ASSIGN);
        this.table.put("!", Tag.NOT);
        this.table.put("==", Tag.EQUALS);
        this.table.put(">", Tag.GREATER);
        this.table.put(">=", Tag.GREATER_EQ);
        this.table.put("<", Tag.LOWER);
        this.table.put("<=", Tag.LOWER_EQ);
        this.table.put("!=", Tag.NOT_EQUALS);
        this.table.put("+", Tag.ADD);
        this.table.put("-", Tag.SUB);
        this.table.put("||", Tag.OR);
        this.table.put("*", Tag.MUL);
        this.table.put("/", Tag.DIV);
        this.table.put("&&", Tag.AND);
        this.table.put("_", Tag.UNDERLINE);

        // KEYWORDS
        this.table.put("program", Tag.PROGRAM);
        this.table.put("begin", Tag.BEGIN);
        this.table.put("end", Tag.END);
        this.table.put("is", Tag.IS);
        this.table.put("int", Tag.INT);
        this.table.put("float", Tag.FLOAT);
        this.table.put("char", Tag.CHAR);
        this.table.put("if", Tag.IF);
        this.table.put("else", Tag.ELSE);
        this.table.put("then", Tag.THEN);
        this.table.put("repeat", Tag.REPEAT);
        this.table.put("until", Tag.UNTIL);
        this.table.put("while", Tag.WHILE);
        this.table.put("do", Tag.DO);
        this.table.put("read", Tag.READ);
        this.table.put("write", Tag.WRITE);

    }

    public Tag findTag(String str) {
        return this.containsString(str) ? this.table.get(str) : Tag.ID;
    }

    public boolean containsString(String str) {
        return this.table.containsKey(str);
    }

    public void put(String lexeme, Tag tag) {
        this.table.put(lexeme, tag);
    }
}