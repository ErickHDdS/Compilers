import symbolTable.Table;

public class Semantic {
    private Table symbolTable;
    private HashMap<String, World> symbolTable = new HashMap<String, World>();

    public Semantic(Table symbolTable) {
        this.symbolTable = symbolTable;
    }

    public boolean isDeclared(String lexeme) {
        return symbolTable.isDeclared(lexeme);
    }


}