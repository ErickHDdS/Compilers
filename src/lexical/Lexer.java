package lexical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import symbolTable.Table;
import utils.CompilerException;

public class Lexer {
    private static final int EOF = 65535;
    private static final int TEN = 10;

    public static int line = 1; // contador de linhas
    private char currentChar = ' '; // caractere lido do arquivo
    private FileReader file; // arquivo fonte

    private Table words = new Table();

    /* Método para inserir palavras reservadas na tabela de símbolos */
    private void reserve(Word w) {
        words.put(w.getLexeme(), w.getType());
    }

    public Lexer(String file) throws FileNotFoundException {
        try {
            this.file = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            throw e;
        }

    }

    /* Lê o próximo caractere do arquivo */
    private void readCurrentChar() throws IOException {
        this.currentChar = (char) file.read();
    }

    /* Lê o próximo caractere do arquivo e verifica se é igual a c */
    private boolean readCurrentChar(char c) throws IOException {
        readCurrentChar();
        if (currentChar != c) {
            return false;
        }
        this.currentChar = ' ';
        return true;
    }

    public Token scan() throws Exception {
        // TRASH
        for (;; readCurrentChar()) {

            if (this.currentChar == ' ' || this.currentChar == '\t' || this.currentChar == '\r'
                    || this.currentChar == '\b')
                continue;

            else if (currentChar == '\n')
                this.line++;
            else
                break;

        }

        // NUMBERS
        if (Character.isDigit(this.currentChar)) {
            boolean isIntegerValue = true;
            int decimalValue = 0;
            int integerValue = 0;
            int dividerDecimalValue = TEN;
            do {
                if (isIntegerValue) {
                    integerValue = TEN * integerValue + Character.digit(this.currentChar, integerValue);
                } else {
                    decimalValue = integerValue
                            + (Character.digit(this.currentChar, integerValue) / dividerDecimalValue);
                    dividerDecimalValue *= TEN;
                }

                if (readCurrentChar('.')) {
                    isIntegerValue = false;
                }

            } while (Character.isDigit(this.currentChar));

            return new Number(integerValue + decimalValue);
        }

        // IDENTIFIERS
        if (Character.isLetter(this.currentChar)) {
            StringBuffer str = new StringBuffer();
            do {
                str.append(this.currentChar);
                readCurrentChar();
            } while (Character.isLetterOrDigit(this.currentChar) || this.currentChar == '_');

            String s = str.toString();
            Tag t = words.findTag(s);

            if (t != Tag.ID)
                return new Word(s, t); // palavra já existe na HashTable
            else {
                Word w = new Word(s, Tag.ID);
                words.put(s, w.getType());
                return w;
            }
        }

        // OPERATORS AND SYMBOLS
        switch (this.currentChar) {
            case '=':
                if (readCurrentChar('='))
                    return new Token(Tag.EQUALS);
                else
                    return new Token(Tag.ASSIGN);
            case '!':
                if (readCurrentChar('='))
                    return new Token(Tag.NOT_EQUALS);
                else
                    return new Token(Tag.NOT);
            case '>':
                if (readCurrentChar('='))
                    return new Token(Tag.GREATER_EQ);
                else
                    return new Token(Tag.GREATER);
            case '<':
                if (readCurrentChar('='))
                    return new Token(Tag.LOWER_EQ);
                else
                    return new Token(Tag.LOWER);
            case '+':
                readCurrentChar();
                return new Token(Tag.ADD);
            case '-':
                readCurrentChar();
                return new Token(Tag.SUB);
            case '*':
                readCurrentChar();
                return new Token(Tag.MUL);
            case '/':
                readCurrentChar();
                if (readCurrentChar('*')) {
                    readCurrentChar();
                    boolean comment = true;
                    while (comment) {
                        readCurrentChar();
                        if (readCurrentChar('\n'))
                            this.line++;
                        if (readCurrentChar('*'))
                            if (readCurrentChar('/'))
                                comment = false;
                        if (readCurrentChar((char) EOF)) {
                            new CompilerException("Comentário aberto.", this.line);
                            return new Token(Tag.END_OF_FILE);
                        }
                    }
                }
                return new Token(Tag.DIV);
            case '&':
                if (readCurrentChar('&'))
                    return new Token(Tag.AND);
                else
                    return new Token(Tag.INVALID_TOKEN);
            case '|':
                if (readCurrentChar('|'))
                    return new Token(Tag.OR);
                else
                    return new Token(Tag.INVALID_TOKEN);
            case '.':
                readCurrentChar();
                return new Token(Tag.DOT);
            case ';':
                readCurrentChar();
                return new Token(Tag.COMMA);
            case '(':
                readCurrentChar();
                return new Token(Tag.OPEN_PAR);
            case ')':
                readCurrentChar();
                return new Token(Tag.CLOSE_PAR);
            case '{':
                Boolean readString = true;
                StringBuilder str = new StringBuilder();

                while (readString) {
                    if (!readCurrentChar('}'))
                        str.append(this.currentChar);
                    else
                        readString = false;
                    if (readCurrentChar((char) EOF)) {
                        new CompilerException("String mal formatada.", this.line);
                        return new Token(Tag.END_OF_FILE);
                    }
                }
                String s = str.toString();
                readCurrentChar();
                return new Word(s, Tag.LITERAL);
        }

        // OTHERS
        Token t = new Token(Tag.NOT_EXPECTED);
        this.currentChar = ' ';
        return t;
    }
    // TO DO:
    // realizar a leitura de tokens

}
