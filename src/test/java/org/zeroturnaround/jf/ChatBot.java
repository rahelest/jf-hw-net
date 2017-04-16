package org.zeroturnaround.jf;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ChatBot implements Runnable {

  private final Process process;
  private final Dialogue dialogue;
  private volatile PrintStream out;

  public ChatBot(Process process, Dialogue.Line... dialogueLines) {
    this.process = process;
    this.dialogue = new Dialogue(dialogueLines);
  }

  public Dialogue getDialogue() {
    return dialogue;
  }

  @Override
  public void run() {
    Scanner in = new Scanner(process.getInputStream());
    PrintStream out = new PrintStream(new BufferedOutputStream(process.getOutputStream()), true);

    this.out = out;

    while (in.hasNextLine()) {
      dialogue.replyTo(in.nextLine()).forEach(out::println);
    }
  }

  public void quit() {
    out.println("/q");
  }
}
