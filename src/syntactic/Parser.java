package syntactic;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;
import utils.CompilerException;

public class Parser {
    private Token currentToken;
    private Token lastToken;

    private Lexer lexer;

    public void throwCompilerException(String message) throws Exception {
        throw new CompilerException(message, this.lexer.getLine());
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public Token getLastToken() {
        return lastToken;
    }

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        advance();
    }

    private void advance() throws Exception {
        this.currentToken = this.lexer.scan();
    }

    private void eat(Tag tag) throws Exception {
        if (this.currentToken.getTag().toString().equals(tag.toString())) {
            System.out.println(this.currentToken.toString());
            advance();
        } else {
            throw new CompilerException("(EAT) Erro na leitura do token: " +
                    this.currentToken.toString() + "\n Token esperado: " + tag.toString(),
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
        declList();
        stmtList();
        eat(Tag.END);
        eat(Tag.DOT);
    }

    // decl-list ::= decl ";" { decl ";"} // TO DO: VALIDAR REGRA DE REPETIÇÃO
    public void declList() throws Exception {
        lastToken = this.currentToken;
        try {
            decl();
            eat(Tag.SEMI_COLON);
            declList();
        } catch (CompilerException e) {
            return;
        }
    }

    // decl ::= ident-list is type
    public void decl() throws Exception {
        try {
            identList();
            eat(Tag.IS);
            type();
        } catch (CompilerException e) {
            throw new CompilerException("(DECL) Token não esperado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        }
    }

    // ident-list ::= identifier {"," identifier} // TO DO: VALIDAR REGRA DE
    // REPETIÇÃO
    public void identList() throws Exception {
        this.lastToken = this.currentToken;
        try {
            identifier();
            eat(Tag.COMMA);
            identList();
        } catch (CompilerException e) {
            return;
        }
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
                throw new CompilerException("(TYPE) Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // stmt-list ::= stmt {";" stmt} // TO DO: VALIDAR REGRA DE REPETIÇÃO
    public void stmtList() throws Exception {
        try {
            stmt();
            if (this.currentToken.getTag() == Tag.SEMI_COLON) {
                eat(Tag.SEMI_COLON);
            }
            stmtList();
        } catch (CompilerException e) {
            return;
        }
    }

    // stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt |
    // write-stmt
    public void stmt() throws Exception {
        // Tratamento para a saída de decl-list para stmt-list
        if (this.lastToken != null && this.lastToken.getTag().toString().equals(Tag.ID.toString())) {
            // assign-stmt ::= identifier(eat) "=" simple_expr
            eat(Tag.ASSIGN);
            simpleExpr();
            lastToken = null;
            return;
        }

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
                throw new CompilerException("(STMT) Token não esperado: " + this.currentToken.toString(),
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
                throw new CompilerException("(IF_STMT_LINE) Token não esperado: " + this.currentToken.toString(),
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
            case CONST_FLOAT:
            case CONST_CHAR:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
                simpleExpr();
                break;
            case LITERAL:
                literal();
                break;
            default:
                throw new CompilerException("(WRITABLE) Token não esperado: " + this.currentToken.toString(),
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
        switch (this.currentToken.getTag()) {
            case EQUALS:
            case GREATER:
            case GREATER_EQ:
            case LOWER:
            case LOWER_EQ:
            case NOT_EQUALS:
                relop();
                simpleExpr();
                break;
            default:
                break;
        }
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
            case CONST_FLOAT:
            case CONST_CHAR:
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
            case CONST_FLOAT:
            case CONST_CHAR:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
            case AND:
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
            case CONST_FLOAT:
            case CONST_CHAR:
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
                throw new CompilerException("(FACTOR-A) Token não esperado: " + this.currentToken.toString(),
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
            case CONST_FLOAT:
            case CONST_CHAR:
                constant();
                break;
            case OPEN_PAR:
                eat(Tag.OPEN_PAR);
                expression();
                eat(Tag.CLOSE_PAR);
                break;

            default:
                throw new CompilerException("(FACTOR) Token não esperado: " + this.currentToken.toString(),
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
                throw new CompilerException("(RELOP) Token não esperado: " + this.currentToken.toString(),
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
                throw new CompilerException("(ADDOP) Token não esperado: " + this.currentToken.toString(),
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
                throw new CompilerException("(MULOP) Token não esperado: " + this.currentToken.toString(),
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
                throw new CompilerException("(CONSTANT) Token não esperado: " + this.currentToken.toString(),
                        this.lexer.getLine());
        }
    }

    // digit ::= [0-9]
    public void digit() throws Exception {
        eat(Tag.INT);
    }

    // carac ::= um dos caracteres ASCII
    public void carac() throws Exception {
        eat(Tag.ID);
    }

    // caractere ::= um dos caracteres ASCII, exceto quebra de linha
    public void caractere() throws Exception {
        if (this.currentToken.toString() == "\n") {
            throw new CompilerException("(CARACTERE) Token não esperado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        } else {
            eat(Tag.ID);
        }
    }

    public boolean isIntegerConst() {
        String currentToken = this.currentToken.getTag().toString();
        return currentToken.equals(Tag.INT.toString());
    }

    public void integerConstLine() throws Exception {
        digit();
    }

    // integer_const ::= digit+ // TO DO: VALIDAR REGRA DE REPETIÇÃO
    public void integerConst() throws Exception {

        boolean condition = isIntegerConst();
        if (!condition) {
            throw new CompilerException("(INTEGER_CONST) Inteiro mal formatado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        }

        while (isIntegerConst()) {
            integerConstLine();
        }
    }

    // float_const ::= digit+ "." digit+ // TO DO: VALIDAR REGRA DE REPETIÇÃO
    public void floatConst() throws Exception {
        boolean condition;

        condition = isIntegerConst();
        if (!condition) {
            throw new CompilerException(
                    "(FLOAT_CONST) Decimal (parte inteira) mal formatado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        }

        while (isIntegerConst()) {
            integerConstLine();
        }

        eat(Tag.DOT);

        condition = isIntegerConst();
        if (!condition) {
            throw new CompilerException(
                    "(FLOAT_CONST) Decimal (parte fracionada) mal formatado: " + this.currentToken.toString(),
                    this.lexer.getLine());
        }

        while (isIntegerConst()) {
            integerConstLine();
        }
    }

    // char_const ::= "'" carac "'"
    public void charConst() throws Exception {
        eat(Tag.SINGLE_QUOTE);
        carac();
        eat(Tag.SINGLE_QUOTE);
    }

    // literal ::= "{" caractere* "}" // TO DO: VALIDAR REGRA DE REPETIÇÃO
    public void literal() throws Exception {
        eat(Tag.LITERAL);
    }

    // identifier ::= letter (letter | digit | "_")* // TO DO: VALIDAR REGRA DE
    // REPETIÇÃO
    public void identifier() throws Exception {
        eat(Tag.ID);
    }

    // letter ::= [A-Za-z] // TO DO
    public void letter() throws Exception {
        eat(Tag.ID);
    }
}