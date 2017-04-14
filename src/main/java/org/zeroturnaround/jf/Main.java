package org.zeroturnaround.jf;

import java.io.IOException;

import org.zeroturnaround.jf.homework13.ChatClient;
import org.zeroturnaround.jf.homework13.ChatServer;

public class Main {
  public static void main(String... args) throws IOException {
    if (args.length < 1) {
      printUsageAndExit();
    }

    switch (args[0]) {
      case "c":
        ChatClient.main(new String[0]);
        break;
      case "s":
        ChatServer.main(new String[0]);
        break;
      default:
        printUsageAndExit();
    }
  }

  private static void printUsageAndExit() {
    System.err.println("Usage: java -jar target/jf-homework13.jar <c|s>");
    System.exit(0);
  }
}
