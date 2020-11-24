package com.steventimothy.baeldung;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

@Slf4j
public class TryWithResources {

  public static void main(String[] args) {
    tryWithResources();
    tryCatchFinallyWithTryWithResources();
    tryWithMultipleResources();
    tryWithResourcesImplementation();
    resourceClosingOrder();
  }

  /**
   * Try-With-Resources allows you to initialize classes that implements the {@link AutoCloseable} interface.
   * For example, the PrintWriter classes hierarchy implements this. This allows you to omit the finally
   * clause so that you do not have to worry about closing the class.
   */
  private static void tryWithResources() {
    try (PrintWriter printWriter = new PrintWriter(new File("test.txt"))) {
      printWriter.println("Hello World");
    }
    catch (FileNotFoundException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * This function takes both the Try-Catch-Finally and Try-With-Resources and implements the same logic to
   * compare the difference between them.
   */
  private static void tryCatchFinallyWithTryWithResources() {

    //Try-Catch-Finally example.
    Scanner scanner = null;
    try {
      scanner = new Scanner(new File("test.txt"));
      while (scanner.hasNext()) {
        log.info(scanner.nextLine());
      }
    }
    catch (FileNotFoundException ex) {
      log.error(ex.getMessage());
    }
    finally {
      if (scanner != null) {
        scanner.close();
      }
    }

    //Try-With-Resources example.
    try (Scanner scanner2 = new Scanner(new File("test.txt"))) {
      while (scanner2.hasNext()) {
        log.info(scanner2.nextLine());
      }
    }
    catch (FileNotFoundException ex) {
      log.error(ex.getMessage());
    }
  }

  /**
   * This function demonstrates how you can initialize 2 auto closable classes in the Try-With-Resources.
   */
  private static void tryWithMultipleResources() {
    try (Scanner scanner = new Scanner(new File("testRead.txt"));
         PrintWriter printWriter = new PrintWriter(new File("testWrite.txt"))) {
      while (scanner.hasNext()) {
        printWriter.print(scanner.nextLine());
      }
    }
    catch (FileNotFoundException ex) {
      log.error(ex.getMessage());
    }
  }

  private static void tryWithResourcesImplementation() {
    try (MyResource myResource = new MyResource()) {
      log.info("Made it here");
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

  private static void resourceClosingOrder() {
    try (AutoCloseableResourcesFirst autoCloseableResourcesFirst = new AutoCloseableResourcesFirst();
         AutoCloseableResourcesSecond autoCloseableResourcesSecond = new AutoCloseableResourcesSecond()) {

      autoCloseableResourcesFirst.doSomething();
      autoCloseableResourcesSecond.doSomething();
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

  private static class MyResource implements AutoCloseable {

    @Override
    public void close() throws Exception {
      log.info("Close MyResource");
    }
  }

  private static class AutoCloseableResourcesFirst implements AutoCloseable {
    public AutoCloseableResourcesFirst() {
      log.info("Constructor -> AutoCloseableResources_First");
    }

    public void doSomething() {
      log.info("Something -> AutoCloseableResources_First");
    }

    @Override
    public void close() throws Exception {
      log.info("Closed AutoCloseableResources_First");
    }
  }

  private static class AutoCloseableResourcesSecond implements AutoCloseable {
    public AutoCloseableResourcesSecond() {
      log.info("Constructor -> AutoCloseableResources_Second");
    }

    public void doSomething() {
      log.info("Something -> AutoCloseableResources_Second");
    }

    @Override
    public void close() throws Exception {
      log.info("Closed AutoCloseableResources_Second");
    }
  }
}
