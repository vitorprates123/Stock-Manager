import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.Portfolio;
import model.StockInformation;
import model.Stocks;

/**
 * A test class for testing the Portfolio class.
 */
public class PortfolioTest {
  private Portfolio portfolio;
  private Portfolio secondPortfolio;
  private Stocks stock1;
  private Stocks stock2;
  private Stocks stock3;

  @Before
  public void setUp() {
    portfolio = new Portfolio("Om's Portfolio");
    secondPortfolio = new Portfolio("Vic's Portfolio");

    StockInformation stockInfo1 = new StockInformation(LocalDate.of(2023,
            6, 1), 100.0, 115.0,
            95.0, 110.0, 1000);
    StockInformation stockInfo2 = new StockInformation(LocalDate.of(2023,
            6, 2), 110.0,
            125.0, 105.0, 120.0, 1500);

    stock1 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));

    StockInformation stockInfo3 = new StockInformation(LocalDate.of(2023,
            6, 1), 200.0,
            215.0, 195.0, 210.0, 2000);
    StockInformation stockInfo4 = new StockInformation(LocalDate.of(2023,
            6, 2), 210.0,
            225.0, 205.0, 220.0, 2500);


    stock2 = new Stocks("ADBE", Arrays.asList(stockInfo3, stockInfo4));

    StockInformation stockInfo5 = new StockInformation(LocalDate.of(2023,
            6, 1), 50.0,
            55.0, 45.0, 52.0, 500);
    StockInformation stockInfo6 = new StockInformation(LocalDate.of(2023,
            6, 2), 52.0,
            58.0, 48.0, 56.0, 700);

    stock3 = new Stocks("MSFT", Arrays.asList(stockInfo5, stockInfo6));
  }

  @Test
  public void testConstructorAndGetName() {
    assertEquals("Om's Portfolio", portfolio.getName());
  }

  @Test
  public void testGetName() {
    assertEquals("Om's Portfolio", portfolio.getName());
    assertEquals("Vic's Portfolio", secondPortfolio.getName());
  }

  @Test
  public void testAddStock() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    assertTrue(portfolio.getStocks().containsKey(stock1));
    assertEquals(10, portfolio.getStocks().get(stock1), 0.001);

    portfolio.addStock(stock1, 5, LocalDate.of(2023, 6, 2));
    assertEquals(15, portfolio.getStocks().get(stock1), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockInvalidQuantity() {
    portfolio.addStock(stock1, -10, LocalDate.of(2023, 6, 1));
  }

  @Test
  public void testAddStockInvalidDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 11, 10));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      portfolio.addStock(stock1, 10, LocalDate.of(2023, 11, 9));
    });
    assertEquals("Stock cannot be added before the first purchase date.", exception.getMessage());
  }

  @Test
  public void testRemoveStock() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.removeStock(stock1, 5, LocalDate.of(2023, 6, 2));
    assertEquals(5, portfolio.getStocks().get(stock1), 0.001);

    portfolio.removeStock(stock1, 5, LocalDate.of(2023, 6, 2));
    assertFalse(portfolio.getStocks().containsKey(stock1));
  }

  @Test
  public void testRemoveStockInvalidQuantity() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      portfolio.removeStock(stock1, 15, LocalDate.of(2023, 6, 2));
    });
    assertEquals("Quantity of stock to remove"
            + " must be less than or equal to current quantity.",
            exception.getMessage());
  }

  @Test
  public void testRemoveStockInvalidDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      portfolio.removeStock(stock1, 5, LocalDate.of(2023, 5, 1));
    });
    assertEquals("Stock cannot be removed before the first purchase date.", exception.getMessage());
  }

  @Test
  public void testCalculateTotalValue() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));

    double expectedValue = (10 * 110.0) + (5 * 210.0);
    assertEquals(expectedValue, portfolio.calculateTotalValue(LocalDate.of(2023, 6, 1)), 0.001);

    expectedValue = (10 * 120.0) + (5 * 220.0);
    assertEquals(expectedValue, portfolio.calculateTotalValue(LocalDate.of(2023, 6, 2)), 0.001);
  }

  @Test
  public void testAddPortfolio() {
    Portfolio.addPortfolio(secondPortfolio);

    List<String> portfolios = Portfolio.getPortfolios();
    assertTrue(portfolios.contains(secondPortfolio.getName()));
  }

  @Test
  public void testCompOfPortfolio() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    Map<Stocks, Double> composition = portfolio.compOfPortfolio(LocalDate.of(2023, 6, 2));
    assertEquals(1, composition.size());
  }

  @Test
  public void testCompOfPortfolioInvalidDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      portfolio.compOfPortfolio(LocalDate.of(2023, 5, 1));
    });
    assertEquals("Composition cannot be calculated"
            + " before the first purchase date.",
            exception.getMessage());
  }

  @Test
  public void testCompositionOfPortfolio() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));

    Map<Stocks, Double> composition = portfolio.compOfPortfolio(LocalDate.of(2023, 6, 2));
    assertEquals(10, composition.get(stock1), 0.001);
    assertEquals(5, composition.get(stock2), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoPortfolioName() {
    new Portfolio("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAdd0StockQuantity() {
    portfolio.addStock(stock1, 0, LocalDate.of(2023, 6, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullStock() {
    portfolio.addStock(null, 10, LocalDate.of(2023, 6, 1));
  }

  @Test
  public void testCalculateTotalValue0StocksBought() {
    double expectedValue = 0.0;
    assertEquals(expectedValue, portfolio.calculateTotalValue(LocalDate.of(2023, 6, 3)), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateTotalValueNoDate() {
    portfolio.calculateTotalValue(null);
  }

  @Test
  public void testPlotPerformance() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));
    Portfolio.addPortfolio(portfolio);
    int portfolioIndex = Portfolio.getPortfolios().indexOf(portfolio.getName());

    List<String> performanceData = Portfolio.plotPerformance(LocalDate.of(2023,
            6, 1), LocalDate.of(2023,
            6, 2), portfolioIndex);
    assertFalse(performanceData.isEmpty());
  }

  @Test
  public void testRebalancePortfolio() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock3, 15, LocalDate.of(2023, 6, 1));

    portfolio.rebalancePortfolio(Arrays.asList(33, 33, 34), LocalDate.of(2023, 6, 2));

    Map<Stocks, Double> composition = portfolio.compOfPortfolio(LocalDate.of(2023, 6, 2));

    assertEquals(3630.0, composition.get(stock1)
            * stock1.getStockInformation().get(0).getClose(), 0.01);
    assertEquals(7140.0, composition.get(stock2)
            * stock2.getStockInformation().get(0).getClose(), 0.01);
    assertEquals(1716.0, composition.get(stock3)
            * stock3.getStockInformation().get(0).getClose(), 0.01);
  }

  @Test
  public void testDistribution() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));

    Map<Stocks, Double> distribution = portfolio.distributionOfPortfolio(LocalDate.of(2023, 6, 1));
    double stock1Value = 10 * stock1.getStockInformation().get(0).getClose();
    double stock2Value = 5 * stock2.getStockInformation().get(0).getClose();

    assertEquals(stock1Value, distribution.get(stock1), 0.001);
    assertEquals(stock2Value, distribution.get(stock2), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockBeforeFirstPurchaseDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 5, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockNotInPortfolio() {
    portfolio.removeStock(stock1, 5, LocalDate.of(2023, 6, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMoreStocksThanInPortfolio() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.removeStock(stock1, 15, LocalDate.of(2023, 6, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveStockBeforeItWasAdded() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 2));
    portfolio.removeStock(stock1, 5, LocalDate.of(2023, 6, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionOfPortfolioBeforeFirstPurchaseDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.compOfPortfolio(LocalDate.of(2023, 5, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRebalancePortfolioWithNegativePercent() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.rebalancePortfolio(Arrays.asList(-50, 150), LocalDate.of(2023, 6, 2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRebalancePortfolioWithPercentSumNot100() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.rebalancePortfolio(Arrays.asList(30, 30), LocalDate.of(2023, 6, 2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRebalancePortfolioWithMorePercentsThanStocks() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.rebalancePortfolio(Arrays.asList(50, 25, 25), LocalDate.of(2023, 6, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlotPerformanceWithStartDateAfterEndDate() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    Portfolio.addPortfolio(portfolio);
    int portfolioIndex = Portfolio.getPortfolios().indexOf(portfolio.getName());

    Portfolio.plotPerformance(LocalDate.of(2023, 6, 2), LocalDate.of(2023, 6, 1), portfolioIndex);
  }


  @Test(expected = NullPointerException.class)
  public void testLoadPortfolioWithNullDate() {
    Portfolio.addPortfolio(portfolio);
    Portfolio.loadPortfolio(0, null);
  }

  @Test
  public void testSaveAndLoadPortfolio() {
    portfolio.addStock(stock1, 10, LocalDate.of(2023, 6, 1));
    portfolio.addStock(stock2, 5, LocalDate.of(2023, 6, 1));
    Portfolio.savePortfolio(portfolio, LocalDate.of(2023, 6, 2));

    Portfolio loadedPortfolio = Portfolio.loadPortfolio(0, LocalDate.of(2023, 6, 2));
    assertEquals(portfolio.getName(), loadedPortfolio.getName());
    assertEquals(portfolio.getStocks().size(), loadedPortfolio.getStocks().size());
  }
}
