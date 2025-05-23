package com.auberer.compilerdesignlectureproject.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;

/**
 * Reader class for reading characters from a file.
 * Input: File path
 * Output: Character stream from file
 */
@Slf4j
public class Reader implements IReader {

  private BufferedReader inputReader;
  private char currentChar;
  private CodeLoc currentCodeLoc = new CodeLoc(1, 0);
  private boolean eofReached = false;

  public Reader(Path path) {
    try {
      File file = path.toFile();
      FileReader fileReader = new FileReader(file);
      inputReader = new BufferedReader(fileReader);
      assert inputReader.ready();

      // Read first character
      advance();
    } catch (IOException e) {
      log.error("Reader error in constructor: {}", e.getMessage());
    }
  }

  // Only for testing purposes
  public Reader(String input) {
    try {
      StringReader stringReader = new StringReader(input);
      inputReader = new BufferedReader(stringReader);
      assert inputReader.ready();

      // Read first character
      advance();
    } catch (IOException e) {
      log.error("Reader error in constructor: {}", e.getMessage());
    }
  }

  @Override
  public char getChar() {
    return currentChar;
  }

  @Override
  public CodeLoc getCodeLoc() {
    return currentCodeLoc;
  }

  @Override
  public void advance() {
    assert !isEOF();
    try {
      int input = inputReader.read();

      if (input == -1) {
        eofReached = true;
        return;
      }

      currentChar = (char) input;
      if (currentChar == '\n') {
        currentCodeLoc.setLine(currentCodeLoc.getLine() + 1);
        currentCodeLoc.setColumn(0);
      } else {
        currentCodeLoc.setColumn(currentCodeLoc.getColumn() + 1);
      }
    } catch (IOException e) {
      log.error("Reader error in advance: {}", e.getMessage());
    }
  }

  @Override
  public void expect(char c) throws RuntimeException {
    if (currentChar != c) {
      throw new RuntimeException("Unexpected character");
    } else {
      advance();
    }
  }

  @Override
  public boolean isEOF() {
    return eofReached;
  }

  @Override
  public void close() {
    try {
      inputReader.close();
    } catch (IOException e) {
      log.error("Reader error in close: {}", e.getMessage());
    }
  }
}
