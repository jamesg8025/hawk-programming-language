package scanner;

/**
* Enum representing the different types of tokens that can be recognized by the scanner
 * to then be stored into each.
*/

public enum TokenType {
    // Reserved words (keywords)
    PROGRAM, BEGIN, END, IF, THEN, ELSE, INPUT, OUTPUT, WHILE, LOOP, ID, NUM, INT, FLOAT, DOUBLE, CALL, EOF,

    // Operators
    ASSIGN, LESS_THAN, GREATER_THAN, EQUALS, NOT_EQUALS, PLUS, MINUS, MULT, DIV,

    // Separators
    SEMICOLON, COMMA, COLON, DOT, LPAREN, RPAREN
}