package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scanner.Scanner;
import scanner.Token;
import scanner.TokenType;

/**
 * Parser for Hawk programming language.
 * Checks for errors according to provided grammar rules.
 */

public class Parser {
    private Scanner scanner;
    private Token currentToken;
    private SymbolTable symbolTable;

    // Constructor for the parser
    public Parser(Scanner scanner) throws IOException {
        this.scanner = scanner;
        this.currentToken = scanner.nextToken();
        this.symbolTable = scanner.getSymbolTable();
    }

    // Match currentToken with expected token type
    private void match(TokenType expectedType) throws ParseException, IOException {
        if (currentToken.getType() == expectedType) {
            currentToken = scanner.nextToken();
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected " + expectedType + " but found " + currentToken.getLexeme() + "'");
        }
    }

    // Rule 1:
    public void parseProgram() throws ParseException, IOException {
        System.out.println("PROGRAM");

        // Match PROGRAM keyword
        if (currentToken.getType() == TokenType.PROGRAM) {
            match(TokenType.PROGRAM);

            // Check if DECL_SEC
            if (currentToken.getType() != TokenType.BEGIN) {
                parseDeclSec();
            }

            // Match BEGIN keyword
            match(TokenType.BEGIN);

            // Parse STMT_SEC
            parseStmtSec();

            // Match END keyword (end;)
            match(TokenType.END);
            match(TokenType.SEMICOLON);

        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Program must start with 'program' keyword");
        }
    }

    // Rule 2:
    private void parseDeclSec() throws ParseException, IOException {
        System.out.println("DECL_SEC");

        parseDecl();

        // Check for more declarations
        if (currentToken.getType() == TokenType.ID) {
            parseDeclSec();
        }
    }

    // Rule 3:
    private void parseDecl() throws ParseException, IOException {
        System.out.println("DECL");

        // Create list of identifiers for multiple same-line id declarations
        List<String> identifiers = parseIdList();

        match(TokenType.COLON);

        String type = parseType();

        match(TokenType.SEMICOLON);

        // Add identifiers to symbol table
        for (String id : identifiers) {
            symbolTable.add(id, type, scanner.getLine());
        }
    }

    // Rule 4:
    private List<String> parseIdList() throws ParseException, IOException {
        System.out.println("ID_LIST");

        List<String> identifiers = new ArrayList<>();

        if (currentToken.getType() == TokenType.ID) {
            identifiers.add(currentToken.getLexeme());
            match(TokenType.ID);

            if (currentToken.getType() == TokenType.COMMA) {
                match(TokenType.COMMA);
                identifiers.addAll(parseIdList());
            }
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected identifier but found " + currentToken.getLexeme() + "'");
        }

        return identifiers;
    }

