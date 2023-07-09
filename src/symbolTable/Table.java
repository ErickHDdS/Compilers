package symbolTable;

import java.util.HashMap;
import java.util.Map;

import lexical.Lexer;
import lexical.Tag;
import lexical.Types;
import lexical.Word;
import utils.CompilerException;

public class Table {
    public Map<String, Word> table;

    public Table() {
        this.table = new HashMap<String, Word>();

        // SYMBOLS
        this.table.put(".", Word.DOT);
        this.table.put(";", Word.SEMI_COLON);
        this.table.put(",", Word.COMMA);
        this.table.put("(", Word.OPEN_PAR);
        this.table.put(")", Word.CLOSE_PAR);
        this.table.put("{", Word.OPEN_BRACKET);
        this.table.put("}", Word.CLOSE_BRACKET);

        // OPERATORS
        this.table.put("=", Word.ASSIGN);
        this.table.put("!", Word.NOT);
        this.table.put("==", Word.EQUALS);
        this.table.put(">", Word.GREATER);
        this.table.put(">=", Word.GREATER_EQ);
        this.table.put("<", Word.LOWER);
        this.table.put("<=", Word.LOWER_EQ);
        this.table.put("!=", Word.NOT_EQUALS);
        this.table.put("+", Word.ADD);
        this.table.put("-", Word.SUB);
        this.table.put("||", Word.OR);
        this.table.put("*", Word.MUL);
        this.table.put("/", Word.DIV);
        this.table.put("&&", Word.AND);
        this.table.put("_", Word.UNDERLINE);

        // KEYWORDS
        this.table.put("program", Word.PROGRAM);
        this.table.put("begin", Word.BEGIN);
        this.table.put("end", Word.END);
        this.table.put("is", Word.IS);
        this.table.put("int", Word.INT);
        this.table.put("float", Word.FLOAT);
        this.table.put("char", Word.CHAR);
        this.table.put("if", Word.IF);
        this.table.put("else", Word.ELSE);
        this.table.put("then", Word.THEN);
        this.table.put("repeat", Word.REPEAT);
        this.table.put("until", Word.UNTIL);
        this.table.put("while", Word.WHILE);
        this.table.put("do", Word.DO);
        this.table.put("read", Word.READ);
        this.table.put("write", Word.WRITE);

    }

    public Word findToken(String str) {
        return this.containsString(str) ? this.table.get(str) : new Word(str, Tag.ID);
    }

    public boolean containsString(String str) {
        return this.table.containsKey(str);
    }

    public void put(String lexeme, Word w) {
        this.table.put(lexeme, w);
    }

    public void setSetTypeOfTag(Word word, Types setTypeOfTag) {
        word.setTypeOfTag(setTypeOfTag);
        put(word.getLexeme(), word);
    }

    public Types getElementTypeByKey(String key) {
        // try {
        Word w = this.table.get(key);
        return w.getTypeOfTag();
        // }
        // catch (Exception e) {

        // // TO DO: TRATAR ERRO QUANDO NÃO POSSUI O ITEM OU O TIPO DELE NA TABELA
        // System.out.println("ERRO:" + key);
        // }
        // return null;
    }

    public void printTable() {
        for (Map.Entry<String, Word> entry : this.table.entrySet()) {
            String key = entry.getKey();
            Word w = entry.getValue();
            System.out.println("{" + key + "}" + " --> " + w.getLexeme() + "|" + w.getTag() + "|" + w.getTypeOfTag());
        }
    }

}