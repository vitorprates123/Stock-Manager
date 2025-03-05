package model;

import java.util.List;

/**
 * Interface for creating files and reading the files as well.
 */
public interface FileCreator {

  /**
   * This method will write the stock data to a file.
   */
  void writeFile();

  /**
   * Reads the stock data from the file, and will return a list of stock information.
   *
   * @return a list of stock information that has the data from the file.
   */
  List<StockInformation> fileToStockInfo();
}