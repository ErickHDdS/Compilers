package lexical;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import symbolTable.Table;
import utils.CompilerException;

public class Lexer {
    private static final int EOF = 65535;

    public static int line = 1; // contador de linhas

    private char currentChar = ' '; // caractere lido do arquivo
    private FileReader file; // arquivo fonte

    public Table symbolTableInfos = new Table();

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
                line++;
            else
                break;

        }

        // NUMBERS
        if (Character.isDigit(currentChar)) {
            StringBuilder lexeme = new StringBuilder();
            boolean isFloat = false;
            boolean hasDot = false;

            while (Character.isDigit(currentChar) || currentChar == '.') {
                lexeme.append((char) currentChar);

                if (currentChar == '.') {
                    readCurrentChar();
                    hasDot = true;

                    if (Character.isDigit(currentChar)) {
                        isFloat = true;
                    }
                } else {
                    readCurrentChar();
                }
            }

            if (isFloat) {
                return new Number(Float.parseFloat(lexeme.toString()), Tag.CONST_FLOAT);
            } else {
                if (hasDot) {
                    return new Token(Tag.INVALID_TOKEN);
                }
                return new Number(Integer.parseInt(lexeme.toString()), Tag.CONST_INT);
            }
        }

        // IDENTIFIERS
        if (Character.isLetter(this.currentChar)) {
            StringBuffer str = new StringBuffer();

            do {
                str.append(this.currentChar);
                readCurrentChar();
            } while (Character.isLetterOrDigit(this.currentChar) || this.currentChar == '_');

            String s = str.toString();
            Token t = symbolTableInfos.findToken(s);

            if (t.getTag() != Tag.ID)
                return new Word(s, t.getTag()); // palavra já existe na HashTable
            else {
                Word w = new Word(s, Tag.ID);
                symbolTableInfos.put(s, w.getTag());
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

                if (currentChar == '*') {
                    readCurrentChar();
                    boolean comment = true;
                    while (comment) {
                        readCurrentChar();
                        if (currentChar == '\n')
                            line++;

                        if (currentChar == '*')
                            if (readCurrentChar('/'))
                                comment = false;

                        if (currentChar == (char) EOF) {
                            throw new CompilerException("Comentário aberto.", line);
                        }
                    }
                    return scan();
                }

                if (currentChar == '/') {
                    readCurrentChar();
                    boolean comment = true;

                    while (comment) {
                        readCurrentChar();
                        if (currentChar == '\n')
                            comment = false;
                    }
                    return scan();
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
            case ',':
                readCurrentChar();
                return new Token(Tag.COMMA);
            case ';':
                readCurrentChar();
                return new Token(Tag.SEMI_COLON);

            case ':':
                readCurrentChar();
                return new Token(Tag.COLON);
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
                    if (currentChar == ((char) EOF)) {
                        new CompilerException("String mal formatada.", line);
                        return new Token(Tag.END_OF_FILE);
                    }
                }
                String s = str.toString();
                readCurrentChar();
                return new Word(s, Tag.LITERAL);

        }

        if (currentChar == '\'') {
            readCurrentChar();
            if (Character.isLetter(currentChar)) {
                char value = currentChar;
                readCurrentChar();
                if (currentChar == '\'') {
                    currentChar = ' ';
                    return new Word(Character.toString(value), Tag.CONST_CHAR);
                } else {
                    Token t = new Token(Tag.NOT_EXPECTED);
                    return t;
                }
            }
        }

        // EOF
        if (this.currentChar == ((char) EOF)) {
            return new Token(Tag.END_OF_FILE);
        }

        // OTHERS
        Token t = new Token(Tag.NOT_EXPECTED);
        this.currentChar = ' ';
        return t;
    }

    public static int getLine() {
        return line;
    }
}
