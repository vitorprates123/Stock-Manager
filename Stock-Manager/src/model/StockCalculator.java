package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that will calculate the gain/loss, moving average, and crossover dates for a given stock.
 */
public class StockCalculator implements Calculations {

  /**
   * This method calculates the gain or loss of a stock between two given dates.
   *
   * @param price     the price of the stock.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @return the gain or loss of the stock as a double.
   */
  @Override
  public double gainLossCheck(List<StockInformation> price, LocalDate startDate,
                              LocalDate endDate) throws IllegalArgumentException {
    if (price == null || price.isEmpty()) {
      throw new IllegalArgumentException("Price list cannot be null or empty.");
    }
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("End date cannot be before start date.");
    }

    double beginningPrice = 0;
    double endingPrice = 0;
    for (StockInformation stock : price) {
      if (stock.getDate().equals(startDate)) {
        if (startDate.isEqual(endDate)) {
          beginningPrice = stock.getOpen();
          endingPrice = stock.getClose();
        } else {
          beginningPrice = stock.getClose();

        }
        break;
      }
      if (stock.getDate().equals(endDate)) {
        endingPrice = stock.getClose();
      }
    }
    return endingPrice - beginningPrice;
  }

  /**
   * This method calculates the moving average of a stock.
   *
   * @param price the price of the stock.
   * @param date  the date we are going to look at.
   * @param days  the number of days we are going to look at.
   * @return the moving average of the stock as a double.
   */
  @Override
  public double movingAverage(List<StockInformation> price,
                              LocalDate date, int days) throws IllegalArgumentException {
    if (days <= 0) {
      throw new IllegalArgumentException("Number of days cannot be less than or equal to 0.");
    }
    if (date == null) {
      throw new IllegalArgumentException("Dates cannot be null");
    }
    if (price == null || price.isEmpty()) {
      throw new IllegalArgumentException("No prices were given.");
    }
    double sum = 0;
    int daysToSubtract = 1;
    LocalDate suppliedDate = date;
    boolean inRange = false;
    for (StockInformation stock : price) {
      if (daysToSubtract > days) {
        break;
      }
      if (inRange && !stock.getDate().equals(suppliedDate)) {
        while (!stock.getDate().equals(suppliedDate)) {
          suppliedDate = suppliedDate.minusDays(1);
        }
      }
      if (stock.getDate().equals(suppliedDate)) {
        inRange = true;
        sum += stock.getClose();
        suppliedDate = suppliedDate.minusDays(1);
        daysToSubtract += 1;
      }
    }
    if (sum == 0) {
      throw new IllegalArgumentException("Data for this date does not exist");
    }
    return sum / (daysToSubtract - 1);
  }

  /**
   * This method calculates the crossover dates of a stock.
   *
   * @param price     the price of the stock.
   * @param startDate the start date.
   * @param endDate   the end date.
   * @param days      the number of days we are going to look at.
   * @return the crossover dates of the stock as a list of LocalDate.
   */
  @Override
  public List<LocalDate> crossoverDates(List<StockInformation> price,
                                        LocalDate startDate,
                                        LocalDate endDate, int days)
          throws IllegalArgumentException {
    if (price == null || price.isEmpty()) {
      throw new IllegalArgumentException("Price list cannot be null or empty.");
    }
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null.");
    }
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("End date cannot be before start date.");
    }
    if (days <= 0) {
      throw new IllegalArgumentException("Number of days must be greater than 0.");
    }

    boolean finishedRange = false;
    boolean inRange = false;
    double currentMovingAverage = 0;
    List<LocalDate> crossoverDateList = new ArrayList<>();
    for (StockInformation stock : price) {
      if (startDate.equals(endDate)) {
        finishedRange = true;
      }
      if (stock.getDate().equals(endDate)) {
        currentMovingAverage = movingAverage(price, stock.getDate(), days);
        inRange = true;
        if (stock.getClose() > currentMovingAverage) {
          crossoverDateList.add(stock.getDate());
        }
        continue;
      }
      if (inRange) {
        if (finishedRange) {
          break;
        }
        currentMovingAverage = movingAverage(price, stock.getDate(), days);
        if (stock.getClose() > currentMovingAverage) {
          crossoverDateList.add(stock.getDate());
        }
        if (stock.getDate().equals(startDate)) {
          finishedRange = true;
          break;
        }
      }
    }
    if (!finishedRange) {
      throw new IllegalArgumentException("Data for this date range does not exist");
    }
    return crossoverDateList;
  }
}