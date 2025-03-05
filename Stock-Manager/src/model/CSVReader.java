package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * A class that reads the stock and writes the data to a CSV file.
 */
public class CSVReader implements FileCreator {
  private static final String DIRECTORY_PATH = "res/stockDataFiles";
  private final APIReader stockData;
  private final String tickerSymbol;

  private final String csvFilePath;

  /**
   * A constructor that initializes the CSVReader object with the stock data and ticker symbol.
   *
   * @param stockData    is the data the API will use to get the
   *                     stock information.
   * @param tickerSymbol is the ticker symbol of the given stock.
   */
  public CSVReader(APIReader stockData, String tickerSymbol) {
    this.stockData = stockData;
    this.tickerSymbol = tickerSymbol;
    this.csvFilePath = DIRECTORY_PATH + "/" + tickerSymbol + ".csv";
  }

  /**
   * Reads the stock data from the file, and will return a list of stock information.
   *
   * @return a list of stock information that has the data from the file.
   */
  @Override
  public List<StockInformation> fileToStockInfo() {
    List<StockInformation> stockData = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      reader.readLine();
      reader.lines().forEach(line -> {
        String[] currentValues = line.split(",");
        LocalDate date = LocalDate.parse(currentValues[0]);
        double open = Double.parseDouble(currentValues[1]);
        double high = Double.parseDouble(currentValues[2]);
        double low = Double.parseDouble(currentValues[3]);
        double close = Double.parseDouble(currentValues[4]);
        long volume = Long.parseLong(currentValues[5]);
        StockInformation currentStockInformation = new StockInformation(date,
                open, high, low, close, volume);
        stockData.add(currentStockInformation);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stockData;
  }

  /**
   * This method will write the stock data to a file.
   *
   * @throws IllegalArgumentException if the file is already up-to-date.
   */
  @Override
  public void writeFile() throws IllegalArgumentException {
    try {
      stockData.readData();
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("No data found for" + tickerSymbol);
    }
    if (stockData.readData().equals("")) {
      throw new IllegalArgumentException("File already up to date");
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(
            DIRECTORY_PATH + "/" + tickerSymbol + ".csv"))) {
      writer.write(stockData.readData());
    } catch (IOException e) {
      throw new RuntimeException("File path does not exist");
    }
  }
}