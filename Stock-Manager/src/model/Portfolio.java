package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * The Portfolio class represents a collection of stocks owned by an investor.
 * It provides methods for managing the portfolio, such as adding and removing stocks,
 * calculating the total value of the portfolio, and rebalancing the portfolio.
 * Each stock in the portfolio is represented,
 * by a `Stocks` object, and the quantity of each stock is stored.
 * The portfolio also keeps track of the first purchase date for accurate calculations.
 * The class also provides functionality for saving and loading the portfolio state,
 * allowing the user to persist their portfolio across multiple sessions.
 */
public class Portfolio implements IPortfolio {
  private final String name;

  private final Map<Stocks, Double> stocks;
  private static final List<String> portfolios = new ArrayList<>();
  private LocalDate firstPurchaseDate;

  private static final Map<String, LocalDate> portfoliosMostRecent = new HashMap<>();

  /**
   * A constructor that will initialize the name of the portfolio.
   *
   * @param name the name of the portfolio.
   */
  public Portfolio(String name) {
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Name for a portfolio must be given.");
    }

    this.name = name;
    this.stocks = new HashMap<>();
  }

  private Portfolio(String name, Map<Stocks, Double> stocks, LocalDate firstPurchaseDate) {
    this.name = name;
    this.stocks = stocks;
    this.firstPurchaseDate = firstPurchaseDate;
  }

  /**
   * A method that will return the name of the portfolio.
   *
   * @return the name of the portfolio as a string.
   */
  public String getName() {
    return name;
  }

  //6. Adding Stocks with Specific Dates: The `addStock`
  // method was updated to include an extra parameter for the date.
  // This allows users to add stocks to their portfolio on
  // specific dates, providing more accurate and flexible portfolio management.

  /**
   * This method will add a stock to the portfolio.
   *
   * @param stock    is the stock we will add to the portfolio.
   * @param quantity is the number of shares
   *                 of stock we will add to the portfolio.
   * @param date     is the date we will add the stock to the portfolio.
   */
  @Override
  public void addStock(Stocks stock, double quantity,
                       LocalDate date) throws IllegalArgumentException {
    LocalDate mostRecentDate = portfoliosMostRecent.get(name);
    if (stock == null) {
      throw new IllegalArgumentException("Stock to add must be given.");
    }

    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity of stock to add must be greater than 0.");
    }
    if (mostRecentDate != null && date.isBefore(mostRecentDate)) {
      throw new IllegalArgumentException("Date cannot be before most recent date of a change.");
    }

    try {
      if (firstPurchaseDate != null && date.isBefore(firstPurchaseDate)) {
        throw new IllegalArgumentException("Stock cannot be added before the first purchase date.");
      }

      if (!stockExistsOnDate(stock, date)) {
        throw new IllegalArgumentException("Stock does not exist on the given date.");
      }


      if (firstPurchaseDate == null) {
        firstPurchaseDate = date;
      }

      if (stocks.containsKey(stock)) {
        Double currentQuantity = stocks.get(stock);
        stocks.put(stock, currentQuantity + quantity);
      } else {
        stocks.put(stock, quantity);
      }
      portfoliosMostRecent.put(name, date);
      savePortfolio(this, date);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid quantity of stock to add.");
    }
  }

  //helper method to assist the addStock method,
  // in checking if the stock exists on the given date.
  private boolean stockExistsOnDate(Stocks stock, LocalDate date) {
    for (StockInformation info : stock.getStockInformation()) {
      if (!info.getDate().isAfter(date)) {
        return true;
      }
    }
    return false;
  }

  //5. Stock Removal: Functionality was added to remove a stock from a portfolio.
  // This gives users more control over their portfolio and allows them to
  // easily adjust their investment strategy.

  /**
   * This method will remove a stock from the portfolio.
   *
   * @param stock    is the stock we will remove to the portfolio.
   * @param quantity is the number of shares
   *                 of stock we will remove from the portfolio.
   * @param date     is the date we will remove the stock from the portfolio.
   */
  @Override
  public void removeStock(Stocks stock, double quantity, LocalDate date) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock to remove must be given.");
    }

    if (quantity <= 0) {
      throw new IllegalArgumentException(
              "Quantity of stock to remove must be greater than 0.");
    }

    try {
      if (firstPurchaseDate != null && date.isBefore(firstPurchaseDate)) {
        throw new IllegalArgumentException(
                "Stock cannot be removed before the first purchase date.");
      }

      if (stocks.containsKey(stock)) {
        double currentQuantity = stocks.get(stock);
        if (currentQuantity < quantity) {
          throw new IllegalArgumentException(
                  "Quantity of stock to remove must be less than or equal to current quantity.");
        }

        if (currentQuantity == quantity) {
          stocks.remove(stock);
        } else {
          stocks.put(stock, currentQuantity - quantity);
        }
      } else {
        throw new IllegalArgumentException(
                "Stock to remove must be in the portfolio.");
      }
      savePortfolio(this, date);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid quantity of stock to remove.");
    }
  }

  //2. Composition Check: A method was added to check the composition
  // of the portfolio. This provides users with a detailed view
  // of their portfolio, including the types and quantities of stocks it contains.

  /**
   * This method outputs the composition of the portfolio on a given date.
   *
   * @param date the date for which the composition is to be returned.
   * @return a map with stocks and their quantities.
   */
  @Override
  public Map<Stocks, Double> compOfPortfolio(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException(
              "Date to calculate composition of portfolio must be given.");
    }

    Map<Stocks, Double> composition = new HashMap<>();
    for (Map.Entry<Stocks, Double> entry : stocks.entrySet()) {
      if (firstPurchaseDate != null && date.isBefore(firstPurchaseDate)) {
        throw new IllegalArgumentException(
                "Composition cannot be calculated before the first purchase date.");
      }

      Stocks stock = entry.getKey();
      double quantity = entry.getValue();

      for (StockInformation info : stock.getStockInformation()) {
        if (!info.getDate().isAfter(date)) {
          composition.put(stock, quantity);
          break;
        }
      }
    }
    return composition;
  }

  //1. Distribution Check: A method was added to check the
  // distribution of a portfolio. This allows users to understand
  // the proportion of each stock in their portfolio,
  // which is crucial for risk management and investment strategy.

  /**
   * This method outputs the distribution of the portfolio's value on a given date.
   *
   * @param date the date for which the distribution is to be returned.
   * @return a map with stocks and their values.
   */
  @Override
  public Map<Stocks, Double> distributionOfPortfolio(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException(
              "Date to calculate distribution of portfolio must be given.");
    }

    Map<Stocks, Double> distribution = new HashMap<>();
    double totalValue = calculateTotalValue(date);
    for (Map.Entry<Stocks, Double> entry : stocks.entrySet()) {
      Stocks stock = entry.getKey();
      Double quantity = entry.getValue();

      for (StockInformation info : stock.getStockInformation()) {
        if (!info.getDate().isAfter(date)) {
          double value = info.getClose() * quantity;
          distribution.put(stock, value);
          break;
        }
      }
    }
    return distribution;
  }

  /**
   * This method will calculate the total value
   * of the portfolio on a given date in.
   *
   * @param date is the date we will calculate
   *             the total value of the portfolio on.
   * @return the total value of the portfolio
   *          on the given date as a double.
   */
  @Override
  public double calculateTotalValue(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException(
              "Date to calculate total value must be given.");
    }


    double totalValue = 0.0;
    for (Map.Entry<Stocks, Double> entry : stocks.entrySet()) {
      Stocks stock = entry.getKey();
      Double quantity = entry.getValue();

      for (StockInformation info : stock.getStockInformation()) {
        if (info.getDate().equals(date)) {
          totalValue = totalValue + info.getClose() * quantity;
          break;
        }
      }
    }

    return totalValue;
  }

  /**
   * This method will add a new portfolio to a list of portfolios.
   *
   * @param portfolio the portfolio to add to the list of portfolios.
   */
  public static void addPortfolio(Portfolio portfolio) {
    if (portfolio == null) {
      throw new IllegalArgumentException("Portfolio to add must be given.");
    }
    portfolios.add(portfolio.getName());
  }


  /**
   * This method will return the most recent dates of the portfolios.
   *
   * @return a map of the most recent dates of the portfolios.
   */
  public static Map<String, LocalDate> getMostRecentDates() {
    return portfoliosMostRecent;
  }

  /**
   * This method will return the first purchase date of the portfolio.
   *
   * @return the first purchase date of the portfolio.
   */
  public LocalDate getFirstPurchaseDate() {
    return firstPurchaseDate;
  }

  /**
   * This method will save a portfolio in the file for later use.
   *
   * @param portfolio    is the portfolio that will be saved.
   * @param dateOfChange is the date of the change in the portfolio.
   */
  public static void savePortfolio(Portfolio portfolio,
                                   LocalDate dateOfChange) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = dateOfChange.format(format);

    String directoryPath = "res/portfolios";
    String directoryDatePath = directoryPath + "/" + formattedDate;
    File dateFolder = new File(directoryDatePath);

    String filePath = directoryDatePath
            + "/" + portfolio.getName() + ".xml";
    File portfolioFile = new File(filePath);


    if (!dateFolder.exists() || !dateFolder.isDirectory()) {
      try {
        dateFolder.mkdirs();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    if (!portfolioFile.exists()) {
      try {
        portfolioFile.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = docFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }

    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement("portfolio");
    doc.appendChild(rootElement);

    rootElement.setAttribute("name", portfolio.getName());

    for (Map.Entry<Stocks, Double> entry : portfolio.getStocks().entrySet()) {
      Stocks stock = entry.getKey();
      Double quantity = entry.getValue();

      Element stockElement = doc.createElement("stock");
      rootElement.appendChild(stockElement);

      Element symbolElement = doc.createElement("symbol");
      symbolElement.appendChild(doc.createTextNode(stock.getSymbol()));
      stockElement.appendChild(symbolElement);

      Element quantityElement = doc.createElement("quantity");
      quantityElement.appendChild(doc.createTextNode(String.valueOf(quantity)));
      stockElement.appendChild(quantityElement);
    }
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate firstPurchaseDate = portfolio.getFirstPurchaseDate();
    String firstDate = firstPurchaseDate.format(dateFormatter);

    rootElement.setAttribute("firstPurchaseDate", firstDate);

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = null;
    try {
      transformer = transformerFactory.newTransformer();
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException(e);
    }
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(portfolioFile);
    try {
      transformer.transform(source, result);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method will refresh the portfolio to make it so,
   * it has the updated data it needs.
   */
  public static void portfolioRefresh() {
    String directoryPath = "res/portfolios";
    List<String> files = new ArrayList<>();
    try {
      Files.walk(Paths.get(directoryPath))
              .forEach(path -> {
                if (Files.isRegularFile(path)) {
                  String fileName = path.getFileName().toString();
                  if (fileName.toLowerCase().endsWith(".xml")) {
                    String portfolioName = fileName.substring(0,
                            fileName.length() - 4); // remove ".xml"
                    files.add(portfolioName);

                    Path parentDir = path.getParent();
                    if (parentDir != null) {
                      String directoryName = parentDir.getFileName().toString();
                      LocalDate date = LocalDate.parse(directoryName,
                              DateTimeFormatter.ISO_LOCAL_DATE);
                      if (!portfoliosMostRecent.containsKey(portfolioName)
                              || date.isAfter(portfoliosMostRecent.get(portfolioName))) {
                        portfoliosMostRecent.put(portfolioName, date);
                      }
                    }
                  }
                }
              });
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (files != null) {
      for (String file : files) {
        String portfolioName = file.replace(".xml", "");
        if (!portfolios.contains(portfolioName)) {
          portfolios.add(portfolioName);
        }
      }
    }
  }

  /**
   * This method will load the portfolio from the file that we stored it in.
   *
   * @param portfolioIndex is the index of the portfolio in the list of portfolios.
   * @param dateOfChange   is the date of the change in the portfolio.
   * @return the portfolio that was loaded from the file.
   */
  public static Portfolio loadPortfolio(int portfolioIndex, LocalDate dateOfChange) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedDate = dateOfChange.format(format);

    String directoryPath = "res/portfolios";
    String directoryDatePath = directoryPath + "/" + formattedDate;

    String filePath = directoryDatePath + "/" + portfolios.get(portfolioIndex) + ".xml";
    File portfolioFile = new File(filePath);

    try {
      File directory = new File(directoryDatePath);
      if (!directory.exists()) {
        throw new FileNotFoundException("Directory not found: " + directoryDatePath);
      }
      if (!portfolioFile.exists()) {
        throw new FileNotFoundException("Portfolio file not found: " + filePath);
      }

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(portfolioFile);

      Element rootElement = document.getDocumentElement();
      String portfolioName = rootElement.getAttribute("name");

      NodeList stockList = rootElement.getElementsByTagName("stock");
      Map<Stocks, Double> loadStocks = new HashMap<>();
      for (int i = 0; i < stockList.getLength(); i++) {
        Element stockElement = (Element) stockList.item(i);
        String symbol = stockElement.getElementsByTagName("symbol").item(0).getTextContent();
        double quantity = Double.parseDouble(
                stockElement.getElementsByTagName(
                        "quantity").item(0).getTextContent());

        APIReader apiRead = new AlphaVantageAPI("GVOWNVFAUMGZOUBF", symbol);
        FileCreator csvRead = new CSVReader(apiRead, symbol);
        List<StockInformation> stockInfoList = csvRead.fileToStockInfo();
        Stocks currentStock = new Stocks(symbol, stockInfoList);
        loadStocks.put(currentStock, quantity);
      }
      String firstDate = rootElement.getAttribute("firstPurchaseDate");
      LocalDate firstPurchaseDate = LocalDate.parse(firstDate);
      return new Portfolio(portfolioName, loadStocks, firstPurchaseDate);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new IllegalArgumentException("Error loading portfolio: " + e.getMessage(), e);
    }
  }

  /**
   * This method will return a list of all portfolios.
   *
   * @return a list of all portfolios.
   */
  public static List<String> getPortfolios() {
    return portfolios;
  }

  /**
   * This method will return the stocks in the portfolio.
   *
   * @return a map of stocks in the portfolio.
   */
  public Map<Stocks, Double> getStocks() {
    return stocks;
  }


  //3. Rebalancing: Logic was added to rebalance the portfolio
  // to its intended weights. This is an important feature
  // for maintaining a specific risk level or investment strategy
  // over time, as market fluctuations can cause a portfolio's
  // actual weights to deviate from its target weights.

  /**
   * This method will rebalance the portfolio to the desired weights of the distributed value.
   *
   * @param percents the desired percentages of the stocks in the portfolio.
   * @param date     the date of the rebalance.
   */
  public void rebalancePortfolio(List<Integer> percents, LocalDate date) {

    double totalValue = calculateTotalValue(date);
    List<Double> percentsAsDecimal = new ArrayList<>();
    for (int percent : percents) {
      double decimal = (double) percent / 100;
      percentsAsDecimal.add(decimal);
    }
    LocalDate mostRecentDate = portfoliosMostRecent.get(name);
    if (firstPurchaseDate != null && date.isBefore(firstPurchaseDate)) {
      throw new IllegalArgumentException(
              "Stock cannot be rebalanced before the first purchase date.");
    }
    if (mostRecentDate != null && date.isBefore(mostRecentDate)) {
      throw new IllegalArgumentException(
              "Date cannot be before most recent date of a change.");
    }
    if (percents.isEmpty()) {
      throw new IllegalArgumentException("Percents cannot be null or empty.");
    }
    int sum = 0;
    for (int percent : percents) {
      if (percent < 0) {
        throw new IllegalArgumentException("Percent cannot be less than 0.");
      }
      sum += percent;
    }
    if (sum != 100) {
      throw new IllegalArgumentException("Percents must add up to 100.");
    }

    Set<Stocks> stockSet = stocks.keySet();
    Collection<Double> quantity = stocks.values();

    Map<Stocks, Double> valuesOfStocks = distributionOfPortfolio(date);

    List<Stocks> stockList = new ArrayList<>(stockSet);
    List<Double> quantityList = new ArrayList<>(quantity);
    int index = 0;

    for (double desiredPercent : percentsAsDecimal) {
      Stocks currentStock = stockList.get(index);
      double valueOnDate = 0.0;
      for (StockInformation stockInfo : currentStock.getStockInformation()) {
        if (stockInfo.getDate().equals(date)) {
          valueOnDate = stockInfo.getClose();
          break;
        }
      }
      double desiredValue = desiredPercent * totalValue;
      double totalStockValueOnDate = valuesOfStocks.get(currentStock);
      if (totalStockValueOnDate > desiredValue) {
        double difference = totalStockValueOnDate - desiredValue;
        double desiredQuantity = difference / valueOnDate;
        removeStock(currentStock, desiredQuantity, date);
      }
      if (totalStockValueOnDate < desiredValue) {
        double difference = desiredValue - totalStockValueOnDate;
        double desiredQuantity = difference / valueOnDate;
        addStock(currentStock, desiredQuantity, date);
      }
      index += 1;
    }
    portfoliosMostRecent.put(this.getName(), date);
    savePortfolio(this, date);
  }

  //here we have a helper method that will help us load the portfolio.
  private static Portfolio portfolioLoaderHelper(LocalDate date, int portfolioIndex) {
    Portfolio testPortfolio = new Portfolio(portfolios.get(portfolioIndex));
    List<String> portfolios = Portfolio.getPortfolios();
    String currentPortfolioName = portfolios.get(portfolioIndex);
    try {
      Portfolio.loadPortfolio(portfolioIndex, portfoliosMostRecent.get(currentPortfolioName));
      testPortfolio = loadPortfolio(portfolioIndex, portfoliosMostRecent.get(currentPortfolioName));
    } catch (Exception ignored) {
    }

    Portfolio portfolio = new Portfolio(portfolios.get(portfolioIndex));
    if (!testPortfolio.getStocks().isEmpty()) {
      LocalDate firstDate = testPortfolio.getFirstPurchaseDate();
      LocalDate dateCheck = date;
      while (dateCheck.isAfter(firstDate) || dateCheck.equals(firstDate)) {
        try {
          Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          portfolio = Portfolio.loadPortfolio(portfolioIndex, dateCheck);
          break;
        } catch (Exception e) {
          dateCheck = dateCheck.minusDays(1);
        }
      }
    }
    return portfolio;
  }


  //4. Performance Check: A method was added to check the performance
  // of a portfolio. This allows users to track the returns of their
  // portfolio over time, which is essential for evaluating the
  // success of their investment strategy.

  /**
   * Plots the performance of the portfolio over time using a text-based bar chart.
   *
   * @param startDate the start date of the performance plot.
   * @param endDate   the end date of the performance plot.
   * @return a list of strings representing the performance plot over time.
   */
  public static List<String> plotPerformance(LocalDate startDate,
                                             LocalDate endDate, int portfolioIndex) {
    invalidDates(startDate, endDate);

    long interval = calculateInterval(startDate, endDate);
    double maxValue = calculateMaxValue(startDate, endDate, interval, portfolioIndex);
    long scaleFactor = calculateScaleFactor(maxValue);

    List<String> performanceData = new ArrayList<>();
    LocalDate currentDate = startDate;
    Portfolio checkFirstPurchaseDate = portfolioLoaderHelper(endDate, portfolioIndex);
    while (!currentDate.isAfter(endDate)) {
      Portfolio currentPortfolio = portfolioLoaderHelper(currentDate, portfolioIndex);
      double totalValue = currentPortfolio.calculateTotalValue(currentDate);

      double barLength = calculateBarLength(scaleFactor, totalValue,
              currentDate, currentPortfolio, startDate, checkFirstPurchaseDate);
      String bar = createBar(barLength);

      performanceData.add(currentDate + ": " + bar);
      currentDate = currentDate.plusDays(interval);
    }

    performanceData.add("Scale: * = " + scaleFactor);

    return performanceData;
  }

  //helper method to check any invalid dates and call exceptions.
  private static void invalidDates(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date must be provided.");
    }

    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date.");
    }
  }

  //helper method  to help calculate interval counting for the bar chart.
  private static long calculateInterval(LocalDate startDate, LocalDate endDate) {
    long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
    long interval = 0;
    if (daysBetween < 365) {
      interval = 30;
      long intervalFactor = daysBetween / 30;

      if (intervalFactor <= 1) {
        interval = 1;
      }
    } else {
      interval = 365;
    }
    return interval;
  }

  //helper method to help calculate the max value for the bar chart.
  private static double calculateMaxValue(LocalDate startDate,
                                          LocalDate endDate, long interval, int portfolioIndex) {
    double maxValue = 0;
    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      Portfolio currentPortfolio = portfolioLoaderHelper(currentDate, portfolioIndex);
      double totalValue = currentPortfolio.calculateTotalValue(currentDate);
      maxValue += totalValue;
      currentDate = currentDate.plusDays(interval);
    }
    return maxValue;
  }

  //helper method to help limit the asterick length.
  private static long calculateScaleFactor(double maxValue) {
    long scaleFactor = (long) (maxValue / 50);
    if (scaleFactor < 1) {
      scaleFactor = 1;
    } else {
      scaleFactor = Math.round(scaleFactor);
    }
    return scaleFactor;
  }

  // helper method to calculate the bar length according to the scale factor as well.
  private static double calculateBarLength(long scaleFactor, double totalValue,
                                           LocalDate currentDate, Portfolio currentPortfolio,
                                           LocalDate startDate, Portfolio checkFirstPurchaseDate) {
    double barLength = (totalValue / scaleFactor);
    if (scaleFactor != 1 && totalValue == 0 && currentDate != startDate
            && currentDate.isAfter(checkFirstPurchaseDate.getFirstPurchaseDate())) {
      LocalDate findingWorkingDate = currentDate;
      while (totalValue == 0) {
        findingWorkingDate = findingWorkingDate.minusDays(1);
        totalValue = currentPortfolio.calculateTotalValue(findingWorkingDate);
      }
    }
    if (barLength < 1 && barLength != 0) {
      barLength = 1;
    } else {
      barLength = (int) Math.round(barLength);
    }
    return barLength;
  }

  //helper method to actually create the bar chart with its astericks showing.
  private static String createBar(double barLength) {
    StringBuilder bar = new StringBuilder();
    for (int i = 0; i < barLength; i++) {
      bar.append("*");
    }
    return bar.toString();
  }
}