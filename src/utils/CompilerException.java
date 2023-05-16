package utils;

public class CompilerException extends Exception {
    public CompilerException(String message, int line) {
        super("Linha: " + line +
                "\nErro: " +
                "\n" + message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
