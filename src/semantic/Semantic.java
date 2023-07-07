package semantic;

import symbolTable.Table;

public class Semantic {
    private Table symbolTableInfos;

    public Semantic(Table symbolTable) {
        this.symbolTableInfos = symbolTable;
    }

    public boolean containsString(String lexeme) {
        return symbolTableInfos.containsString(lexeme);
    }
}
