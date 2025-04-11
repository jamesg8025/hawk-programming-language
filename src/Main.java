import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import parser.Parser;
import parser.ParseException;
import scanner.Scanner;

/**
 * Main driver class for the Hawk compiler.
 * Handles command line arguments and initiates the parsing process.
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
                                "x, y: double;\n" +
                                "e: int;\n" +
                            "begin\n" +
                                "input x;\n" +
                                "y := 1;\n" +
                                "if (x > 0) then\n" +
                                    "e := 0;\n" +
                                    "while (x > 0)\n" +
                                    "loop\n" +
                                        "y := y * x;\n" +
                                        "x := x -1;\n" +
                                    "end loop;\n" +
                                "else\n" +
                                    "e := 1;\n" +
                                "end if;\n" +
                                "output e, x, y;\n" +
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