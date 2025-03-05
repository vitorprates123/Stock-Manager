import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import model.StockInformation;
import model.Stocks;

/**
 * A test class to test the Stocks class.

 */
public class StocksTest {
  private Stocks stocks;
  private List<StockInformation> stockInformationList;

  @Before
  public void setUp() {
    stockInformationList = List.of(
            new StockInformation(LocalDate.of(2020, 1, 1),
                    100.0, 105.0, 95.0, 100.0, 1000000),
            new StockInformation(LocalDate.of(2020, 1, 2),
                    101.0, 106.0, 96.0, 115.0, 1000000),
            new StockInformation(LocalDate.of(2020, 1, 3),
                    102.0, 107.0, 97.0, 120.0, 1000000));
    stocks = new Stocks("ADBE", stockInformationList);
  }

  @Test
  public void testGetSymbol() {
    assertEquals("ADBE", stocks.getSymbol());
  }

  @Test
  public void testGetStockInformation() {
    assertEquals(stockInformationList, stocks.getStockInformation());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStocksEmptySymbol() {
    new Stocks("", stockInformationList);
  }

  @Test
  public void testEquals() {
    StockInformation stockInfo1 = new StockInformation(LocalDate.of(2023, 6, 1),
            100.0, 115.0, 95.0, 110.0, 1000);
    StockInformation stockInfo2 = new StockInformation(LocalDate.of(2023, 6, 2),
            110.0, 125.0, 105.0, 120.0, 1500);

    Stocks stock1 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));
    Stocks stock2 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));

    assertTrue(stock1.equals(stock2));
  }

  @Test
  public void testNotEqualsDifferentSymbol() {
    StockInformation stockInfo1 = new StockInformation(LocalDate.of(2023, 6, 1),
            100.0, 115.0, 95.0, 110.0, 1000);
    StockInformation stockInfo2 = new StockInformation(LocalDate.of(2023, 6, 2),
            110.0, 125.0, 105.0, 120.0, 1500);

    Stocks stock1 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));
    Stocks stock2 = new Stocks("ADBE", Arrays.asList(stockInfo1, stockInfo2));

    assertFalse(stock1.equals(stock2));
  }

  @Test
  public void testNotEqualsNull() {
    StockInformation stockInfo1 = new StockInformation(LocalDate.of(2023, 6, 1),
            100.0, 115.0, 95.0, 110.0, 1000);
    StockInformation stockInfo2 = new StockInformation(LocalDate.of(2023, 6, 2),
            110.0, 125.0, 105.0, 120.0, 1500);

    Stocks stock1 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));

    assertNotEquals(null, stock1);
  }

  @Test
  public void testEqualsSameObject() {
    StockInformation stockInfo1 = new StockInformation(LocalDate.of(2023, 6, 1),
            100.0, 115.0, 95.0, 110.0, 1000);
    StockInformation stockInfo2 = new StockInformation(LocalDate.of(2023, 6, 2),
            110.0, 125.0, 105.0, 120.0, 1500);

    Stocks stock1 = new Stocks("AAPL", Arrays.asList(stockInfo1, stockInfo2));

    assertEquals(stock1, stock1);
  }
}