    // Rule 18: TYPE
    private String parseType() throws ParseException, IOException {
        System.out.println("TYPE");

        String type = ""; // Initialize type variable

        if (currentToken.getType() == TokenType.INT) {
            type = "int";
            match(TokenType.INT);
        } else if (currentToken.getType() == TokenType.FLOAT) {
            type = "float";
            match(TokenType.FLOAT);
        } else if (currentToken.getType() == TokenType.DOUBLE) {
            type = "double";
            match(TokenType.DOUBLE);
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected type (int, float, double) but found '" +
                    currentToken.getLexeme() + "'");
        }
        return type;
    }

    // Rule 6: STMT_SEC
    private void parseStmtSec() throws ParseException, IOException {
        System.out.print("STMT_SEC");

        parseStmt();

        // Check for more statements
        if (currentToken.getType() != TokenType.END &&
            currentToken.getType() != TokenType.ELSE) {
            parseStmtSec();
        }
    }

    // Rule 7: STMT
    private void parseStmt() throws ParseException, IOException {
        System.out.println("STMT");

        switch (currentToken.getType()) {
            case ID:
                parseAssign();
                break;
            case IF:
                parseIfStmt();
                break;
            case WHILE:
                parseWhileStmt();
                break;
            case INPUT:
                parseInput();
                break;
            case OUTPUT:
                parseOutput();
                break;
            default:
                throw new ParseException("Error at line " + currentToken.getLine() +
                        " : Expected statement but found '" + currentToken.getLexeme() + "'");
        }
    }

    // Rule 8: ASSIGN
    private void parseAssign() throws ParseException, IOException {
        System.out.println("ASSIGN");

        if (currentToken.getType() == TokenType.ID) {
            // Check if id is declared
            symbolTable.checkDeclared(currentToken.getLexeme(), currentToken.getLine());

            match(TokenType.ID);
            match(TokenType.ASSIGN);
            parseExpr();
            match(TokenType.SEMICOLON);
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected identifier but found '" + currentToken.getLexeme() + "'");
        }
    }

    // Rule 9: IFSTMT
    private void parseIfStmt() throws ParseException, IOException {
        System.out.println("IFSTMT");

        match(TokenType.IF);
        parseComp();
        match(TokenType.THEN);
        parseStmtSec();

        if (currentToken.getType() == TokenType.ELSE) {
            match(TokenType.ELSE);
            parseStmtSec();
        }

        match(TokenType.END);
        match(TokenType.IF);
        match(TokenType.SEMICOLON);
    }

    // Rule 10: WHILESTMT
    private void parseWhileStmt() throws ParseException, IOException {
        System.out.println("WHILESTMT");

        match(TokenType.WHILE);
        parseComp();
        match(TokenType.LOOP);
        parseStmtSec();
        match(TokenType.END);
        match(TokenType.LOOP);
        match(TokenType.SEMICOLON);
    }

    // Rule 11: INPUT
    private void parseInput() throws ParseException, IOException {
        System.out.println("INPUT");

        // Check if all id are declared
        List<String> identifiers = parseIdList();
        for (String id : identifiers) {
            symbolTable.checkDeclared(id, scanner.getLine());
        }

        match(TokenType.SEMICOLON);
    }

    // Rule 12: OUTPUT
    private void parseOutput() throws ParseException, IOException {
        System.out.println("OUTPUT");

        if (currentToken.getType() == TokenType.ID) {
            // Check if all id are declared
            List<String> identifiers = parseIdList();
            for (String id : identifiers) {
                symbolTable.checkDeclared(id, scanner.getLine());
            }
        } else if (currentToken.getType() == TokenType.NUM) {
            match(TokenType.NUM);
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected identifier or number but found '" + currentToken.getLexeme() + "'");
        }

        match(TokenType.SEMICOLON);
    }

    // Rule 13: EXPR
    private void parseExpr() throws ParseException, IOException {
        System.out.println("EXPR");

        parseFactor();

        if (currentToken.getType() == TokenType.PLUS) {
            match(TokenType.PLUS);
            parseExpr();
        } else if (currentToken.getType() == TokenType.MINUS) {
            match(TokenType.MINUS);
            parseExpr();
        }
    }

    // Rule 14: FACTOR
    private void parseFactor() throws ParseException, IOException {
        System.out.println("FACTOR");

        parseOperand();

        if (currentToken.getType() == TokenType.MULT) {
            match(TokenType.MULT);
            parseFactor();
        } else if (currentToken.getType() == TokenType.DIV) {
            match(TokenType.DIV);
            parseFactor();
        }
    }

    // Rule 15: OPERAND
    private void parseOperand() throws ParseException, IOException {
        System.out.print("OPERAND");

        if (currentToken.getType() == TokenType.NUM) {
            // Check if id is declared
            symbolTable.checkDeclared(currentToken.getLexeme(), currentToken.getLine());

            // Store id in case it's part of a function call
            String idName = currentToken.getLexeme();
            match(TokenType.NUM);

            // Check if it's a function call
            if (currentToken.getType () == TokenType.LPAREN) {
                // handle function call
                parseFunCall(idName);
            }
        } else if (currentToken.getType() == TokenType.LPAREN) {
            match(TokenType.LPAREN);
            parseExpr();
            match(TokenType.RPAREN);
        } else if (currentToken.getType() == TokenType.CALL) {
            parseFunCall(null);
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected number, identifier, '(', or function call but found '" +
                    currentToken.getLexeme() + "'");
        }
    }

    // Rule 19: FUNCALL
    private void parseFunCall(String idName) throws ParseException, IOException {
        System.out.println("FUNCALL");

        if (idName == null) {
            match(TokenType.CALL);
            idName = currentToken.getLexeme();
            match(TokenType.ID);
        }

        // Valid func name is assumed

        match(TokenType.LPAREN);

        // Check that all ids in list are declared
        List<String> identifiers = parseIdList();
        for (String id : identifiers) {
            symbolTable.checkDeclared(id, scanner.getLine());
        }

        match(TokenType.RPAREN);
        match(TokenType.SEMICOLON);
    }

    // Rule 17: COMP
    private void parseComp() throws ParseException, IOException {
        System.out.println("COMP");

        match(TokenType.LPAREN);
        parseOperand();

        if (currentToken.getType() == TokenType.EQUALS) {
            match(TokenType.EQUALS);
        } else if (currentToken.getType() == TokenType.NOT_EQUALS) {
            match(TokenType.NOT_EQUALS);
        } else if (currentToken.getType() == TokenType.GREATER_THAN) {
            match(TokenType.GREATER_THAN);
        } else if (currentToken.getType() == TokenType.LESS_THAN) {
            match(TokenType.LESS_THAN);
        } else {
            throw new ParseException("Error at line " + currentToken.getLine() +
                    " : Expected comparison operator but found '" + currentToken.getLexeme() + "'");
        }

        parseOperand();
        match(TokenType.RPAREN);
    }
}