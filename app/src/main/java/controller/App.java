package controller;

import controller.ControllerManager;
import view.ConsoleView;

/**
 * The App class serves as the entry point for the application.
 */
public class App {
  /**
   * The main method serves as the application starting point.
   *
   * @param args command line arguments passed to the application
   */
  public static void main(String[] args) {
    ControllerManager controller = ControllerManager.createWithHardCodedData();
    ConsoleView view = new ConsoleView(controller);
    view.start();
  }
}