package parser;

/**
 * Exception class for parsing errors (and syntax errors) in Hawk programming language.
 */

public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }
}