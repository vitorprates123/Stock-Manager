package model;

import java.time.LocalDate;

/**
 * A class that stores all the information of a stock.
 */
public class StockInformation {
  private final double open;
  private final double close;
  private final double high;
  private final double low;
  private final long volume;
  private final LocalDate date;

  /**
   * A constructor that initializes the date of a stock, its open value,
   * high value, low value, close value, and volume.
   *
   * @param date   the date of the stock.
   * @param open   the open value of the stock.
   * @param high   the high value of the stock.
   * @param low    the low value of the stock.
   * @param close  the closing value of the stock.
   * @param volume the volume of the stock.
   */
  public StockInformation(LocalDate date, double open,
                          double high, double low, double close, long volume) {
    if (open < 0 || high < 0 || low < 0 || close < 0 || volume < 0) {
      throw new IllegalArgumentException("Stock values cannot be less than 0.");
    }
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be nothing.");
    }
    if (high < low) {
      throw new IllegalArgumentException("High value cannot be less than low value.");
    }

    this.open = open;
    this.close = close;
    this.high = high;
    this.low = low;
    this.volume = volume;
    this.date = date;
  }

  /**
   * A method that returns the open value of a stock.
   *
   * @return the open value of a stock as a double.
   */
  public double getOpen() {
    return open;
  }

  /**
   * A method that returns the closing value of a stock.
   *
   * @return the closing value of a stock as a double.
   */
  public double getClose() {
    return close;
  }

  /**
   * A method that returns the high value of a stock.
   *
   * @return the high value of a stock as a double.
   */
  public double getHigh() {
    return high;
  }

  /**
   * A method that returns the low value of a stock.
   *
   * @return the low value of a stock as a double.
   */
  public double getLow() {
    return low;
  }

  /**
   * A method that returns the volume of a stock.
   *
   * @return the volume of a stock as a long.
   */
  public long getVolume() {
    return volume;
  }

  /**
   * A method that returns the date of a stock.
   *
   * @return the date of a stock as a LocalDate.
   */
  public LocalDate getDate() {
    return date;
  }
}
