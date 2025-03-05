
import model.IPortfolio;
import model.Portfolio;
import model.Stocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A mock implementation of the Portfolio class for testing purposes.
 * It extends the Portfolio class and logs method calls and parameters.
 */
public class MockPortfolio extends Portfolio implements IPortfolio {
  final StringBuilder log;
  private static List<Portfolio> portfolioList = new ArrayList<>();


  /**
   * Constructs a MockPortfolio with the specified log.
   *
   * @param log the StringBuilder to log method calls and parameters.
   */
  public MockPortfolio(StringBuilder log) {
    super("Om's Portfolio");
    this.log = Objects.requireNonNull(log);
  }


  @Override
  public double calculateTotalValue(LocalDate date) {
    log.append(String.format("calculateTotalValue(%s)\n", date));
    return 1000.0; // return a fixed value for testing purposes
  }

  @Override
  public Map<Stocks, Double> compOfPortfolio(LocalDate date) {
    log.append(String.format("compOfPortfolio(%s)\n", date));
    return Map.of(); // return an empty map for testing purposes
  }

  @Override
  public void rebalancePortfolio(List<Integer> percentages, LocalDate date) {
    log.append(String.format("rebalancePortfolio(%s, %s)\n", percentages, date));
  }

  @Override
  public Map<Stocks, Double> distributionOfPortfolio(LocalDate date) {
    log.append(String.format("distributionOfPortfolio(%s)\n", date));
    return Map.of(); // return an empty map for testing purposes
  }

  @Override
  public String getName() {
    log.append("getName()\n");
    return "Om's Portfolio"; // return a fixed name for testing purposes
  }

  @Override
  public Map<Stocks, Double> getStocks() {
    log.append("getStocks()\n");
    return Map.of(); // return an empty map for testing purposes
  }

  public static void addPortfolio(Portfolio portfolio) {
    portfolioList.add(portfolio);
  }

  public static List<String> getPortfolios() {
    return portfolioList.stream().map(Portfolio::getName)
            .collect(Collectors.toList());
  }
}
