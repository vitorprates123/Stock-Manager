import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import model.StockInformation;

/**
 * A test class to test the StockInformation class.
 */
public class StockInformationTest {
  private StockInformation stockInformation;

  @Before
  public void setUp() {
    stockInformation = new StockInformation(LocalDate.of(2018, 1, 1),
            100.0, 105.0, 95.0, 100.0, 1000000);
  }

  @Test
  public void testGetOpen() {
    assertEquals(100.0, stockInformation.getOpen(), 0.001);
  }

  @Test
  public void testGetClose() {
    assertEquals(100.0, stockInformation.getClose(), 0.001);
  }

  @Test
  public void testGetHigh() {
    assertEquals(105.0, stockInformation.getHigh(), 0.001);
  }

  @Test
  public void testGetLow() {
    assertEquals(95.0, stockInformation.getLow(), 0.001);
  }

  @Test
  public void testGetVolume() {
    assertEquals(1000000, stockInformation.getVolume());
  }

  @Test
  public void testGetDate() {
    assertEquals(LocalDate.of(2018, 1, 1), stockInformation.getDate());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues() {
    new StockInformation(LocalDate.of(2018, 1, 1), -100.0,
            -105.0, -95.0, -100.0, -1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues1() {
    new StockInformation(LocalDate.of(2018, 1, 1), -100.0,
            105.0, 95.0, 100.0, 1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues2() {
    new StockInformation(LocalDate.of(2018, 1, 1), 100.0,
            -105.0, 95.0, 100.0, 1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues3() {
    new StockInformation(LocalDate.of(2018, 1, 1), 100.0,
            105.0, -95.0, 100.0, 1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues4() {
    new StockInformation(LocalDate.of(2018, 1, 1), 100.0,
            105.0, 95.0, -100.0, 1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNegativeValues5() {
    new StockInformation(LocalDate.of(2018, 1, 1), 100.0,
            105.0, 95.0, 100.0, -1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNoDate() {
    new StockInformation(null, 100.0, 105.0,
            95.0, 100.0, 1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationNoDateWithNegativeValues() {
    new StockInformation(null, -100.0, -105.0,
            -95.0, -100.0, -1000000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStockInformationHighLessThanLow() {
    new StockInformation(LocalDate.of(2018, 1, 1), 100.0,
            90.0, 95.0, 100.0, 1000000);
  }
}