package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The IPortfolio interface represents a portfolio of stocks in a stock market application.
 * It provides methods for managing a portfolio, such as adding and removing stocks,
 * calculating the total value of the portfolio, and rebalancing the portfolio.
 * Each method is designed to handle a specific operation related to portfolio management.
 * For example, the addStock method is used to,
 * add a specific quantity of a stock to the portfolio on a given date,
 * while the removeStock method is used to,
 * remove a specific quantity of a stock from the portfolio on a given date.
 * The interface also provides methods for,
 * retrieving information about the portfolio,
 * such as its composition and distribution on a given date,
 * and the first purchase date of the portfolio.
 */
public interface IPortfolio {

  /**
   * This method will add a stock to the portfolio.
   *
   * @param stock    is the stock we will add to the portfolio.
   * @param quantity is the number of shares
   *                 of stock we will add to the portfolio.
   * @param date     is the date we will add the stock to the portfolio.
   */
  void addStock(Stocks stock, double quantity, LocalDate date);

  /**
   * This method will remove a stock to the portfolio.
   *
   * @param stock    is the stock we will remove to the portfolio.
   * @param quantity is the number of shares
   *                 of stock we will remove from the portfolio.
   * @param date     is the date we will remove the stock from the portfolio.
   */
  void removeStock(Stocks stock, double quantity, LocalDate date);

  /**
   * This method outputs the composition of the portfolio on a given date.
   *
   * @param date the date for which the composition is to be returned.
   * @return a map with stocks and their quantities.
   */
  Map<Stocks, Double> compOfPortfolio(LocalDate date);

  /**
   * This method outputs the distribution of a portfolio on a given date.
   *
   * @param date the date for which the distribution is to be returned.
   * @return a map with stocks and their values.
   */
  Map<Stocks, Double> distributionOfPortfolio(LocalDate date);

  /**
   * This method will calculate the total value
   * of the portfolio on a given date in.
   *
   * @param date is the date we will calculate
   *             the total value of the portfolio on.
   * @return the total value of the portfolio
   *          on the given date as a double.
   */
  double calculateTotalValue(LocalDate date);

  /**
   * This method will return the first purchase date of the portfolio.
   *
   * @return the first purchase date of the portfolio.
   */
  LocalDate getFirstPurchaseDate();


  /**
   * This method will return the stocks in the portfolio.
   *
   * @return a map of stocks in the portfolio.
   */
  Map<Stocks, Double> getStocks();


  /**
   * This method will rebalance the portfolio to the desired weights of the distributed value.
   *
   * @param percents the desired percentages of the stocks in the portfolio.
   * @param date     the date of the rebalance.
   */
  void rebalancePortfolio(List<Integer> percents, LocalDate date);
}