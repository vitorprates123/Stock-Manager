package model;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface represents the calculations that can be done on a stock.
 */
public interface Calculations {
  /**
   * This method calculates the gain or loss of a stock between two given dates.
   *
   * @param price     the price of the stock.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @return the gain or loss of the stock as a double.
   */

  double gainLossCheck(List<StockInformation> price, LocalDate startDate, LocalDate endDate);

  /**
   * This method calculates the moving average of a stock.
   *
   * @param price the price of the stock.
   * @param date  the date we are going to look at.
   * @param days  the number of days we are going to look at.
   * @return the moving average of the stock as a double.
   */

  double movingAverage(List<StockInformation> price, LocalDate date, int days);

  /**
   * This method calculates the crossover dates of a stock.
   *
   * @param price     the price of the stock.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @param days      the number of days we are going to look at.
   * @return the crossover dates of the stock as a list of LocalDate.
   */

  List<LocalDate> crossoverDates(List<StockInformation> price,
                                 LocalDate startDate, LocalDate endDate, int days);
}
