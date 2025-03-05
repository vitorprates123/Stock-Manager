package controller;

/**
 * The Controller interface acts as a communicating factor between the Model and the View.
 * The Controller receives user input from the View, processes it (which may involve,
 * updating the Model), and returns the results to the View.
 * This interface defines a single method, construct, which is responsible for,
 * initiating the controller's operations.
 */
public interface Controller {
  /**
   * The construct method is called to start the controller's operations.
   * This method should contain the logic for handling user input and updating the
   * Model and View as necessary. The specific implementation will depend on the
   * requirements of the application.
   */
  void construct();
}