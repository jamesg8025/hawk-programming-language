# hawk-programming-language
Implementation of a scanner and recursive decent parser for Hawk, a small programming language.

## Overview:
The scanner in this program performs lexical analysis by converting input characters into a sequence of tokens. Such tokens that are handled consist of keywords, identifiers, numbers, operators, and separators. The parser then analyzes the tokens to ensure that the Hawk grammar rules are being followed correctly. In addition to parsing and scanning, the program stops when an error is encountered and the proper error message is generated.


### How to use:
To use this compiler for the Hawk programming language, do one of two things:
- Use the input file name as an argument by using the following syntax: java Main [filepath]
- Or, hard-code a sample program into the Main driver class, as shown already in the class.
