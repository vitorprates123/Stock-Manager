
import java.io.InputStreamReader;

import controller.Controller;
import controller.GUIStocksController;
import controller.StocksController;
import model.Calculations;
import model.StockCalculator;
import view.GUIStocksView;
import view.GUIView;
import view.StocksView;
import view.View;

/**
 * This class will run the program.
 */
public class ProgramRunner {
  /**
   * This is the main method that will run the program.
   *
   * @param args the arguments passed to the program.
   */
  public static void main(String[] args) {
    Calculations model = new StockCalculator();
    Readable in = new InputStreamReader(System.in);
    Controller controller;
    View view;
    GUIView view2;

    try {
      if (args.length > 0 && args[0].equals("-text")) {
        view = new StocksView();
        controller = new StocksController(model, view, in);
      } else {
        view2 = new GUIStocksView("Stocks");
        controller = new GUIStocksController(model, view2);
      }
      controller.construct();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}