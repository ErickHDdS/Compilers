import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import lexical.Lexer;
import lexical.Tag;
import lexical.Token;

public class Test {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        String fileName;

        while (true) {
            System.out.println("\nDigite o nome do programa: \nCRTL + C para encerrar.\n");
            fileName = input.nextLine();
            String filePath;
            Lexer lexer;
            Token token;

            try {
                filePath = "../programs/" + fileName;
                lexer = new Lexer(filePath);
                // run scan from init file to EOF
                do {
                    token = lexer.scan();
                    System.out.println(token.toString() + " ");
                } while (token.getTag() != Tag.END_OF_FILE);

                // debug symbolTable
                // for (Entry<String, Tag> entry : lexer.words.table.entrySet()) {
                // String key = entry.getKey();
                // Tag value = entry.getValue();
                // System.out.println("Chave: " + key + ", Valor: " + value);
                // }
            } catch (FileNotFoundException e) {
                System.out.println("Insira o nome de um arquivo válido.");
            }

        }
    }
}