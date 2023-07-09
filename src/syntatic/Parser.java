package syntatic;

import java.util.HashMap;
import java.util.Map;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;
import lexical.Types;
import lexical.Word;
import utils.CompilerException;
import semantic.Semantic;
import symbolTable.Table;

public class Parser {
    private Word currentToken;
    private Word lastToken;
    private Tag reservedWordDoesNotHaveSemicolon;

    public Table getSymbolTable() {
        return lexer.symbolTableInfos;
    }

    private Lexer lexer;
    private boolean debug;
    private Semantic semantic;

    public Parser(Lexer lexer, boolean debug) throws Exception {
        this.lexer = lexer;
        this.debug = debug;
        this.semantic = new Semantic(lexer.symbolTableInfos);
        advance();
    }

    // TO DO: APAGAR ESTA FUNCAO
    // public void printTableWordsInDeclaration() {
    // for (Map.Entry<String, Word> entry : this.wordsInDeclaration.entrySet()) {
    // String key = entry.getKey();
    // Word w = entry.getValue();
    // System.out.println("{" + key + "}" + " --> " + w.getLexeme() + "|" +
    // w.getTag() + "|" + w.getTypeOfTag());
    // }
    // }

    public void putWordInSymbolTable(String key, Word word) {
        lexer.symbolTableInfos.put(key, word);
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void throwCompilerException(String message) throws Exception {
        throw new CompilerException(message, Lexer.getLine());
    }

    private void advance() throws Exception {
        this.currentToken = this.lexer.scan();
    }

    private void eat(Tag tag) throws Exception {
        boolean isCurrentTokenEqualTag = this.currentToken.getTag().toString().equals(tag.toString());
        if (isCurrentTokenEqualTag) {
            advance();
        } else {
            String message = "(EAT) Erro na leitura do token: " + this.currentToken.toString();
            // + "\n Token esperado: "
            // + tag.toString();
            this.throwCompilerException(message);
        }
    }

    // program’ ::= program $
    public void programLine() throws Exception {
        if (debug) {
            System.out.println("------------------------------ INICIO ------------------------------");
        }
        program();
        eat(Tag.END_OF_FILE);
        if (debug) {
            System.out.println("------------------------------ FIM ------------------------------");
        }
    }

    // program ::= program identifier begin [decl-list] stmt-list end "."
    public void program() throws Exception {
        if (debug) {
            System.out.println("program ::= program identifier [decl-list] begin stmt-list end \".\"");
        }
        eat(Tag.PROGRAM);

        boolean isInvalid = this.semantic.containsString(this.currentToken.getLexeme());
        if (isInvalid) {
            String message = "(PROGRAM) Erro variável já declarada: " +
                    this.currentToken.getLexeme();
            this.throwCompilerException(message);
        }

        semantic.addWordInDeclaration(this.currentToken.getLexeme(), this.currentToken);
        semantic.addTypeWordsInDeclarationInTable(this.currentToken.getLexeme(), Types.PROGRAM);

        identifier();
        declList();
        eat(Tag.BEGIN);
        stmtList();
        eat(Tag.END);
        this.reservedWordDoesNotHaveSemicolon = Tag.END;
        eat(Tag.DOT);
    }

    // decl-list ::= decl ";" { decl ";"}
    public void declList() throws Exception {
        if (debug) {
            System.out.println("decl-list ::= decl \";\" { decl \";\"}");
        }

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
        if (debug) {
            System.out.println("decl ::= ident-list is type");
        }
        try {
            identList();
            eat(Tag.IS);
            Types type = type();

            semantic.addTypeWordsInDeclarationInTable(this.lastToken.getLexeme(), type);

            // identifier.setTypeOfTag(type);
        } catch (CompilerException e) {
            String message = "(DECL) Erro na declaração de variável: " + this.currentToken.toString();
            this.throwCompilerException(message);
        }
    }

    // ident-list ::= identifier {"," identifier}
    public void identList() throws Exception {
        if (debug) {
            System.out.println("ident-list ::= identifier {\",\" identifier}");
        }

        this.lastToken = this.currentToken;

        try {
            // this.lexer.getSymbolTableInfos().printTable();
            boolean isInvalid = this.semantic.containsString(this.currentToken.getLexeme());

            if (this.currentToken.getTag() != Tag.BEGIN) {
                if (isInvalid) {
                    String message = "(IDENT-LIST) Variável já declarada: " +
                            this.currentToken.getLexeme();
                    this.throwCompilerException(message);
                }
                semantic.addWordInDeclaration(this.currentToken.getLexeme(), this.currentToken);
            }
            identifier();
            eat(Tag.COMMA);
            identList();
        } catch (CompilerException e) {
            return;
        }
    }

    // type ::= int | float | char
    public Types type() throws Exception {
        if (debug) {
            System.out.println("type ::= int | float | char");
        }
        switch (this.currentToken.getTag()) {
            case INT:
                eat(Tag.INT);
                return Types.INTEGER;

            case FLOAT:
                eat(Tag.FLOAT);
                return Types.FLOAT;
            case CHAR:
                eat(Tag.CHAR);
                return Types.CHAR;
            default:
                String message = "(TYPE) Erro na declaração de tipo: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
        return null;
    }

    // stmt-list ::= stmt {";" stmt}
    public void stmtList() throws Exception {
        if (debug) {
            System.out.println("stmt-list ::= stmt {\";\" stmt}");
        }

        try {
            stmt();
            if (this.reservedWordDoesNotHaveSemicolon != Tag.END
                    && this.reservedWordDoesNotHaveSemicolon != Tag.UNTIL) {
                eat(Tag.SEMI_COLON);
            } else {
                this.reservedWordDoesNotHaveSemicolon = null;
            }

            switch (this.currentToken.getTag()) {
                case ID:
                case IF:
                case WHILE:
                case REPEAT:
                case READ:
                case WRITE:
                    stmtList();
                    break;
                default:
                    break;
            }

        } catch (CompilerException e) {
            String message = "(STMT LIST) Erro na leitura do token: " +
                    this.currentToken.toString() + "\n" +
                    e.getMessage();
            this.throwCompilerException(message);
        }
    }

    // stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt |
    // write-stmt
    public void stmt() throws Exception {
        if (debug) {
            System.out.println("stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt");
        }

        // Tratamento para a saída de decl-list para stmt-list
        // if (this.lastToken != null &&
        // this.lastToken.getTag().toString().equals(Tag.ID.toString())) {
        // // assign-stmt ::= identifier(eat) "=" simple_expr
        // eat(Tag.ASSIGN);
        // simpleExpr(this);
        // lastToken = null;
        // return;
        // }

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
                String message = "(STMT) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // assign-stmt ::= identifier "=" simple_expr
    public void assignStmt() throws Exception {
        if (debug) {
            System.out.println("assign-stmt ::= identifier(eat) \"=\" simple_expr");
        }

        String lexeme = this.currentToken.getLexeme();
        Types actualType = semantic.getWordInSymbolTable(lexeme);
        if (actualType == null) {
            String message = "Variável não encontrada na tabela de símbolos";
            throwCompilerException(message);
        }

        identifier();
        eat(Tag.ASSIGN);

        lexeme = this.currentToken.getLexeme();

        simpleExpr(actualType);
    }

    // if-stmt ::= if condition then stmt-list if-stmt’
    public void ifStmt() throws Exception {
        if (debug) {
            System.out.println("if-stmt ::= if condition then stmt-list if-stmt’");
        }

        eat(Tag.IF);
        condition();
        eat(Tag.THEN);
        stmtList();
        ifStmtLine();
    }

    // if-stmt’ ::= end | else stmt-list end
    public void ifStmtLine() throws Exception {
        if (debug) {
            System.out.println("if-stmt' ::= end | else stmt-list end");
        }

        switch (this.currentToken.getTag()) {
            case END:
                eat(Tag.END);
                this.reservedWordDoesNotHaveSemicolon = Tag.END;
                break;
            case ELSE:
                eat(Tag.ELSE);
                stmtList();
                eat(Tag.END);
                this.reservedWordDoesNotHaveSemicolon = Tag.END;
                break;
            default:
                String message = "(IF_STMT_LINE) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // condition ::= expression
    public void condition() throws Exception {
        if (debug) {
            System.out.println("condition ::= expression");
        }

        System.out.println(semantic.getWordInSymbolTable(this.currentToken.getLexeme()));
        expression(null);
    }

    // repeat-stmt ::= repeat stmt-list stmt-suffix
    public void repeatStmt() throws Exception {
        if (debug) {
            System.out.println("repeat-stmt ::= repeat stmt-list stmt-suffix");
        }

        eat(Tag.REPEAT);
        stmtList();
        stmtSuffix();
    }

    // stmt-suffix ::= until condition
    public void stmtSuffix() throws Exception {
        if (debug) {
            System.out.println("stmt-suffix ::= until condition");
        }

        eat(Tag.UNTIL);
        this.reservedWordDoesNotHaveSemicolon = Tag.UNTIL;
        condition();
    }

    // while-stmt ::= stmt-prefix stmt-list end
    public void whileStmt() throws Exception {
        if (debug) {
            System.out.println("while-stmt ::= stmt-prefix stmt-list end");
        }

        stmtPrefix();
        stmtList();
        eat(Tag.END);
        this.reservedWordDoesNotHaveSemicolon = Tag.END;
    }

    // stmt-prefix ::= while condition do
    public void stmtPrefix() throws Exception {
        if (debug) {
            System.out.println("stmt-prefix ::= while condition do");
        }

        eat(Tag.WHILE);
        condition();
        eat(Tag.DO);
    }

    // read-stmt ::= read "(" identifier ")"
    public void readStmt() throws Exception {
        if (debug) {
            System.out.println("read-stmt ::= read \"(\" identifier \")\"");
        }

        eat(Tag.READ);
        eat(Tag.OPEN_PAR);
        // boolean isInvalid =
        // this.semantic.containsString(this.currentToken.getLexeme());
        // if (isInvalid) {
        // String message = "(PROGRAM) Erro variável já declarada: " +
        // this.currentToken.getLexeme();
        // this.throwCompilerException(message);
        // }
        identifier();
        eat(Tag.CLOSE_PAR);
    }

    // write-stmt ::= write "(" writable ")"
    public void writeStmt() throws Exception {
        if (debug) {
            System.out.println("write-stmt ::= write \"(\" writable \")\"");
        }

        eat(Tag.WRITE);
        eat(Tag.OPEN_PAR);
        writable();
        eat(Tag.CLOSE_PAR);
    }

    // writable ::= simple-expr | literal
    public void writable() throws Exception {
        if (debug) {
            System.out.println("writable ::= simple-expr | literal");
        }

        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case CONST_FLOAT:
            case CONST_CHAR:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
            case SUB:
                simpleExpr(null);
                break;
            case LITERAL:
                literal();
                break;
            default:
                String message = "(WRITABLE) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // expression ::= simple-expr expression’
    public void expression(Types actualType) throws Exception {
        if (debug) {
            System.out.println("expression ::= simple-expr expression’");
        }

        simpleExpr(actualType);
        expressionLine(actualType);
    }

    // expression’ ::= relop simple-expr
    public void expressionLine(Types actualType) throws Exception {
        if (debug) {
            System.out.println("expression' ::= relop simple-expr");
        }

        switch (this.currentToken.getTag()) {
            case EQUALS:
            case GREATER:
            case GREATER_EQ:
            case LOWER:
            case LOWER_EQ:
            case NOT_EQUALS:
                relop();
                simpleExpr(null);
                break;
            default:
                break;
        }
    }

    // simple-expr ::= term simple-expr’
    public void simpleExpr(Types actualType) throws Exception {
        if (debug) {
            System.out.println("simple-expr ::= term simple-expr'");
        }

        term(actualType);
        simpleExprLine(actualType);
    }

    // simple-expr’ ::= simple-expr addop simple-expr’ | λ
    public void simpleExprLine(Types actualType) throws Exception {
        if (debug) {
            System.out.println("simple-expr' ::= simple-expr addop simple-expr' | λ");
        }

        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case CONST_FLOAT:
            case CONST_CHAR:
            case SINGLE_QUOTE:
            case OPEN_PAR:
            case NOT:
                simpleExpr(actualType);
                addop();
                simpleExprLine(actualType);
                break;
            case OR:
            case ADD:
            case SUB:
                addop();
                expression(actualType);
                break;
            default:
                break;
        }
    }

    // term ::= factor-a term’
    public void term(Types actualType) throws Exception {
        if (debug) {
            System.out.println("term ::= factor-a term'");
        }

        factorA(actualType);
        termLine(actualType);
    }

    // term’ ::= mulop factor-a term’ | λ
    public void termLine(Types actualType) throws Exception {
        if (debug) {
            System.out.println("term' ::= mulop factor-a term' | λ");
        }

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
            case MUL:
            case DIV:
                mulop();
                factorA(actualType);
                termLine(actualType);
                break;
            default:
                break;
        }
    }

    // factor-a ::= factor |"!" factor | "-" factor
    public void factorA(Types actualType) throws Exception {
        if (debug) {
            System.out.println("factor-a ::= factor |\"!\" factor | \"-\" factor");
        }

        switch (this.currentToken.getTag()) {
            case ID:
            case CONST_INT:
            case CONST_FLOAT:
            case CONST_CHAR:
            case SINGLE_QUOTE:
            case OPEN_PAR:
                factor(actualType);
                break;
            case NOT:
                eat(Tag.NOT);
                factor(null);
                break;
            case SUB:
                eat(Tag.SUB);
                factor(null);
                break;
            default:
                String message = "(FACTOR-A) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // factor ::= identifier | constant | "(" expression ")"
    public void factor(Types actualTypeExpected) throws Exception {
        if (debug) {
            System.out.println("factor ::= identifier | constant | \"(\" expression \")\"");
        }
        boolean isValid;

        switch (this.currentToken.getTag()) {
            case ID:
                Types actualType = semantic.getWordInSymbolTable(this.currentToken.getLexeme());

                if (actualTypeExpected != null) {
                    isValid = actualType.toString().equals(actualTypeExpected.toString());

                    if (!isValid) {
                        String message = "(FACTOR) Erro na atribuição de identificadores, tipos incompatíveis";
                        this.throwCompilerException(message);
                    }
                }
                identifier();
                break;
            case CONST_INT:
            case CONST_FLOAT:
            case CONST_CHAR:
                if (actualTypeExpected != null) {
                    Tag actualTag = actualTypeExpected == Types.INTEGER ? Tag.CONST_INT
                            : (actualTypeExpected == Types.FLOAT ? Tag.CONST_FLOAT : Tag.CONST_CHAR);

                    isValid = this.currentToken.getTag().toString().equals(actualTag.toString());

                    if (!isValid) {
                        String message = "(FACTOR) Erro na atribuição de constantes, tipos incompatíveis";
                        this.throwCompilerException(message);
                    }
                }
                constant();
                break;
            case OPEN_PAR:
                eat(Tag.OPEN_PAR);
                expression(actualTypeExpected);
                eat(Tag.CLOSE_PAR);
                break;

            default:
                String message = "(FACTOR) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
    public void relop() throws Exception {
        if (debug) {
            System.out.println("relop ::= \"==\" | \">\" | \">=\" | \"<\" | \"<=\" | \"!=\"");
        }

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
                String message = "(RELOP) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // addop ::= "+" | "-" | "||"
    public void addop() throws Exception {
        if (debug) {
            System.out.println("addop ::= \"+\" | \"-\" | \"||\"");
        }

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
                String message = "(ADDOP) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // mulop ::= "*" | "/" | "&&"
    public void mulop() throws Exception {
        if (debug) {
            System.out.println("mulop ::= \"*\" | \"/\" | \"&&\"");
        }

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
                String message = "(MULOP) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // constant ::= integer_const | float_const | char_const
    public void constant() throws Exception {
        if (debug) {
            System.out.println("constant ::= integer_const | float_const | char_const");
        }

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
                String message = "(CONSTANT) Token não esperado: " + this.currentToken.toString();
                this.throwCompilerException(message);
        }
    }

    // digit ::= [0-9]
    public void digit() throws Exception {
        if (debug) {
            System.out.println("digit ::= [0-9]");
        }

        eat(Tag.INT);
    }

    // carac ::= um dos caracteres ASCII
    public void carac() throws Exception {
        if (debug) {
            System.out.println("carac ::= um dos caracteres ASCII");
        }

        eat(Tag.ID);
    }

    // caractere ::= um dos caracteres ASCII, exceto quebra de linha
    public void caractere() throws Exception {
        if (debug) {
            System.out.println("caractere ::= um dos caracteres ASCII, exceto quebra de linha");
        }

        if (this.currentToken.toString() == "\n") {
            String message = "(CARACTERE) Token não esperado: " + this.currentToken.toString();
            this.throwCompilerException(message);
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

    // integer_const ::= digit+
    public void integerConst() throws Exception {

        boolean condition = isIntegerConst();
        if (!condition) {
            String message = "(INTEGER_CONST) Inteiro mal formatado: " + this.currentToken.toString();
            this.throwCompilerException(message);
        }

        while (isIntegerConst()) {
            integerConstLine();
        }
    }

    // float_const ::= digit+ "." digit+
    public void floatConst() throws Exception {
        if (!isIntegerConst()) {
            String message = "(FLOAT_CONST) Decimal (parte inteira) mal formatado: " + this.currentToken.toString();
            this.throwCompilerException(message);
        }

        while (isIntegerConst()) {
            integerConstLine();
        }

        eat(Tag.DOT);

        if (!isIntegerConst()) {
            String message = "(FLOAT_CONST) Decimal (parte fracionada) mal formatado: " + this.currentToken.toString();
            this.throwCompilerException(message);
        }

        while (isIntegerConst()) {
            integerConstLine();
        }
    }

    // char_const ::= "'" carac "'"
    public void charConst() throws Exception {
        if (debug) {
            System.out.println("char_const ::= \"'\" carac \"'\"");
        }

        eat(Tag.SINGLE_QUOTE);
        carac();
        eat(Tag.SINGLE_QUOTE);
    }

    // literal ::= "{" caractere* "}"
    public void literal() throws Exception {
        if (debug) {
            System.out.println("literal ::= \"{\" caractere* \"}\"");
        }

        eat(Tag.LITERAL);
    }

    // identifier ::= letter (letter | digit | "_")*
    public void identifier() throws Exception {
        if (debug) {
            System.out.println("identifier ::= letter (letter | digit | \"_\")*");
        }
        lexer.putWordInSymbolTable(this.currentToken);
        eat(Tag.ID);
    }
}