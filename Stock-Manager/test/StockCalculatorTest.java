import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.AlphaVantageAPI;
import model.CSVReader;
import model.StockCalculator;
import model.StockInformation;

import static org.junit.Assert.assertEquals;

/**
 * A test class to test the StockCalculator class.
 */
public class StockCalculatorTest {
  private StockCalculator test;
  private List<StockInformation> googleStockData;
  private Random random;

  private final LocalDate startDateRange = LocalDate.of(2014, 3, 27);
  private final LocalDate endDateRange = LocalDate.of(2024, 6, 5);

  @Before
  public void setUp() {
    test = new StockCalculator();
    AlphaVantageAPI testAPI = new AlphaVantageAPI("JVMZKLP2NJASHJPI", "GOOG");
    CSVReader testCSV = new CSVReader(testAPI, "GOOG");
    googleStockData = testCSV.fileToStockInfo();
    random = new Random();
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidDateOrderTest() {
    List<StockInformation> testList = new ArrayList<>();
    test.gainLossCheck(testList, LocalDate.of(2023, 12, 31),
            LocalDate.of(2021, 12, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noPrice() {
    List<StockInformation> testList = null;
    test.gainLossCheck(testList, LocalDate.of(2023, 12, 31),
            LocalDate.of(2021, 12, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noDate() {
    List<StockInformation> testList = null;
    test.gainLossCheck(testList, null, LocalDate.of(2021, 12, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void beforeFirstDateInData() {
    List<StockInformation> testList = null;
    test.gainLossCheck(testList, startDateRange.minusDays(1),
            LocalDate.of(2021, 12, 31));
  }

  @Test(expected = IllegalArgumentException.class)
  public void afterLastDateInData() {
    List<StockInformation> testList = null;
    test.gainLossCheck(testList, startDateRange, endDateRange.plusDays(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noDateInDataTest() {
    LocalDate startDate = LocalDate.of(2015, 12, 11);
    LocalDate endDate = LocalDate.of(2016, 6, 25);
    test.gainLossCheck(googleStockData, startDate, endDate);
  }

  @Test
  public void sameDateGainLossTest() {
    double result = test.gainLossCheck(googleStockData, LocalDate.of(2024,
            4, 15), LocalDate.of(2024,
            4, 15));
    double delta = 1e-12;
    assertEquals(-3.95, result, delta);
  }

  @Test
  public void basicCloseDatesGainLossTest() {
    double result = test.gainLossCheck(googleStockData, LocalDate.of(2024, 4, 15),
            LocalDate.of(2024, 4, 19));
    double delta = 1e-12;
    assertEquals(-0.61, result, delta);
  }

  @Test
  public void gainTest() {
    double result = test.gainLossCheck(googleStockData, LocalDate.of(2024,
            4, 19), LocalDate.of(2024,
            5, 29));
    double delta = 1e-12;
    assertEquals(21.68, result, delta);
  }

  @Test
  public void lossTest() {
    double result = test.gainLossCheck(googleStockData, LocalDate.of(2024,
            1, 30), LocalDate.of(2024,
            2, 27));
    double delta = 1e-12;
    assertEquals(-12.95, result, delta);
  }

  @Test
  public void randomTest() {
    for (int i = 0; i < 100; i++) {
      LocalDate startDate = generateRandomDate();
      LocalDate endDate = generateRandomDate();

      if (startDate.isAfter(endDate)) {
        LocalDate toSwap = startDate;
        startDate = endDate;
        endDate = toSwap;
      }

      try {
        double expected = calculateExpectedGainLoss(startDate, endDate);
        double result = test.gainLossCheck(googleStockData, startDate, endDate);

        double delta = 1e-12;
        assertEquals("Failed for dates: " + startDate
                + " to " + endDate, expected, result, delta);
      } catch (IllegalArgumentException e) {
        System.out.println("Dates not found in stock data: "
                + startDate + " to " + endDate);
      }
    }
  }

  private LocalDate generateRandomDate() {
    long startEpochDay = startDateRange.toEpochDay();
    long endEpochDay = endDateRange.toEpochDay();
    long randomEpochDay = startEpochDay + random.nextInt((int)
            (endEpochDay - startEpochDay + 1));
    return LocalDate.ofEpochDay(randomEpochDay);
  }

  private double calculateExpectedGainLoss(LocalDate startDate,
                                           LocalDate endDate) {
    Double startValue = null;
    Double endValue = null;

    for (StockInformation stockInfo : googleStockData) {
      if (stockInfo.getDate().equals(startDate)) {
        startValue = stockInfo.getClose();
      }
      if (stockInfo.getDate().equals(endDate)) {
        endValue = stockInfo.getClose();
      }
      if (startValue != null && endValue != null) {
        break;
      }
    }

    if (startValue == null || endValue == null) {
      throw new IllegalArgumentException("Start or end date not found in stock data");
    }

    return endValue - startValue;
  }

  @Test
  public void fromEarliestDateToLatestDateTest() {
    double result = test.gainLossCheck(googleStockData,
            startDateRange, endDateRange);
    double delta = 1e-12;
    assertEquals(-381.39, result, delta);
  }

  @Test
  public void monthEdgeCase() {
    LocalDate startDate = LocalDate.of(2024, 1, 31);
    LocalDate endDate = LocalDate.of(2024, 2, 1);
    double delta = 1e-12;
    double result = test.gainLossCheck(googleStockData, startDate, endDate);
    assertEquals(0.91, result, delta);
  }

  @Test
  public void yearEdgeCase() {
    LocalDate endDate = LocalDate.of(2024, 1, 2);
    LocalDate startDate = LocalDate.of(2023, 12, 29);
    double delta = 1e-12;
    double result = test.gainLossCheck(googleStockData, startDate, endDate);
    assertEquals(-1.37, result, delta);
  }

  //Moving Average Tests
  @Test(expected = IllegalArgumentException.class)
  public void negativeDaysTest() {
    LocalDate date = LocalDate.of(2023, 12, 29);
    test.movingAverage(googleStockData, date, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidDateTest() {
    LocalDate date = null;
    test.movingAverage(googleStockData, date, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void datesThatDoNotExistInFileTest() {
    LocalDate date = LocalDate.of(2016, 6, 25);
    test.movingAverage(googleStockData, date, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noPricesTest() {
    LocalDate date = LocalDate.of(2024, 6, 4);
    test.movingAverage(null, date, 2);
  }

  @Test
  public void basicMovingAverageTest() {
    LocalDate date = LocalDate.of(2024, 5, 29);
    double result = test.movingAverage(googleStockData, date, 5);
    double delta = 1e-12;
    assertEquals(176.962, result, delta);
  }

  @Test
  public void dateWhereOneOfTheDatesBeforeItDoesNotExistTest() {
    LocalDate date = LocalDate.of(2024, 5, 28);
    double result = test.movingAverage(googleStockData, date, 5);
    double delta = 1e-12;
    assertEquals(177.39, result, delta);
  }

  @Test
  public void firstDateTest() {
    LocalDate date = startDateRange;
    double result = test.movingAverage(googleStockData, date, 5);
    double delta = 1e-12;
    assertEquals(558.46, result, delta);
  }

  @Test
  public void randomMovingAverageTest() {
    for (int i = 0; i < 100; i++) {
      LocalDate randomDate = generateRandomDate();
      int randomDays = random.nextInt(10) + 1;

      try {
        double result = test.movingAverage(googleStockData, randomDate, randomDays);
        double expected = calculateExpectedMovingAverage(randomDate, randomDays);
        double delta = 1e-12;
        assertEquals("Failed for date: " + randomDate
                + " and days: " + randomDays, expected, result, delta);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid test case for date: "
                + randomDate + " and days: " + randomDays + ". " + e.getMessage());
      }
    }
  }

  private double calculateExpectedMovingAverage(LocalDate date, int days) {
    double sum = 0;
    int daysToSubtract = 1;
    LocalDate suppliedDate = date;
    boolean inRange = false;

    for (StockInformation stock : googleStockData) {
      if (daysToSubtract > days) {
        break;
      }
      if (inRange && !stock.getDate().equals(suppliedDate)) {
        while (!stock.getDate().equals(suppliedDate)) {
          suppliedDate = suppliedDate.minusDays(1);
        }
      }
      if (stock.getDate().equals(suppliedDate)) {
        inRange = true;
        sum += stock.getClose();
        suppliedDate = suppliedDate.minusDays(1);
        daysToSubtract += 1;
      }
    }

    if (sum == 0) {
      throw new IllegalArgumentException("Data for this date does not exist");
    }

    return sum / days;
  }

  //Tests for Crossover Dates
  @Test(expected = IllegalArgumentException.class)
  public void negativeDaysCrossoverDatesTest() {
    LocalDate startDate = LocalDate.of(2023, 12, 29);
    LocalDate endDate = LocalDate.of(2024, 5, 29);
    test.crossoverDates(googleStockData, startDate, endDate, -3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void endDateBeforeStartDateTest() {
    LocalDate startDate = LocalDate.of(2023, 12, 29);
    LocalDate endDate = LocalDate.of(2024, 5, 29);
    test.crossoverDates(googleStockData, endDate, startDate, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidDateCrossoverDatesTest() {
    test.crossoverDates(googleStockData, null, LocalDate.of(2024, 5, 29), 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void datesThatDoNotExistInFileCrossoverDatesTest() {
    LocalDate date = LocalDate.of(2016, 6, 25);
    LocalDate endDate = LocalDate.of(2017, 5, 29);
    test.crossoverDates(googleStockData, date, endDate, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void datesThatDoNotExistInFileCrossoverDatesTest2() {
    LocalDate date = LocalDate.of(2016, 5, 29);
    LocalDate endDate = LocalDate.of(2016, 6, 25);
    test.crossoverDates(googleStockData, date, endDate, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void noPricesCrossoverDatesTest() {
    LocalDate date2 = LocalDate.of(2024, 6, 4);
    LocalDate date = LocalDate.of(2016, 5, 29);
    test.crossoverDates(null, date, date2, 2);
  }

  @Test
  public void startDateEqualsEndDateCrossoverDatesTest() {
    LocalDate date = LocalDate.of(2024, 5, 29);
    List<LocalDate> result = test.crossoverDates(googleStockData, date, date, 30);
    List<LocalDate> expected = new ArrayList<>();
    expected.add(date);
    assertEquals(expected, result);
  }

  @Test
  public void basicCrossoverDatesTest() {
    LocalDate startDate = LocalDate.of(2024, 5, 20);
    LocalDate endDate = LocalDate.of(2024, 5, 29);
    List<LocalDate> result = test.crossoverDates(googleStockData, startDate, endDate, 30);
    List<LocalDate> expected = new ArrayList<>();
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(4);
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    expected.add(startDate);
    assertEquals(expected, result);
  }

  @Test
  public void basicCrossoverDatesTest2() {
    LocalDate startDate = LocalDate.of(2023, 12, 11);
    LocalDate endDate = LocalDate.of(2023, 12, 18);
    List<LocalDate> result = test.crossoverDates(googleStockData, startDate, endDate, 30);
    List<LocalDate> expected = new ArrayList<>();
    expected.add(endDate);
    expected.add(startDate);
    assertEquals(expected, result);
  }

  @Test
  public void noCrossoversTest() {
    LocalDate startDate = LocalDate.of(2023, 12, 13);
    LocalDate endDate = LocalDate.of(2023, 12, 15);
    List<LocalDate> result = test.crossoverDates(googleStockData, startDate, endDate, 30);
    List<LocalDate> expected = new ArrayList<>();
    assertEquals(expected, result);
  }

  @Test
  public void firstDateCrossoverDatesTest() {
    LocalDate startDate = LocalDate.of(2014, 3, 27);
    LocalDate endDate = LocalDate.of(2014, 4, 4);
    List<LocalDate> result = test.crossoverDates(googleStockData, startDate, endDate, 30);
    List<LocalDate> expected = new ArrayList<>();
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(1);
    expected.add(endDate);
    endDate = endDate.minusDays(4);
    expected.add(endDate);
    assertEquals(expected, result);
  }

  @Test
  public void randomCrossoverDatesTest() {
    for (int i = 0; i < 100; i++) {
      LocalDate startDate = generateRandomDate();
      LocalDate endDate = generateRandomDate();
      int randomDays = random.nextInt(10) + 1;

      // Ensure startDate is before endDate
      if (startDate.isAfter(endDate)) {
        LocalDate temp = startDate;
        startDate = endDate;
        endDate = temp;
      }

      try {
        List<LocalDate> result = test.crossoverDates(googleStockData,
                startDate, endDate, randomDays);
        List<LocalDate> expected = calculateExpectedCrossoverDates(startDate, endDate, randomDays);
        assertEquals("Failed for dates: " + startDate
                + " to " + endDate + " and days: "
                + randomDays, expected, result);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid test case for dates: "
                + startDate + " to " + endDate
                + " and days: " + randomDays + ". " + e.getMessage());
      }
    }
  }

  private List<LocalDate> calculateExpectedCrossoverDates(LocalDate startDate,
                                                          LocalDate endDate, int days) {
    List<LocalDate> crossoverDateList = new ArrayList<>();
    boolean finishedRange = false;
    boolean inRange = false;
    double currentMovingAverage = 0;

    for (StockInformation stock : googleStockData) {
      if (startDate.equals(endDate)) {
        finishedRange = true;
      }
      if (stock.getDate().equals(endDate)) {
        currentMovingAverage = calculateExpectedMovingAverage(stock.getDate(), days);
        inRange = true;
        if (stock.getClose() > currentMovingAverage) {
          crossoverDateList.add(stock.getDate());
        }
        continue;
      }
      if (inRange) {
        if (finishedRange) {
          break;
        }
        currentMovingAverage = calculateExpectedMovingAverage(stock.getDate(), days);
        if (stock.getClose() > currentMovingAverage) {
          crossoverDateList.add(stock.getDate());
        }
        if (stock.getDate().equals(startDate)) {
          finishedRange = true;
          break;
        }
      }
    }
    if (!finishedRange) {
      throw new IllegalArgumentException("Data for this date range does not exist");
    }
    return crossoverDateList;
  }

}