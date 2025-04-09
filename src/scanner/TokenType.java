package scanner;

/**
* Enum representing the different types of tokens that can be recognized by the scanner
 * to then be stored into each.
*/

public enum TokenType {
    // Reserved words
    PROGRAM, BEGIN, END, IF, THEN, ELSE, INPUT, OUTPUT, INT, WHILE, LOOP, ID, NUM, EOF

    // Operators
    ASSIGN, LESS_THAN, GREATER_THAN, EQUALS, NOT_EQUALS, PLUS, MINUS, MULTIPLY, DIVIDE,

    // Separators
    SEMICOLON, COMMA, COLON, DOT, LPAREN, RPAREN
}