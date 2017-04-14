package org.zeroturnaround.jf;

import java.util.Arrays;
import java.util.List;

public class Dialogue {

  private String name = "<name unknown>";

  public static class Line {
    String phrase;
    List<String> replies;

    private Line(String phrase, List<String> replies) {
      this.phrase = phrase;
      this.replies = replies;
    }
  }

  public static Line line(String phrase, String... replies) {
    return new Line(phrase, Arrays.asList(replies));
  }

  private final Line[] lines;
  private int currentLineIndex = 0;
  private volatile boolean completedAsExpected = false;

  public Dialogue(Line... lines) {
    this.lines = lines;
  }

  public List<String> replyTo(String phrase) {
    if (currentLineIndex == lines.length) {
      completedAsExpected = false;
      throw new IllegalStateException(String.format("%s got an extra phrase \"%s\" after the dialog has ended", name, phrase));
    }

    Line line = lines[currentLineIndex];

    if (line.phrase.equals(phrase)) {
      if (currentLineIndex == 0) {
        name = line.replies.get(0);
      }

      currentLineIndex++;

      if (currentLineIndex == lines.length) {
        completedAsExpected = true;
      }

      return line.replies;
    }

    completedAsExpected = false;
    throw new IllegalStateException(String.format("%s expected to hear \"%s\", instead heard \"%s\"", name, line.phrase, phrase));
  }

  public boolean hasCompletedAsExpected() {
    return completedAsExpected;
  }
}
