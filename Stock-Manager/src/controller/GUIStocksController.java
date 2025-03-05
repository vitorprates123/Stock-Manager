package controller;

import model.Calculations;
import model.Portfolio;
import model.StockInformation;
import model.Stocks;
import model.APIReader;
import model.AlphaVantageAPI;
import model.CSVReader;
import view.GUIView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import static model.Portfolio.loadPortfolio;

//GUIStocksController - This class was updated to manage the new functionalities
// of the Portfolio class. Logic was added to check the composition and to remove
// a stock from the portfolio. More cases were also added to the construct method
// to handle these new functionalities and check if the inputs work, but we
// handled it, so it works with a graphical user interface.

/**
 * A GUI controller class for managing stocks and portfolios.
 * This handles user interactions,
 * and communicates between the model and view.
 */
public class GUIStocksController implements Controller, ActionListener {
  private static final String API_KEY = "GVOWNVFAUMGZOUBF";
  private final GUIView view;

  /**
   * Constructs a new GUIStocksController with the specified model and view.
   * Initializes the model and view.
   *
   * @param model the model for calculations.
   * @param view  the view for the GUI.
   */
  public GUIStocksController(Calculations model, GUIView view) {
    this.view = view;
  }

  /**
   * This method starts the functionality of the controller
   * and makes it, so it handles any inputs.
   */
  @Override
  public void construct() {
    Portfolio.portfolioRefresh();
    view.display();
    view.setListener(this);
  }

  /**
   * Performs an action based on the action command from the ActionEvent.
   * The action command can be "Create Portfolio",
   * "Manage Portfolios", "Quit", or any other command.
   *
   * @param e the ActionEvent containing the action command.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Create Portfolio":
        createPortfolio();
        break;
      case "Manage Portfolios":
        managePortfolios();
        break;
      case "Quit":
        view.quitProgram();
        break;
      default:
        view.displayError("Unknown command: " + e.getActionCommand());
    }
  }

  //helper method to format a date's input in the GUI.
  private LocalDate getDateInput() {
    while (true) {
      try {
        String yearInput = JOptionPane.showInputDialog("Enter the year (yyyy):");
        if (yearInput == null) {
          return null;
        }

        String monthInput = JOptionPane.showInputDialog("Enter the month (mm):");
        if (monthInput == null) {
          return null;
        }
        String dayInput = JOptionPane.showInputDialog("Enter the day (dd):");
        if (dayInput == null) {
          return null;
        }

        int year = Integer.parseInt(yearInput);
        int month = Integer.parseInt(monthInput);
        int day = Integer.parseInt(dayInput);

        return LocalDate.of(year, month, day);
      } catch (DateTimeException | NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid date."
                + " Please enter a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }


  //helper method to display a message and prompt it for the input.
  private String promptInput(String message) {
    return JOptionPane.showInputDialog(message);
  }

  //helper method to create a new portfolio.
  private void createPortfolio() {
    String name = promptInput("Enter the name of the new portfolio:");
    if (name == null) {
      return;
    }
    Portfolio portfolio = new Portfolio(name);
    Portfolio.addPortfolio(portfolio);
    view.displayPortfolioCreation(name);
  }

  //helper method to manage the portfolios.
  private void managePortfolios() {
    List<String> portfolios = Portfolio.getPortfolios();
    if (portfolios.isEmpty()) {
      view.displayError("No portfolios available. Please create a portfolio first.");
      return;
    }

    int portfolioIndex = view.displayPortfolios(portfolios);
    if (portfolioIndex == -1) {
      view.displayError("No portfolio selected.");
      return;
    }
    manageSelectedPortfolio(portfolioIndex);
  }

  //helper method to handle the management of a selected portfolio.
  private void manageSelectedPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);

    while (true) {
      view.displayManagingPortfolio(currentPortfolioName);
      String choice = promptInput("Enter your choice:\n1. Buy Stock\n2. Sell Stock\n3."
              + " Check Composition\n4. Check Value\n5. Back to Menu");
      if (choice == null) {
        return;
      }

      switch (choice) {
        case "1":
          addStockToPortfolio(portfolioIndex);
          break;
        case "2":
          removeStockFromPortfolio(portfolioIndex);
          break;
        case "3":
          checkPortfolioComposition(portfolioIndex);
          break;
        case "4":
          checkPortfolioTotalValue(portfolioIndex);
          break;
        case "5":
          return;
        default:
          view.displayError("Invalid choice. Please try again.");
      }
    }
  }

  // Helper to add stock to portfolio
  private void addStockToPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    String symbol = promptInput("Enter stock symbol:");
    if (symbol == null) {
      return;
    }
    int quantity = Integer.parseInt(promptInput("Enter quantity (whole number shares only):"));
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

    List<StockInformation> stockInfo = getStockInformation(symbol);
    if (!stockInfo.isEmpty()) {
      Stocks stock = new Stocks(symbol, stockInfo);
      portfolio.addStock(stock, quantity, date);
      view.displayStockAddition(quantity, symbol, portfolio.getName());
    }
  }

  // Helper to remove stock from portfolio
  private void removeStockFromPortfolio(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    String symbol = promptInput("Enter stock symbol:");
    if (symbol == null) {
      return;
    }
    int quantity = Integer.parseInt(promptInput("Enter quantity (whole number shares only):"));
    LocalDate date = getDateInput();
    if (date == null) {
      return;
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      portfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }
    List<StockInformation> stockInfo = getStockInformation(symbol);

    if (!stockInfo.isEmpty()) {
      Stocks stock = new Stocks(symbol, stockInfo);
      portfolio.removeStock(stock, quantity, date);
      view.displayStockRemoval(quantity, symbol, portfolio.getName());
    }
  }

  // Helper to check portfolio composition
  private void checkPortfolioComposition(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    LocalDate date = getDateInput();
    if (date == null) {
      return;
    }
    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          loadPortfolio(portfolioIndex, dateCheck);
          portfolio = loadPortfolio(portfolioIndex, dateCheck);
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
    view.displayPortfolioComposition(portfolio.getName(), date, formattedComposition.toString());
  }

  // Helper to check portfolio total value
  private void checkPortfolioTotalValue(int portfolioIndex) {
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    Map<String, LocalDate> mostRecentDates = Portfolio.getMostRecentDates();
    LocalDate date = getDateInput();
    if (date == null) {
      return;
    }
    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    try {
      loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, mostRecentDates.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          loadPortfolio(portfolioIndex, dateCheck);
          portfolio = loadPortfolio(portfolioIndex, dateCheck);
          break;
        } catch (Exception e) {
          dateCheck = dateCheck.minusDays(1);
        }
      }
    }
    double totalValue = portfolio.calculateTotalValue(date);
    view.displayPortfolioValue(portfolio.getName(), date, totalValue);
  }

  // Helper to get stock information
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
