package parser;

import java.util.HashMap;
import java.util.Map;

/**
 * SymbolTable for Hawk programming language.
 * Works by storing identifiers and their types into a hashmap.
 */

public class SymbolTable {
    private Map<String, SymbolInfo> symbols = new HashMap<>();

    // class to store symbol info
    private class SymbolInfo {
        private String type;
        private int declarationLine;

        // create new symbol info
        public SymbolInfo(String type, int declarationLine) {
            this.type = type;
            this.declarationLine = declarationLine;
        }

        // getters for symbol info
        public String getType() {
            return type;
        }

        public int getDeclarationLine() {
            return declarationLine;
        }
    }

    // Add symbol to symbol table
    public void add(String name, String type, int line) throws ParseException {
        if (symbols.containsKey(name)) {
            throw new ParseException("Error at line " + line +
                    ": Redeclaration of variable '" + name + "'");
        }
        symbols.put(name, new SymbolInfo(type, line));
    }

    // Check if symbol is declared
    public void checkDeclared(String name, int line) throws ParseException {
        if (!symbols.containsKey(name)) {
            throw new ParseException("Error at line " + line +
                    ": Undeclared variable '" + name + "'");
        }
    }

    // Return symbol type, name
    public String getType(String name) {
        SymbolInfo info = symbols.get(name);
        if (info != null) {
            return info.getType();
        }
        return null;
    }

    // Check if word is a reserved word
    public boolean isReservedWord(String word) {
        String[] reservedWords = {
            "program", "begin", "end", "if", "then", "else",
            "input", "output", "while", "loop",
            "int", "float", "double", "call"
        };

        for (String reserved : reservedWords) {
            if (reserved.equals(word)) {
                return true;
            }
        }
        return false;
    }
}