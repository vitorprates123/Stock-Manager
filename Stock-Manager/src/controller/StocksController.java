package controller;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.APIReader;
import model.AlphaVantageAPI;
import model.CSVReader;
import model.Calculations;
import model.Portfolio;
import model.StockInformation;
import model.Stocks;
import view.View;

import static model.Portfolio.loadPortfolio;
import static model.Portfolio.plotPerformance;


/**
 * A controller class for managing stocks and portfolios.
 * This handles user interactions,
 * and communicates between the model and view.
 */

public class StocksController implements Controller {
  private static final String API_KEY = "GVOWNVFAUMGZOUBF";
  private final Calculations model;
  private final View view;

  private final Scanner scanner;

  /**
   * This is a constructor or StocksController with the specified model, view, and input source.
   *
   * @param model the model to use for calculations
   * @param view  the view to interact with the user
   * @param in    the input source for user input as readable.
   */

  public StocksController(Calculations model, View view, Readable in) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(in);
  }

  //StocksController - In this class we basically updated the controller
  // to manage the updated portfolio class. We added the logic to check
  // the distribution of the portfolio, the composition of the portfolio,
  // the performance of the portfolio, and the rebalance of the portfolio.
  // We also added the logic to remove a stock from the portfolio. Also,
  // we added more cases to our construct method to handle the new functionalities,
  // and see if the inputs work. To justify our changes,
  // the reason we did this is that our approach for the StocksController
  // is already simplified down since we have a lot of helper methods,
  // and we wanted to keep that same ideology when handling the new
  // functionalities from the portfolio, and it actually keeps the code
  // simple and well-designed as well because
  // its only little changes we are adding to the controller.
  /**
   * This method starts the functionality of the controller
   * and makes it, so it handles any inputs.
   */

  public void construct() {
    Portfolio.portfolioRefresh();
    while (true) {
      view.printMenu();
      view.getUserChoice();
      String leadingInput = scanner.nextLine().trim(); // remove leading and trailing whitespaces.
      int choice = Integer.parseInt(leadingInput);
      switch (choice) {
        case 1:
          checkGainOrLoss();
          break;
        case 2:
          checkMovingAverage();
          break;
        case 3:
          checkXDayCrossovers();
          break;
        case 4:
          createPortfolio();
          break;
        case 5:
          managePortfolios();
          break;
        case 6:
          checkBarChart();
          break;
        case 7:
          view.quitProgram();
          return;
        default:
          view.displayError("Invalid choice, Please enter a valid input.");
      }
    }
  }

  //helper method to get the date input with less error handling.
  private LocalDate getDateInput() {
    int year = 0;
    while (true) {
      view.getInput("Enter the year(yyyy): ");
      String yearInput = scanner.nextLine();
      try {
        year = Integer.parseInt(yearInput);
        if (yearInput.length() != 4 || year < 0) {
          throw new NumberFormatException();
        }
        break;
      } catch (NumberFormatException e) {
        view.displayError("Invalid year. Please enter a valid year (4 digits).");
      }
    }

    int month = 0;
    while (true) {
      view.getInput("Enter the month(mm): ");
      String monthInput = scanner.nextLine();
      if (monthInput.length() == 1) {
        monthInput = "0" + monthInput;
      }
      try {
        month = Integer.parseInt(monthInput);
        if (month < 1 || month > 12) {
          throw new NumberFormatException();
        }
        break;
      } catch (NumberFormatException e) {
        view.displayError("Invalid month. Please enter a valid month (1-12).");
      }
    }

    int day = 0;
    while (true) {
      view.getInput("Enter the day (dd): ");
      String dayInput = scanner.nextLine();
      if (dayInput.length() == 1) {
        dayInput = "0" + dayInput;
      }
      try {
        day = Integer.parseInt(dayInput);
        if (day < 1 || day > 31) {
          throw new NumberFormatException();
        }
        break;
      } catch (NumberFormatException e) {
        view.displayError("Invalid day. Please enter a valid day (1-31).");
      }
    }

    try {
      return LocalDate.of(year, month, day);
    } catch (DateTimeException e) {
      view.displayError("Invalid date. Please enter a valid date.");
      return null;
    }
  }

  // helper method to check the gain or loss of a stock.
  private void checkGainOrLoss() {
    view.getInput("Enter stock symbol:");
    String symbol = scanner.nextLine();
    view.getInput("ENTER START DATE\n");
    LocalDate startDate = getDateInput();
    view.getInput("ENTER END DATE\n");
    LocalDate endDate = getDateInput();

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (stockInfo.isEmpty()) {
      return;
    }

    double gainLoss = model.gainLossCheck(stockInfo, startDate, endDate);
    view.displayGainLoss(symbol, startDate, endDate, gainLoss);
  }

  // helper method to check the moving average of a stock.
  private void checkMovingAverage() {
    view.getInput("Enter stock symbol:");
    String symbol = scanner.nextLine();
    view.getInput("ENTER DATE\n");
    LocalDate date = getDateInput();
    view.getInput("Enter the number of days for moving average:");
    int days = Integer.parseInt(scanner.nextLine());

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (stockInfo.isEmpty()) {
      return;
    }

    double movingAvg = model.movingAverage(stockInfo, date, days);
    view.displayMovingAverage(symbol, date, days, movingAvg);
  }

  // helper method to check the x amount of days crossovers.
  private void checkXDayCrossovers() {
    view.getInput("Enter stock symbol:");
    String symbol = scanner.nextLine();
    view.getInput("Enter the number of days for crossover:");
    int days = Integer.parseInt(scanner.nextLine());
    view.getInput("ENTER START DATE\n");
    LocalDate startDate = getDateInput();
    view.getInput("ENTER END DATE\n");
    LocalDate endDate = getDateInput();

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (stockInfo.isEmpty()) {
      return;
    }

    List<LocalDate> crossovers = model.crossoverDates(stockInfo,
            startDate, endDate, days);
    view.displayCrossovers(symbol, startDate, endDate, crossovers);
  }

  // helper method to create a portfolio.
  private void createPortfolio() {
    view.getInput("Enter the name of the new portfolio:");
    String name = scanner.nextLine();
    Portfolio portfolio = new Portfolio(name);
    Portfolio.addPortfolio(portfolio);
    view.displayPortfolioCreation(name);
  }

  // helper method to assist managing portfolios and their options.
  private void managePortfolios() {
    List<String> portfolios = Portfolio.getPortfolios();
    if (portfolios.isEmpty()) {
      view.displayError("No portfolios available. Please create a portfolio first.");
      return;
    }

    view.displayPortfolios(portfolios);
    view.getInput("Select a portfolio to manage:");
    int portfolioIndex = Integer.parseInt(scanner.nextLine()) - 1;
    if (portfolioIndex < 0 || portfolioIndex >= portfolios.size()) {
      view.displayError("Invalid portfolio selection.");
      return;
    }
    manageSelectedPortfolio(portfolioIndex);
  }

  // helper method to manage a selected portfolio.
  private void manageSelectedPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    while (true) {
      view.displayManagingPortfolio(portfolios.get(portfolioIndex));
      view.getUserChoice();
      String leadingInput = scanner.nextLine().trim();
      int choice = Integer.parseInt(leadingInput);
      switch (choice) {
        case 1:
          addStockToPortfolio(portfolioIndex);
          break;
        case 2:
          removeStockFromPortfolio(portfolioIndex);
          break;
        case 3:
          checkPortfolioComposition(portfolioIndex);
          break;
        case 4:
          checkPortfolioDistribution(portfolioIndex);
          break;
        case 5:
          checkPortfolioTotalValue(portfolioIndex);
          break;
        case 6:
          checkRebalance(portfolioIndex);
          break;
        case 7:
          return;
        default:
          view.displayError("Invalid choice. Please try again.");
      }
    }
  }

  // this helper adds stocks to a portfolio.
  private void addStockToPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    view.getInput("Enter stock symbol:");
    String symbol = scanner.nextLine();
    view.getInput("Enter quantity (whole number shares only):");
    int quantity = Integer.parseInt(scanner.nextLine());
    view.getInput("ENTER PURCHASE DATE\n");
    LocalDate date = getDateInput();

    if (date == null) {
      return;
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      portfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {

    }


    if (quantity <= 0) {
      view.displayError("Invalid quantity. Please enter a positive whole number.");
      return;
    }

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (stockInfo.isEmpty()) {
      return;
    }

    Stocks stock = new Stocks(symbol, stockInfo);

    portfolio.addStock(stock, quantity, date);

    System.out.println(mostRecentDates.get(currentPortfolioName)); //for debug

    view.displayStockAddition(quantity, symbol, portfolio.getName());
  }

  // this helper removes stocks from a portfolio.
  private void removeStockFromPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    view.getInput("Enter stock symbol:");
    String symbol = scanner.nextLine();
    view.getInput("Enter quantity (whole number shares only):");
    int quantity = Integer.parseInt(scanner.nextLine());
    view.getInput("ENTER SALE DATE\n");
    LocalDate date = getDateInput();

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      portfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    if (quantity <= 0) {
      view.displayError("Invalid quantity. Please enter a positive whole number.");
      return;
    }

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (stockInfo.isEmpty()) {
      return;
    }

    Stocks stock = new Stocks(symbol, stockInfo);
    try {
      portfolio.removeStock(stock, quantity, date);
      view.displayStockRemoval(quantity, symbol, portfolio.getName());
    } catch (Exception e) {
      view.displayError(e.getMessage());
    }
  }

  //helper method to check the composition of your portfolio.
  private void checkPortfolioComposition(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    view.getInput("ENTER DATE TO CHECK PORTFOLIO COMPOSITION\n");
    LocalDate date = getDateInput();

    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          portfolio = Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          break;
        } catch (Exception e) {
          dateCheck = dateCheck.minusDays(1);
        }
      }
    }


    Map<Stocks, Double> composition = portfolio.compOfPortfolio(date);
    StringBuilder formattedComposition = new StringBuilder();
    for (Map.Entry<Stocks, Double> entry : composition.entrySet()) {
      formattedComposition.append(entry.getKey().getSymbol()).append(": ")
              .append(String.format("%.2f", entry.getValue())).append("\n");
    }
    view.displayPortfolioComposition(portfolio.getName(),
            date, formattedComposition.toString());
  }

  //helper method to check the distribution of your portfolio.
  private void checkPortfolioDistribution(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    view.getInput("ENTER DATE TO CHECK PORTFOLIO DISTRIBUTION\n");
    LocalDate date = getDateInput();
    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          portfolio = Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          break;
        } catch (Exception e) {
          dateCheck = dateCheck.minusDays(1);
        }
      }
    }
    Map<Stocks, Double> distribution = portfolio.distributionOfPortfolio(date);
    StringBuilder formattedDistribution = new StringBuilder();
    for (Map.Entry<Stocks, Double> entry : distribution.entrySet()) {
      formattedDistribution.append(entry.getKey().getSymbol())
              .append(": $").append(String.format("%.2f", entry.getValue())).append("\n");
    }
    view.displayPortfolioDistribution(portfolio.getName(),
            date, formattedDistribution.toString());
  }

  // helper method to check the total value of your portfolio.
  private void checkPortfolioTotalValue(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    view.getInput("ENTER DATE TO CHECK PORTFOLIO'S TOTAL VALUE\n");
    LocalDate date = getDateInput();

    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          portfolio = Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          break;
        } catch (Exception e) {
          dateCheck = dateCheck.minusDays(1);
        }
      }
    }
    double totalValue = portfolio.calculateTotalValue(date);
    view.displayPortfolioValue(portfolio.getName(), date, totalValue);
  }

  //helper that receives percent inputs and makes sure the values add up to 100.
  private List<Integer> getPercentInput(Portfolio portfolio) {
    List<Integer> result = new ArrayList<>();
    Map<Stocks, Double> availableStocks = portfolio.getStocks();
    for (Stocks key : availableStocks.keySet()) {
      view.getInput("Enter the desired percentage for "
              + key.getSymbol() + ": ");
      Integer percent =  Integer.parseInt(scanner.nextLine());
      result.add(percent);
    }
    Integer total = 0;
    for (int value : result ) {
      total += value;
    }
    if (total > 100 || total < 100) {
      return new ArrayList<>();
    }
    return result;
  }

  //helper method to check the rebalance of your portfolio.
  private void checkRebalance(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    if (portfolios.isEmpty()) {
      view.displayError(
              "No portfolios available. Please create a portfolio first.");
      return;
    }

    view.getInput("ENTER DATE TO REBALANCE PORTFOLIO\n");
    LocalDate date = getDateInput();

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      Portfolio.loadPortfolio(portfolioIndex,
              mostRecentDates.get(currentPortfolioName));
      portfolio = Portfolio.loadPortfolio(portfolioIndex,
              mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    List<Integer> percentages = getPercentInput(portfolio);
    if (percentages.isEmpty()) {
      view.displayError("Please type valid percentages.");
      return;
    }
    try {
      portfolio.rebalancePortfolio(percentages, date);
      view.displayPortfolioRebalance(portfolio.getName(), date);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
  }

  // helper method to check the bar chart of your portfolio.
  private void checkBarChart() {
    List<String> portfolios = Portfolio.getPortfolios();
    if (portfolios.isEmpty()) {
      view.displayError("No portfolios available. Please create a portfolio first.");
      return;
    }

    view.displayPortfolios(portfolios);
    view.getInput("Select a portfolio to plot performance:");
    int portfolioIndex = Integer.parseInt(scanner.nextLine()) - 1;
    if (portfolioIndex < 0 || portfolioIndex >= portfolios.size()) {
      view.displayError("Invalid portfolio selection.");
      return;
    }

    view.getInput("ENTER START DATE FOR PERFORMANCE PLOTTING\n");
    LocalDate startDate = getDateInput();
    view.getInput("Enter END DATE FOR PERFORMANCE PLOTTING\n");
    LocalDate endDate = getDateInput();

    String currentPortfolioName = portfolios.get(portfolioIndex);
    try {
      plotPerformance(startDate, endDate, portfolioIndex);
      List<String> performanceData = plotPerformance(startDate, endDate, portfolioIndex);
      view.displayBarChart(currentPortfolioName, startDate, endDate, performanceData);
    } catch (IllegalArgumentException e) {
      view.displayError(e.getMessage());
    }
  }


  // this is a helper to get the information of a stock.

  //Changed this method to more gracefully handle situations where the file could not be read
  //or if there was an invalid ticker symbol
  private List<StockInformation> getStockInformation(String symbol) {
    APIReader apiReader = new AlphaVantageAPI(API_KEY, symbol);
    CSVReader csvReader = new CSVReader(apiReader, symbol);
    List<StockInformation> stockInfo = new ArrayList<>();
    try {
      csvReader.writeFile();
      stockInfo = csvReader.fileToStockInfo();
    } catch (IllegalArgumentException e) {
      stockInfo = csvReader.fileToStockInfo();
    } catch (RuntimeException e) {
      view.displayError("Data could not be found for " + symbol + ", please enter a valid symbol.");
      stockInfo = csvReader.fileToStockInfo();
    } catch (Exception e) {
      view.displayError("An error occurred while accessing the file for " + symbol);
    }
    return stockInfo;
  }
}