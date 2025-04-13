package scanner;

import java.io.IOException;
import java.io.Reader;
import parser.SymbolTable;

/**
 * Scanner for Hawk programming language.
 * Performs lexical analysis on the input code to then turn into tokens.
 */

public class Scanner {
    private Reader reader;
    private int line = 1;
    private int position = 0;
    private int currentChar = -1;
    private Token currentToken = null;
    private SymbolTable symbolTable;

    // Constructor for the scanner
    public Scanner(Reader input) throws IOException {
        this.reader = input;
        this.symbolTable = new SymbolTable();
        nextChar(); // Read first char
    }

    // Method to move to next char
    private void nextChar() throws IOException {
            currentChar = reader.read();
            position++;
            if (currentChar == '\n') {
                line++;
                position = 0;
            }
    }

    // Method for skipping whitespace
    private void skipWhitespace() throws IOException {
        while (currentChar != -1 && Character.isWhitespace(currentChar)) {
            nextChar();
        }
    }

    // Return current token
    public Token getToken() {
        return currentToken;
    }

    // Return current line num
    public int getLine() {
        return line;
    }

    // Return symbol table used
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    // Scan next token
    public Token nextToken() throws IOException {
        skipWhitespace();

        // Check for end of file
        if (currentChar == -1) {
            currentToken = new Token(TokenType.EOF, "", line, position);
            return currentToken;
        }

        // Check for identifiers based on the first char
        if (Character.isLetter(currentChar) || currentChar == '_') {
            return scanIdentifier();
        } else if (Character.isDigit(currentChar)) {
            return scanNumber();
        } else {
            return scanSymbol();
        }
    }

    // Scan identifiers or keyword from input
    private Token scanIdentifier() throws IOException {
            StringBuilder lexeme = new StringBuilder();
            // init start pos
            int startPosition = position;
            while (currentChar != -1 && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                lexeme.append((char) currentChar);
                nextChar();
            }

            // Convert lexeme to id and declare enum type for token classification.
            String id = lexeme.toString();
            TokenType type;

            // Check if the identifier is a keyword
            switch (id) {
                case "program":
                    type = TokenType.PROGRAM;
                    break;
                case "begin":
                    type = TokenType.BEGIN;
                    break;
                case "end":
                    type = TokenType.END;
                    break;
                case "if":
                    type = TokenType.IF;
                    break;
                case "then":
                    type = TokenType.THEN;
                    break;
                case "else":
                    type = TokenType.ELSE;
                    break;
                case "input":
                    type = TokenType.INPUT;
                    break;
                case "output":
                    type = TokenType.OUTPUT;
                    break;
                case "while":
                    type = TokenType.WHILE;
                    break;
                case "loop":
                    type = TokenType.LOOP;
                    break;
                case "int":
                    type = TokenType.INT;
                    break;
                case "float":
                    type = TokenType.FLOAT;
                    break;
                case "double":
                    type = TokenType.DOUBLE;
                    break;
                case "call":
                    type = TokenType.CALL;
                    break;
                default:
                    if (symbolTable.isReservedWord(id)) {
                        throw new IOException("Error at line " + line + ": " + id + " ' is a reserved word.");
                    }
                    type = TokenType.ID;
                    break;
            }

            currentToken = new Token(type, id, line, startPosition);
            return currentToken;
        }

        // Scan Num from input
        private Token scanNumber() throws IOException {
            StringBuilder lexeme = new StringBuilder();
            int startPosition = position;
            boolean hasDecimal = false;
            int digitCount = 0;

            while (currentChar != -1 && (Character.isDigit(currentChar) || currentChar == '.')) {
                if (currentChar == '.') {
                    if (hasDecimal) {
                        break; // not a num if there is second decimal
                    }
                    hasDecimal = true;
                    } else {
                        digitCount++;
                        if (digitCount > 10) {
                            throw new IOException("Error at line " + line + ": Number exceeds 10 digits");
                        }
                }

                lexeme.append((char) currentChar);
                nextChar();
            }
            currentToken = new Token(TokenType.NUM, lexeme.toString(), line, startPosition);
            return currentToken;
        }

    // Scan symbol from input
    private Token scanSymbol() throws IOException {
        int startPosition = position;
        TokenType type;
        String lexeme = String.valueOf((char) currentChar);

        switch (currentChar) {
            case ':':
                nextChar();
                if (currentChar == '=') {
                    nextChar();
                    type = TokenType.ASSIGN;
                    lexeme = ":=";
                } else {
                    type = TokenType.COLON;
                    lexeme = ":";
                }
                break;
            case ';':
                nextChar();
                type = TokenType.SEMICOLON;
                lexeme = ";";
                break;
            case ',':
                nextChar();
                type = TokenType.COMMA;
                lexeme = ",";
                break;
            case '+':
                nextChar();
                type = TokenType.PLUS;
                lexeme = "+";
                break;
            case '-':
                nextChar();
                type = TokenType.MINUS;
                lexeme = "-";
                break;
            case '*':
                nextChar();
                type = TokenType.MULT;
                lexeme = "*";
                break;
            case '/':
                nextChar();
                type = TokenType.DIV;
                lexeme = "/";
                break;
            case '(':
                nextChar();
                type = TokenType.LPAREN;
                lexeme = "(";
                break;
            case ')':
                nextChar();
                type = TokenType.RPAREN;
                lexeme = ")";
                break;
            case '=':
                nextChar();
                type = TokenType.EQUALS;
                lexeme = "=";
                break;
            case '>':
                nextChar();
                type = TokenType.GREATER_THAN;
                lexeme = ">";
                break;
            case '<':
                nextChar();
                type = TokenType.LESS_THAN;
                lexeme = "<";
                break;
            default:
                nextChar();
                throw new IOException("Error at line " + line + ": Illegal symbol: '" + lexeme + "'");
        }
        currentToken = new Token(type, lexeme, line, startPosition);
        return currentToken;
    }
}