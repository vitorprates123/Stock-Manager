
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import view.GUIStocksView;

/**
 * A MockStocksGUIView that creates a mock implementation for the GUI view.
 */
public class MockStocksGUIView extends GUIStocksView {
  final StringBuilder log;

  /**
   * A constructor that makes a MockStocksGUIView with the log.
   * @param log the String builder to log the method calls and parameters.
   */
  public MockStocksGUIView(StringBuilder log) {
    super("MockStocksGUIView");
    this.log = Objects.requireNonNull(log);
  }

  /**
   * Logs the setListener method call and parameter.
   *
   * @param listener the listener for the buttons.
   */
  @Override
  public void setListener(ActionListener listener) {
    log.append(String.format("setListener(%s)\n", listener));
  }

  /**
   * Logs the setInputLabel method call and parameter.
   *
   * @param labelText the label text for the input.
   */
  @Override
  public void setInputLabel(String labelText) {
    log.append(String.format("setInputLabel(%s)\n", labelText));
  }

  /**
   * Logs the clearInputs method call.
   */
  @Override
  public void clearInputs() {
    log.append("clearInputs()\n");
  }

  /**
   * Logs the display method call.
   */
  @Override
  public void display() {
    log.append("display()\n");
  }

  /**
   * Logs the getInput method call and parameter.
   *
   * @param output the output to be displayed after user inputs.
   */
  @Override
  public void getInput(String output) {
    log.append(String.format("getInput(%s)\n", output));
  }

  /**
   * Logs the displayPortfolioCreation method call and parameter.
   *
   * @param name the name of the portfolio created.
   */
  @Override
  public void displayPortfolioCreation(String name) {
    log.append(String.format("displayPortfolioCreation(%s)\n", name));
  }

  /**
   * Logs the displayPortfolios method call and parameter.
   *
   * @param portfolios the list of all portfolios.
   * @return the number of the chosen portfolio.
   */
  @Override
  public int displayPortfolios(List<String> portfolios) {
    log.append(String.format("displayPortfolios(%s)\n", portfolios));
    return -1; // or any other logic you want to simulate the selection
  }

  /**
   * Logs the displayManagingPortfolio method call and parameter.
   *
   * @param name the name of the portfolio to manage.
   */
  @Override
  public void displayManagingPortfolio(String name) {
    log.append(String.format("displayManagingPortfolio(%s)\n", name));
  }

  /**
   * Logs the displayStockAddition method call and parameters.
   *
   * @param quantity      the shares of stock added.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is added.
   */
  @Override
  public void displayStockAddition(int quantity,
                                   String symbol, String portfolioName) {
    log.append(String.format("displayStockAddition(%d, %s, %s)\n",
            quantity, symbol, portfolioName));
  }

  /**
   * Logs the displayStockRemoval method call and parameters.
   *
   * @param quantity      the shares of stock removed.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is removed.
   */
  @Override
  public void displayStockRemoval(int quantity,
                                  String symbol, String portfolioName) {
    log.append(String.format("displayStockRemoval(%d,"
            + " %s, %s)\n", quantity, symbol, portfolioName));
  }

  /**
   * Logs the displayPortfolioComposition method call and parameters.
   *
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date          is the date we look at to get the composition.
   * @param composition   is the composition of the portfolio.
   */
  @Override
  public void displayPortfolioComposition(String portfolioName,
                                          LocalDate date, String composition) {
    log.append(String.format("displayPortfolioComposition(%s,"
            + " %s, %s)\n", portfolioName, date, composition));
  }

  /**
   * Logs the displayPortfolioValue method call and parameters.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the total value.
   * @param totalValue    the total value of the portfolio.
   */
  @Override
  public void displayPortfolioValue(String portfolioName,
                                    LocalDate date, double totalValue) {
    log.append(String.format("displayPortfolioValue(%s, %s, %.2f)\n",
            portfolioName, date, totalValue));
  }

  /**
   * Logs the displayError method call and parameter.
   *
   * @param message the error message to be displayed.
   */
  @Override
  public void displayError(String message) {
    log.append(String.format("displayError(%s)\n", message));
  }

  /**
   * Logs the quitProgram method call.
   */
  @Override
  public void quitProgram() {
    log.append("quitProgram()\n");
  }
}
