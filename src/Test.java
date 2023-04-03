import java.util.Map;
import java.util.Map.Entry;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;

public class Test {
    public static void main(String[] args) throws Exception {
        String filePath = "../programs/teste4";

        Lexer lexer = new Lexer(filePath);
        Token token;

        // run scan from init file to EOF
        do {
            token = lexer.scan();
            System.out.println(token);
        } while (token.getTag() != Tag.END_OF_FILE);

        // debug symbolTable
        // for (Entry<String, Tag> entry : lexer.words.table.entrySet()) {
        // String key = entry.getKey();
        // Tag value = entry.getValue();
        // System.out.println("Chave: " + key + ", Valor: " + value);
        // }
    }
}