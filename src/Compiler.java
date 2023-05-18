import java.io.FileNotFoundException;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;
import syntactic.Parser;
import utils.CompilerException;

public class Compiler {
    public static void main(String[] args) throws Exception {
        // Scanner input = new Scanner(System.in);

        String fileName = args[0];

        Lexer lexer;
        Token token;
        Parser parser;

        try {
            lexer = new Lexer(fileName);
            parser = new Parser(lexer);
            do {
                // token = lexer.scan();
                // System.out.println(token.toString() + " ");
                parser.programLine();

            }
            // while (token.getTag() != Tag.END_OF_FILE);
            while (parser.getCurrentToken().getTag() != Tag.END_OF_FILE);

        } catch (FileNotFoundException e) {
            System.out.println("Insira o nome de um arquivo válido.");
        } catch (CompilerException e) {
            System.out.println(e);
        }

    }
}