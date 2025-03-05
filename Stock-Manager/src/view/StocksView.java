package view;

import java.time.LocalDate;
import java.util.List;

//View & StocksView- In our interface, we updated the interface to allow more
// displays to be shown because it needed to handle the updated
// functionality of our portfolio class. We added the
// displayDistribution, displayComposition, displayPerformance,
// and displayRebalance methods to the interface. We also added
// the displayRemoveStock method to the interface. We also added
// the displayAddStock method to the interface to handle the new
// functionality of adding a stock to the portfolio on a specific date.
// This way, we can have different displays and keep the code simple
// and concise, as well as adding minimalistic changes to the interface
// and class itself. This way, we don't have to extract code or simplify
// by adding more classes, when the class itself handles the displays
// and new functionality correctly and efficiently!
/**
 * A class that represents the view of a stock and displays the menu options to the user.
 */
public class StocksView implements View {


  /**
   * this method displays the menu.
   */
  @Override
  public void printMenu() {
    System.out.println("Welcome to the Stock Analyzer!");
    System.out.println("Please choose an option: You have 125 total queries"
            + " every minute! Use them wisely.");
    System.out.println("1. Examine gain or loss of a stock over a specified period.");
    System.out.println("2. Examine the x-day moving average of a stock.");
    System.out.println("3. Determine x-day crossovers for a stock.");
    System.out.println("4. Create A Portfolio.");
    System.out.println("5. Manage Portfolios.");
    System.out.println("6. Check Bar-Chart.");
    System.out.println("7. Quit Program >:).");
  }

  /**
   * this method gets the user choice.
   */
  @Override
  public void getUserChoice() {
    System.out.print("Enter your choice: ");
  }

  /**
   * this method gets the input from the user and outputs it.
   *
   * @param output the output to be displayed after user inputs.
   */
  @Override
  public void getInput(String output) {
    System.out.print(output);
  }

  /**
   * this method displays the gain or loss of a stock over a specified period.
   *
   * @param symbol    the ticker symbol of a stock.
   * @param startDate the start date of the period.
   * @param endDate   the end date of the period.
   * @param gainLoss  the gain or loss of the stock over the period.
   */
  @Override
  public void displayGainLoss(String symbol, LocalDate startDate,
                              LocalDate endDate, double gainLoss) {
    System.out.printf("The gain/loss for %s from %s to %s is: $%.2f%n",
            symbol, startDate, endDate, gainLoss);
  }

  /**
   * this method displays the x-day moving average of a stock.
   *
   * @param symbol    the ticker symbol of a stock.
   * @param date      the date of the moving average.
   * @param days      the number of days for the moving average.
   * @param movingAvg the moving average of the stock.
   */
  @Override
  public void displayMovingAverage(String symbol, LocalDate date,
                                   int days, double movingAvg) {
    System.out.printf("The %d-day moving average for %s on %s is: $%.2f%n",
            days, symbol, date, movingAvg);
  }

  /**
   * this method displays the x-day crossovers for a stock.
   *
   * @param symbol     the ticker symbol of a stock.
   * @param startDate  the start date of the period.
   * @param endDate    the end date of the period.
   * @param crossovers the list of dates where the crossovers occur.
   */
  @Override
  public void displayCrossovers(String symbol, LocalDate startDate,
                                LocalDate endDate, List<LocalDate> crossovers) {
    System.out.printf("x-day crossovers for %s from %s to %s are: %n",
            symbol, startDate, endDate);
    for (LocalDate crossoverDate : crossovers) {
      System.out.println(crossoverDate);
    }
  }

  /**
   * this method displays the creation of a portfolio.
   *
   * @param name the name of the portfolio is displayed.
   */
  @Override
  public void displayPortfolioCreation(String name) {
    System.out.println("Portfolio '" + name + "' created successfully.");
  }

  /**
   * this method displays the list of all portfolios.
   *
   * @param portfolios the list of all portfolios.
   */
  @Override
  public void displayPortfolios(List<String> portfolios) {
    System.out.println("Available Portfolios:");
    for (int i = 0; i < portfolios.size(); i++) {
      System.out.println((i + 1) + ". " + portfolios.get(i));
    }
  }

  /**
   * this method displays the management of a given portfolio the user chooses.
   *
   * @param name the name of the portfolio to be managed.
   */
  @Override
  public void displayManagingPortfolio(String name) {
    System.out.println("Managing Portfolio: " + name);
    System.out.println("1. Add Stock to Portfolio");
    System.out.println("2. Remove Stock from Portfolio");
    System.out.println("3. Check Composition of Portfolio");
    System.out.println("4. Check Distribution of Portfolio");
    System.out.println("5. Check Total Value of Portfolio");
    System.out.println("6. Rebalance Portfolio");
    System.out.println("7. Go Back to Main Menu");
  }

  /**
   * this method displays the addition of a stock to a portfolio.
   *
   * @param quantity      the shares of stock added.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is added.
   */
  @Override
  public void displayStockAddition(int quantity, String symbol,
                                   String portfolioName) {
    System.out.printf("Added %d shares of %s to portfolio '%s'.%n",
            quantity, symbol, portfolioName);
  }

  /**
   * this method displays the removal of a stock from a portfolio.
   *
   * @param quantity      the shares of stock removed.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is removed.
   */
  @Override
  public void displayStockRemoval(int quantity, String symbol,
                                  String portfolioName) {
    System.out.printf("Removed %d shares of %s from portfolio '%s'.%n",
            quantity, symbol, portfolioName);
  }

  /**
   * this method displays the composition of your portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date          is the date we look at to get the composition.
   * @param composition   is the composition of the portfolio.
   */
  @Override
  public void displayPortfolioComposition(String portfolioName,
                                          LocalDate date, String composition) {
    System.out.printf("The composition of portfolio '%s' on %s is: %s%n",
            portfolioName, date, composition);
  }

  /**
   * this method displays the distribution of your portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio where the distribution is displayed.
   * @param date          is the date we look at to get the distribution.
   * @param distribution  is the distribution of the portfolio.
   */
  @Override
  public void displayPortfolioDistribution(String portfolioName,
                                           LocalDate date, String distribution) {
    System.out.printf("The distribution of portfolio '%s' on %s is: %s%n",
            portfolioName, date, distribution);
  }

  /**
   * this method displays the total value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the total value.
   * @param totalValue    the total value of the portfolio.
   */
  @Override
  public void displayPortfolioValue(String portfolioName,
                                    LocalDate date, double totalValue) {
    System.out.printf("The total value of portfolio '%s' on %s is: $%.2f%n",
            portfolioName, date, totalValue);
  }

  /**
   * this method displays the rebalance of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the rebalance.
   */
  @Override
  public void displayPortfolioRebalance(String portfolioName, LocalDate date) {
    System.out.printf("Rebalanced portfolio '%s' on %s.%n", portfolioName, date);
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
  public void displayBarChart(String portfolioName,
                              LocalDate startDate,
                              LocalDate endDate,
                              List<String> performanceData) {
    System.out.printf("Performance of portfolio '%s' from %s to %s:%n",
            portfolioName, startDate, endDate);
    for (String data : performanceData) {
      System.out.println(data);
    }
  }

  /**
   * this method displays an error message whenever there is one given.
   *
   * @param message the error message to be displayed.
   */
  @Override
  public void displayError(String message) {
    System.out.println(message);
  }

  /**
   * this method quits the program.
   */
  @Override
  public void quitProgram() {
    System.out.println("Quitting the program");
  }
}