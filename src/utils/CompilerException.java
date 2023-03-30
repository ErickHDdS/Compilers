package utils;

public class CompilerException extends Exception {
    public CompilerException(String message, String local, int line) {
        super("Linha: " + line +
                "\nErro ao processar: " + local +
                "\n" + message);
    }

}
