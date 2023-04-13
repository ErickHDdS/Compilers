import java.io.FileNotFoundException;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;
import utils.CompilerException;

public class Compiler {
    public static void main(String[] args) throws Exception {
        // Scanner input = new Scanner(System.in);

        String fileName = "../programs/" + args[0];

        Lexer lexer;
        Token token;

        try {
            lexer = new Lexer(fileName);
            do {
                token = lexer.scan();
                System.out.println(token.toString() + " ");
            } while (token.getTag() != Tag.END_OF_FILE);

        } catch (FileNotFoundException e) {
            System.out.println("Insira o nome de um arquivo válido.");
        } catch (CompilerException e) {
            System.out.println(e);
        }

    }
}