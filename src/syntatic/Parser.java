package syntatic;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;
import utils.CompilerException;

public class Parser {
    private Token currentToken;
    private Token lastToken;
    private Lexer lexer;

    private void advance() throws Exception {
        this.currentToken = this.lexer.scan();
    }

    private void eat(Tag tag) throws Exception {
        if (currentToken.getTag() == tag) {
            System.out.println("EAT: " + this.currentToken.toString());
            advance();
        } else {
            throw new CompilerException("Erro na leitura do token: " + this.currentToken.toString(),
                    this.lexer.getLine());
        }
    }

    // program’ ::= program $
    public void programLine() throws Exception {
        program();
        eat(Tag.END_OF_FILE);
    }

    // program ::= program identifier begin [decl-list] stmt-list end "."
    public void program() throws Exception {
        eat(Tag.PROGRAM);
        identifier();
        eat(Tag.BEGIN);
        declList(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 0, 1 OU N VEZES
        stmtList();
        eat(Tag.END);
        eat(Tag.DOT);
    }

    // decl-list ::= decl ";" { decl ";"}
    public void declList() throws Exception {
        decl();
        eat(Tag.SEMI_COLON);
        declList(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
        eat(Tag.SEMI_COLON); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
    }

    // decl ::= ident-list is type
    public void decl() throws Exception {
        identList();
        eat(Tag.IS);
        type();
    }

    // ident-list ::= identifier {"," identifier}
    public void identList() throws Exception {
        identifier();
        eat(Tag.COMMA); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
        identifier(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
    }

    // type ::= int | float | char
    public void type() throws Exception {
        switch (this.currentToken.getTag()) {
            case INT:
                eat(Tag.INT);
                break;
            case FLOAT:
                eat(Tag.FLOAT);
                break;
            case CHAR:
                eat(Tag.CHAR);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // stmt-list ::= stmt {";" stmt}
    public void stmtList() throws Exception {
        stmt();
        eat(Tag.SEMI_COLON); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
        stmt(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
    }

    // stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt |
    // write-stmt
    public void stmt() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
                assignStmt();
                break;
            case IF:
                ifStmt();
                break;
            case WHILE:
                whileStmt();
                break;
            case REPEAT:
                repeatStmt();
                break;
            case READ:
                readStmt();
                break;
            case WRITE:
                writeStmt();
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // assign-stmt ::= identifier "=" simple_expr
    public void assignStmt() throws Exception {
        identifier();
        eat(Tag.ASSIGN);
        simpleExpr();
    }

    // if-stmt ::= if condition then stmt-list if-stmt’
    public void ifStmt() throws Exception {
        eat(Tag.IF);
        condition();
        eat(Tag.THEN);
        stmtList();
        ifStmtLine();
    }

    // if-stmt’ ::= end | else stmt-list end
    public void ifStmtLine() throws Exception {
        switch (this.currentToken.getTag()) {
            case END:
                eat(Tag.END);
                break;
            case ELSE:
                eat(Tag.ELSE);
                stmtList();
                eat(Tag.END);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // condition ::= expression
    public void condition() throws Exception {
        expression();
    }

    // repeat-stmt ::= repeat stmt-list stmt-suffix
    public void repeatStmt() throws Exception {
        eat(Tag.REPEAT);
        stmtList();
        stmtSuffix();
    }

    // stmt-suffix ::= until condition
    public void stmtSuffix() throws Exception {
        eat(Tag.UNTIL);
        condition();
    }

    // while-stmt ::= stmt-prefix stmt-list end
    public void whileStmt() throws Exception {
        stmtPrefix();
        stmtList();
        eat(Tag.END);
    }

    // stmt-prefix ::= while condition do
    public void stmtPrefix() throws Exception {
        eat(Tag.WHILE);
        ;
        condition();
        eat(Tag.DO);
    }

    // read-stmt ::= read "(" identifier ")"
    public void readStmt() throws Exception {
        eat(Tag.READ);
        eat(Tag.OPEN_PAR);
        identifier();
        eat(Tag.CLOSE_PAR);
    }

    // write-stmt ::= write "(" writable ")"
    public void writeStmt() throws Exception {
        eat(Tag.WRITE);
        eat(Tag.OPEN_PAR);
        writable();
        eat(Tag.CLOSE_PAR);
    }

    // writable ::= simple-expr | literal
    public void writable() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
                simpleExpr();
                break;
            case OPEN_BRACKET:
                literal();
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // expression ::= simple-expr expression’
    public void expression() throws Exception {
        simpleExpr();
        expressionLine();
    }

    // expression’ ::= relop simple-expr
    public void expressionLine() throws Exception {
        relop();
        simpleExpr();
    }

    // simple-expr ::= term simple-expr’
    public void simpleExpr() throws Exception {
        term();
        simpleExprLine();
    }

    // simple-expr’ ::= simple-expr addop simple-expr’ | λ
    public void simpleExprLine() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
                simpleExpr();
                addop();
                simpleExprLine();
                break;
            default:
                break;
        }
    }

    // term ::= factor-a term’
    public void term() throws Exception {
        factorA();
        termLine();
    }

    // term’ ::= mulop factor-a term’ | λ
    public void termLine() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
                mulop();
                factorA();
                termLine();
                break;
            default:
                break;
        }
    }

    // factor-a ::= factor |"!" factor | "-" factor
    public void factorA() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case SINGLE_QUOTE:
            case OPEN_PAR:
                factor();
                break;
            case NOT:
                eat(Tag.NOT);
                factor();
                break;
            case SUB:
                eat(Tag.SUB);
                factor();
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // factor ::= identifier | constant | "(" expression ")"
    public void factor() throws Exception {
        switch (this.currentToken.getTag()) {
            case ID:
                identifier();
                break;
            case CONST_INT:
                constant();
                break;
            case OPEN_PAR:
                eat(Tag.OPEN_PAR);
                expression();
                eat(Tag.CLOSE_PAR);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
    public void relop() throws Exception {
        switch (this.currentToken.getTag()) {
            case EQUALS:
                eat(Tag.EQUALS);
                break;
            case GREATER:
                eat(Tag.GREATER);
                break;
            case GREATER_EQ:
                eat(Tag.GREATER_EQ);
                break;
            case LOWER:
                eat(Tag.LOWER);
                break;
            case LOWER_EQ:
                eat(Tag.LOWER_EQ);
                break;
            case NOT_EQUALS:
                eat(Tag.NOT_EQUALS);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // addop ::= "+" | "-" | "||"
    public void addop() throws Exception {
        switch (this.currentToken.getTag()) {
            case ADD:
                eat(Tag.ADD);
                break;
            case SUB:
                eat(Tag.SUB);
                break;
            case OR:
                eat(Tag.OR);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // mulop ::= "*" | "/" | "&&"
    public void mulop() throws Exception {
        switch (this.currentToken.getTag()) {
            case MUL:
                eat(Tag.MUL);
                break;
            case DIV:
                eat(Tag.DIV);
                break;
            case AND:
                eat(Tag.AND);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // constant ::= integer_const | float_const | char_const
    public void constant() throws Exception {
        switch (this.currentToken.getTag()) {
            case CONST_INT:
                eat(Tag.CONST_INT);
                break;
            case CONST_FLOAT:
                eat(Tag.CONST_FLOAT);
                break;
            case CONST_CHAR:
                eat(Tag.CONST_CHAR);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // digit ::= [0-9]
    public void digit() throws Exception {
        eat(Tag.CONST_INT);
    }

    // carac ::= um dos caracteres ASCII
    public void carac() throws Exception {
        eat(Tag.CONST_CHAR);
    }

    // caractere ::= um dos caracteres ASCII, exceto quebra de linha
    public void caractere() throws Exception {
        if (this.currentToken.toString() == "\n") {
            throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        } else {
            eat(Tag.CONST_CHAR);
        }
    }

    // integer_const ::= digit+
    public void integerConst() throws Exception {
        digit(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
    }

    // float_const ::= digit+ "." digit+
    public void floatConst() throws Exception {
        digit(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
        eat(Tag.DOT);
        digit(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 1 OU N VEZES
    }

    // char_const ::= "'" carac "'"
    public void charConst() throws Exception {
        eat(Tag.SINGLE_QUOTE);
        carac();
        eat(Tag.SINGLE_QUOTE);
    }

    // literal ::= "{" caractere* "}"
    public void literal() throws Exception {
        eat(Tag.OPEN_BRACKET);
        caractere(); // TO DO: TRATAR ESSE ARGUMENTO PARA SER LIDO, 0, 1 OU N VEZES
        eat(Tag.CLOSE_BRACKET);
    }

    // identifier ::= letter (letter | digit | "_")*
    public void identifier() throws Exception {
        letter();

        switch (this.currentToken.getTag()) { // TO DO: TRATAR ESSE SWITCH PARA SER LIDO, 0, 1 OU N VEZES
            case ID:
                letter();
                break;
            case CONST_INT:
                digit();
                break;
            case UNDERLINE:
                eat(Tag.UNDERLINE);
                break;
            default:
                throw new CompilerException("Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // letter ::= [A-Za-z]
    public void letter() throws Exception {
        eat(Tag.ID);

    }
}
