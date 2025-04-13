import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import parser.Parser;
import parser.ParseException;
import scanner.Scanner;

/**
 * Main driver class for the Hawk language compiler.
 * Handles command line arguments (program file path) or hard-coded sample program inputs.
 * The parsing process is then initiated.
 */
public class Main {

    public static void main(String[] args) {
        // If a file path is provided, read from file, otherwise use the sample program
        Reader input;
        try {
            if (args.length > 0) {
                input = new FileReader(args[0]);
            } else {
                // Default sample program for testing
                String sampleProgram =
                        "program\n" +
                                "x, y: int;\n" +
                            "begin\n" +
                                "input x, y;\n" +
                                "y := x + y;\n" +
                                "output y;\n" +
                            "end;\n";

                input = new StringReader(sampleProgram);
            }

            // Initialize the scanner and parser
            Scanner scanner = new Scanner(input);
            Parser parser = new Parser(scanner);

            // Start parsing
            parser.parseProgram();

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            System.exit(1);
        }
    }
}