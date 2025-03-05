package model;

import java.util.List;
import java.util.Objects;

/**
 * This class is used to store the stock information with its ticker symbol.
 */
public class Stocks {
  private final String symbol;
  private final List<StockInformation> stockInformation;

  /**
   * A constructor that creates a new stock object with the given symbol and stock information.
   *
   * @param symbol           The ticker symbol of the stock.
   * @param stockInformation The stock information of the stock.
   */
  public Stocks(String symbol, List<StockInformation> stockInformation) {
    if (symbol.isEmpty()) {
      throw new IllegalArgumentException("You must have a ticker symbol.");
    }

    this.symbol = symbol;
    this.stockInformation = stockInformation;
  }

  /**
   * This method will return a ticker symbol of a stock.
   *
   * @return The ticker symbol of the stock as a string.
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * This method will return a list of stockInformation when called upon.
   *
   * @return The stock information of the stock as a list of StockInformation objects.
   */
  public List<StockInformation> getStockInformation() {
    return stockInformation;
  }

  //We decided to add a simple method in this class
  // "equals" which will compare stocks to help remove stocks in a portfolio.
  //Before, we were using the "contains" method in the portfolio class to remove stocks.
  // This was not working as expected because the "contains" method was not
  // comparing the stocks correctly. We decided to try making an equals
  // method to compare the stocks and remove them from the portfolio.
  // This change was made to the Stocks class, and the Portfolio
  // class was updated to use the equals method to remove stocks.
  /**
   * This method will check if the stock object is equal to another stock object.
   *
   * @param o The object to compare to.
   * @return true if the stock objects are equal, false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Stocks stocks = (Stocks) o;
    return Objects.equals(symbol, stocks.symbol);
  }

  /**
   * This method will return the hash code of the stock object.
   *
   * @return the number that represents the hash code of the stock object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
}
