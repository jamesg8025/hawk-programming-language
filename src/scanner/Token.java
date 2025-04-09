package scanner;

/**
 * Represents a token found in the language.
 */

public class Token {
    private TokenType type;
    private String lexeme;
    private int line;
    private int position;

    // Token Constructor for identifying the token type, lexeme, line number, and position
    public Token(TokenType type, String lexeme, int line, int position) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.position = position;
    }

    // Getters for the token attributes
    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }

    // Provide string representation of the token found
    @Override
    public String toString() {
        return type + " " + lexeme + " at line " + line + ", position " + position;
    }
}