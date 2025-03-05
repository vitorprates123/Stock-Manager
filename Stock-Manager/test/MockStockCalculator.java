import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import model.Calculations;
import model.StockCalculator;
import model.StockInformation;

/**
 * A mock implementation of the StockCalculator for testing purposes.
 * It extends the StockCalculator and logs method calls and parameters.
 * and implements the Calculations interface.
 */
public class MockStockCalculator extends StockCalculator implements Calculations {
  final StringBuilder log;


  /**
   * A constructor that constructs MockStockCalculator with the specified log.
   * @param log the StringBuilder to the log method and parameters.
   */
  public MockStockCalculator(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  /**
   * Logs the gainLossCheck method call and parameters, and returns a value.
   *
   * @param price the list of stock information.
   * @param startDate the start date for the gain/loss calculation.
   * @param endDate the end date for the gain/loss calculation.
   * @return a fixed gain/loss value for testing.
   */
  @Override
  public double gainLossCheck(List<StockInformation> price,
                              LocalDate startDate, LocalDate endDate) {
    log.append(String.format("gainLossCheck(%s, %s, %s)%n",
            price, startDate, endDate));
    return 25.0;
  }

  /**
   * Logs the movingAverage method calls and parameters, and
   * returns a value.
   * @param price the list of stock information.
   * @param date the date for the calculation.
   * @param days the number of days for the moving average.
   * @return a value of the moving average as a double.
   */
  @Override
  public double movingAverage(List<StockInformation> price,
                              LocalDate date, int days) {
    log.append(String.format("movingAverage(%s, %s, %d)\n",
            price, date, days));
    return 110.0;
  }

  /**
   * Logs the crossoverDates method call and parameters, and returns a list of dates.
   *
   * @param price the list of stock information.
   * @param startDate the start date.
   * @param endDate the end date.
   * @param days the number of days for the moving average.
   * @return a list of crossover dates for testing.
   */
  @Override
  public List<LocalDate> crossoverDates(List<StockInformation> price,
                                        LocalDate startDate,
                                        LocalDate endDate, int days) {
    log.append(String.format("crossoverDates(%s, %s, %s, %d)\n", price,
            startDate, endDate, days));
    return List.of(LocalDate.of(2023, 1, 1));
  }
}
