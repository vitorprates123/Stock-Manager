
import view.StocksView;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A MockStocksView that creates a mock implementation for the view.
 */
public class MockStocksView extends StocksView {
  final StringBuilder log;

  /**
   * A constructor that makes a MockStocksView with the log.
   * @param log the String builder to log the method calls and parameters.
   */
  public MockStocksView(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }


  /**
   * Logs the printMenu method call.
   */
  @Override
  public void printMenu() {
    log.append("displayMenu()\n");
  }


  /**
   * Logs the getUserChoice method call.
   */
  @Override
  public void getUserChoice() {
    log.append("getUserChoice()\n");
  }


  /**
   * Logs the getInput method call and parameter.
   *
   * @param output the output message to display to the user.
   */
  @Override
  public void getInput(String output) {
    log.append(String.format("getInput(%s)\n", output));
  }


  /**
   * Logs the displayGainLoss method call and parameters.
   *
   * @param symbol the ticker symbol.
   * @param startDate the start date.
   * @param endDate the end date.
   * @param gainLoss the calculated gain or loss.
   */
  @Override
  public void displayGainLoss(String symbol, LocalDate startDate,
                              LocalDate endDate, double gainLoss) {
    log.append(String.format("displayGainLoss(%s, %s, %s, %.2f)\n", symbol, startDate,
            endDate, gainLoss));
  }

  /**
   * Logs the displayMovingAverage method call and parameters.
   *
   * @param symbol the ticker symbol.
   * @param date the date for the moving average calculation.
   * @param days the number of days for the moving average.
   * @param movingAvg the calculated moving average.
   */
  @Override
  public void displayMovingAverage(String symbol, LocalDate date, int days, double movingAvg) {
    log.append(String.format("displayMovingAverage(%s, %s, %d, %.2f)\n",
            symbol, date, days, movingAvg));
  }

  /**
   * Logs the displayCrossovers method call and parameters.
   *
   * @param symbol the ticker symbol
   * @param startDate the start date.
   * @param endDate the end date.
   * @param crossovers the list of crossover dates.
   */
  @Override
  public void displayCrossovers(String symbol, LocalDate startDate, LocalDate endDate,
                                List<LocalDate> crossovers) {
    log.append(String.format("displayCrossovers(%s, %s, %s, %s)\n",
            symbol, startDate, endDate, crossovers));
  }

  /**
   * Logs the displayPortfolioCreation method call and parameter.
   *
   * @param name the name of the new portfolio.
   */

  @Override
  public void displayPortfolioCreation(String name) {
    log.append(String.format("displayPortfolioCreation(%s)\n", name));
  }

  /**
   * Logs the displayPortfolios method call.
   *
   * @param portfolios the list of portfolios.
   */
  @Override
  public void displayPortfolios(List<String> portfolios) {
    log.append("displayPortfolios()\n");
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
   * @param quantity the shares of the stock added.
   * @param symbol the ticker symbol.
   * @param portfolioName the name of the portfolio.
   */
  @Override
  public void displayStockAddition(int quantity, String symbol, String portfolioName) {
    log.append(String.format("displayStockAddition(%d, %s, %s)\n",
            quantity, symbol, portfolioName));
  }

  /**
   * this method displays the removal of a stock from a portfolio.
   *
   * @param quantity      the shares of stock removed.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is removed.
   */
  @Override
  public void displayStockRemoval(int quantity, String symbol, String portfolioName) {
    log.append(String.format("displayStockRemoval(%d, %s, %s)\n",
            quantity, symbol, portfolioName));
  }

  /**
   * this method displays the composition of your portfolio on a given date.
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date is the date we look at to get the composition.
   * @param composition is the composition of the portfolio.
   */
  @Override
  public void displayPortfolioComposition(String portfolioName,
                                          LocalDate date, String composition) {
    log.append(String.format("displayPortfolioComposition(%s, %s, %s)\n",
            portfolioName, date, composition));
  }

  /**
   * this method displays the distribution of your portfolio on a given date.
   * @param portfolioName the name of the portfolio where the distribution is displayed.
   * @param date is the date we look at to get the distribution.
   * @param distribution is the distribution of the portfolio.
   */
  @Override
  public void displayPortfolioDistribution(String portfolioName,
                                           LocalDate date, String distribution) {
    log.append(String.format("displayPortfolioDistribution(%s, %s, %s)\n",
            portfolioName, date, distribution));
  }

  /**
   * Logs the displayPortfolioValue method call and parameters.
   *
   * @param portfolioName the name of the portfolio.
   * @param date the date for the value calculation.
   * @param totalValue the calculated total value.
   */
  @Override
  public void displayPortfolioValue(String portfolioName, LocalDate date, double totalValue) {
    log.append(String.format("displayPortfolioValue(%s, %s, %.2f)\n",
            portfolioName, date, totalValue));
  }

  /**
   * this method displays the rebalance of a portfolio.
   * @param portfolioName the name of the portfolio.
   * @param date the date of the rebalance.
   */
  @Override
  public void displayPortfolioRebalance(String portfolioName, LocalDate date) {
    log.append(String.format("displayPortfolioRebalance(%s, %s)\n",
            portfolioName, date));
  }

  /**
   * this method displays the performance of a portfolio over a given date range.
   *
   * @param portfolioName   the name of the portfolio.
   * @param startDate       the start date of the range.
   * @param endDate         the end date of the range.
   * @param performanceData the performance data of the portfolio.
   */
  @Override
  public void displayBarChart(String portfolioName, LocalDate startDate,
                              LocalDate endDate, List<String> performanceData) {
    log.append(String.format("displayBarChart(%s, %s, %s, %s)\n",
            portfolioName, startDate, endDate, performanceData));
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
   * Logs the exitProgram method call.
   */
  @Override
  public void quitProgram() {
    log.append("exitProgram()\n");
  }
}