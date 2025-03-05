import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;

import view.StocksView;
import view.View;

import static org.junit.Assert.assertEquals;

/**
 * A test class to test the StocksView class.
 */
public class StocksViewTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  public void testDisplayGainLoss() {
    View view = new StocksView();
    view.displayGainLoss("AAPL", LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31), 100.50);

    String expectedOutput = "The gain/loss for AAPL from 2023-01-01 to 2023-12-31 is: $100.50\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayMovingAverage() {
    View view = new StocksView();
    view.displayMovingAverage("AAPL", LocalDate.of(2023, 1,
            1), 50, 150.75);

    String expectedOutput = "The 50-day moving average for AAPL on 2023-01-01 is: $150.75\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayCrossovers() {
    View view = new StocksView();
    view.displayCrossovers("AAPL", LocalDate.of(2023, 1,
            1), LocalDate.of(2023, 12, 31),
            Arrays.asList(LocalDate.of(2023, 2, 1),
                    LocalDate.of(2023, 3, 1)));

    String expectedOutput = "x-day crossovers for AAPL from 2023-01-01 "
            + "to 2023-12-31 are: \r\n2023-02-01\r\n2023-03-01\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayMenu() {
    View view = new StocksView();
    view.printMenu();

    String expectedOutput = "Welcome to the Stock Analyzer!\r\n"
            + "Please choose an option: You have 125 total "
            + "queries every minute! Use them wisely.\r\n"
            + "1. Examine gain or loss of a"
            + " stock over a specified period.\r\n"
            + "2. Examine the x-day moving average of a stock.\r\n"
            + "3. Determine x-day crossovers for a stock.\r\n"
            + "4. Create A Portfolio.\r\n"
            + "5. Manage Portfolios.\r\n"
            + "6. Check Bar-Chart.\r\n"
            + "7. Quit Program >:).\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayPortfolioComposition() {
    View view = new StocksView();
    view.displayPortfolioComposition("MyPortfolio",
            LocalDate.of(2023, 1, 1),
            "AAPL: 50 shares, GOOG: 50 shares");

    String expectedOutput = "The composition of portfolio 'MyPortfolio' on "
            + "2023-01-01 is: AAPL: 50 shares, GOOG: 50 shares\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayPortfolioDistribution() {
    View view = new StocksView();
    view.displayPortfolioDistribution("MyPortfolio",
            LocalDate.of(2023, 1, 1),
            "AAPL: 50 shares ($5000), GOOG: 50 shares ($10000)");

    String expectedOutput = "The distribution of portfolio 'MyPortfolio' on "
            + "2023-01-01 is: AAPL: 50 shares ($5000), GOOG: 50 shares ($10000)\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayPortfolioRebalance() {
    View view = new StocksView();
    view.displayPortfolioRebalance("om",
            LocalDate.of(2023, 1, 1));

    String expectedOutput = "Rebalanced portfolio 'om' on 2023-01-01.\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayBarChart() {
    View view = new StocksView();
    view.displayBarChart("MyPortfolio",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31),
            Arrays.asList("AAPL: $150", "GOOG: $200"));

    String expectedOutput = "Performance of portfolio 'MyPortfolio'"
            + " from 2023-01-01 to 2023-12-31:\r\nAAPL: $150\r\nGOOG: $200\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  public void testDisplayError() {
    View view = new StocksView();
    view.displayError("Invalid input.");

    String expectedOutput = "Invalid input.\r\n";
    assertEquals(expectedOutput, outContent.toString());
  }
}