package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A class that reads the data of a given stock from.
 * the API.
 */
public class AlphaVantageAPI implements APIReader {

  private final String apiKey;

  private final String tickerSymbol;
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final String DIRECTORY_PATH = "stockDataFiles";

  /**
   * A constructor to make the AlphaVantageAPI object with.
   * the given apiKey and the ticker symbol of a stock.
   *
   * @param apiKey       the API key to access the API.
   * @param tickerSymbol the ticker symbol of the stock.
   */

  public AlphaVantageAPI(String apiKey, String tickerSymbol) {
    this.apiKey = apiKey;
    this.tickerSymbol = tickerSymbol;
  }

  /**
   * A method to read the data of the stock from the API.
   *
   * @return the data of the stock in a string format.
   * @throws IllegalArgumentException if the data read is not valid.
   */
  @Override
  public String readData() throws IllegalArgumentException {
    URL url;
    String csvFilePath = DIRECTORY_PATH + "/" + tickerSymbol + ".csv";
    if (Files.exists(Paths.get(csvFilePath))) {
      try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
        reader.readLine();
        String firstDataLine = reader.readLine();
        if (firstDataLine != null) {
          String[] dataValues = firstDataLine.split(",");
          LocalDate latestDate = LocalDate.parse(dataValues[0], dateFormatter);
          if (!latestDate.isBefore(LocalDate.now().minusDays(1))) {
            return "";
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + tickerSymbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + tickerSymbol);
    }

    if (output.toString().isEmpty() || output.toString().contains("Error Message")) {
      throw new IllegalArgumentException("Invalid stock symbol: " + tickerSymbol);
    }
    return output.toString();
  }
}