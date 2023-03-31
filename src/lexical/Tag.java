package lexical;

public enum Tag {
    // SPECIALS
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,
    NOT_EXPECTED,

    // SYMBOLS
    DOT, // .
    SEMI_COLON, // ;
    COMMA, // ,
    OPEN_PAR, // (
    CLOSE_PAR, // )
    OPEN_KEY, // {
    CLOSE_KEY, // }

    // OPERATORS
    ASSIGN, // =
    NOT, // !
    EQUALS, // ==
    GREATER, // >
    GREATER_EQ, // >=
    LOWER, // <
    LOWER_EQ, // <=
    NOT_EQUALS, // !=
    ADD, // +
    SUB, // -
    OR, // ||
    MUL, // *
    DIV, // /
    AND, // &&
    UNDERLINE, // _

    // KEYWORDS
    PROGRAM, // program
    BEGIN, // begin
    END, // end
    IS, // is
    INT, // int
    FLOAT, // float
    CHAR, // char
    IF, // if
    ELSE, // else
    THEN, // then
    REPEAT, // repeat
    UNTIL, // until
    WHILE, // while
    DO, // do
    READ, // read
    WRITE, // write

    // OTHERS
    ID, // identifier
    NUMBER, // number
    LITERAL, // literal
}
