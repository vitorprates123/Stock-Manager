package view;

import java.time.LocalDate;
import java.util.List;


/**
 * An interface for the view to display the menu.
 */
public interface View {
  /**
   * this method displays the menu.
   */
  void printMenu();

  /**
   * this method gets the user choice.
   */
  void getUserChoice();

  /**
   * this method gets the input from the user and outputs it.
   *
   * @param output the output to be displayed after user inputs.
   */
  void getInput(String output);

  /**
   * this method displays the gain or loss of a stock over a specified period.
   *
   * @param symbol    the ticker symbol of a stock.
   * @param startDate the start date of the period.
   * @param endDate   the end date of the period.
   * @param gainLoss  the gain or loss of the stock over the period.
   */
  void displayGainLoss(String symbol, LocalDate startDate, LocalDate endDate, double gainLoss);

  /**
   * this method displays the x-day moving average of a stock.
   *
   * @param symbol    the ticker symbol of a stock.
   * @param date      the date of the moving average.
   * @param days      the number of days for the moving average.
   * @param movingAvg the moving average of the stock.
   */
  void displayMovingAverage(String symbol, LocalDate date, int days, double movingAvg);

  /**
   * this method displays the x-day crossovers for a stock.
   *
   * @param symbol     the ticker symbol of a stock.
   * @param startDate  the start date of the period.
   * @param endDate    the end date of the period.
   * @param crossovers the list of dates where the crossovers occur.
   */
  void displayCrossovers(String symbol, LocalDate startDate,
                         LocalDate endDate, List<LocalDate> crossovers);

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
   */
  void displayPortfolios(List<String> portfolios);

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
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date is the date we look at to get the composition.
   * @param composition is the composition of the portfolio.
   */
  void displayPortfolioComposition(String portfolioName, LocalDate date, String composition);

  /**
   * this method displays the distribution of your portfolio on a given date.
   * @param portfolioName the name of the portfolio where the distribution is displayed.
   * @param date is the date we look at to get the distribution.
   * @param distribution is the distribution of the portfolio.
   */
  void displayPortfolioDistribution(String portfolioName, LocalDate date, String distribution);

  /**
   * this method displays the total value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the total value.
   * @param totalValue    the total value of the portfolio.
   */
  void displayPortfolioValue(String portfolioName, LocalDate date, double totalValue);

  /**
   * this method displays the rebalance of a portfolio.
   * @param portfolioName the name of the portfolio.
   * @param date the date of the rebalance.
   */
  void displayPortfolioRebalance(String portfolioName, LocalDate date);

  /**
   * this method displays the performance of a portfolio over a given date range.
   * @param portfolioName the name of the portfolio.
   * @param startDate the start date of the range.
   * @param endDate the end date of the range.
   * @param performanceData the performance data of the portfolio.
   */
  void displayBarChart(String portfolioName, LocalDate startDate,
                       LocalDate endDate, List<String> performanceData);

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