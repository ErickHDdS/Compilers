package lexical;

public enum Tag {
    // SPECIALS
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,

    // SYMBOLS
    SEMI_COLON,     // ;
    COMMA,          // ,
    ASSIGN,         // =
    DOT,            // .
    APOSTROPHE,     // '

    // OPERATORS
    EQUALS,         // ==
    NOT_EQUALS,     // !=
    LOWER,          // <
    GREATER,        // >
    LOWER_EQ,       // <=
    GREATER_EQ,     // >=
    CONTAINS,       // ===
    ADD,            // +
    SUB,            // -
    MUL,            // *
    DIV,            // /
    MOD,            // %
    EXP,            // **
    AND,            // &&
    OR,             // ||

    // KEYWORDS
    IF,             // if
    THEN,           // then
    ELSIF,          // elsif
    ELSE,           // else
    END,            // end
    WHILE,          // while
    DO,             // do
    FOR,            // for
    TRUE,           // true
    FALSE,          // false
    OPEN_BRA,       // [
    CLOSE_BRA,      // ]
    OPEN_PAR,       // (
    CLOSE_PAR,      // )

    // OTHERS
    ID,             // identifier
    NUMBER,         // number
    STRING,         // string
}
