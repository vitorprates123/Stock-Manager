package view;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

//GUIView and GUIStocksView - We decided to add another interview
// for the GUIView to handle the code for the GUI separately as we
// knew that we wouldn't need some of the methods that were in the
// original view because they were specific to the text-based interface,
// this way, we were also able to implement GUIView simply and concisely
// as well. We wanted to make clear that our approach, though added
// more classes, was to make it so nothing was confusing,
// and everything was clear in our eyes.
/**
 * An interface for the view to display the menu but in GUI formatting.
 */
public interface GUIView {

  /**
   * this method sets the listener for the buttons.
   *
   * @param listener the listener for the buttons.
   */
  void setListener(ActionListener listener);

  /**
   * this method sets the input label for the user.
   *
   * @param labelText the label text for the input.
   */
  void setInputLabel(String labelText);

  /**
   * this method clears the input field.
   */
  void clearInputs();

  /**
   * this method displays the output to the user.
   */
  void display();

  /**
   * this method gets the input from the user and outputs it.
   *
   * @param output the output to be displayed after user inputs.
   */
  void getInput(String output);

  /**
   * this method displays the creation of a portfolio.
   *
   * @param name the name of the portfolio is displayed.
   */
  void displayPortfolioCreation(String name);

  /**
   * this method displays the list of all portfolios.
   *
   * @param portfolios the list of all portfolios.
   * @return the number of the chosen portfolio.
   */
  int displayPortfolios(List<String> portfolios);

  /**
   * this method displays the management of a given portfolio the user chooses.
   *
   * @param name the name of the portfolio to be managed.
   */
  void displayManagingPortfolio(String name);

  /**
   * this method displays the addition of a stock to a portfolio.
   *
   * @param quantity      the shares of stock added.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is added.
   */
  void displayStockAddition(int quantity, String symbol, String portfolioName);


  /**
   * this method displays the removal of a stock from a portfolio.
   *
   * @param quantity      the shares of stock removed.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is removed.
   */
  void displayStockRemoval(int quantity, String symbol, String portfolioName);

  /**
   * this method displays the composition of your portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date          is the date we look at to get the composition.
   * @param composition   is the composition of the portfolio.
   */
  void displayPortfolioComposition(String portfolioName, LocalDate date, String composition);

  /**
   * this method displays the total value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the total value.
   * @param totalValue    the total value of the portfolio.
   */
  void displayPortfolioValue(String portfolioName, LocalDate date, double totalValue);

  /**
   * this method displays an error message whenever there is one given.
   *
   * @param message the error message to be displayed.
   */
  void displayError(String message);

  /**
   * this method quits the program.
   */
  void quitProgram();
}
