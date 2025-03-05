import org.junit.Before;
import org.junit.Test;


import java.io.StringReader;
import java.util.List;
import controller.StocksController;
import model.APIReader;
import model.AlphaVantageAPI;
import model.CSVReader;
import model.StockInformation;
import view.StocksView;
import view.View;

import static org.junit.Assert.assertEquals;

/**
 * A test class to test the controller to model interaction.
 */
public class StocksControllerToModelTest {
  private StringBuilder log;
  private StocksController testController;
  private View testView;

  private MockStockCalculator testModel;

  @Before
  public void setup() {
    log = new StringBuilder();
    testModel = new MockStockCalculator(log);
    testView = new StocksView();
  }


  @Test
  public void gainLossCheckTest() {
    String input = "1\r\nGOOG\r\n2021-01-01\r\n2021-12-31\r\n6\r\n";
    Readable in = new StringReader(input);
    testController = new StocksController(testModel, testView, in);
    testController.construct();
    APIReader apiReader = new AlphaVantageAPI("GVOWNVFAUMGZOUBF", "GOOG");
    CSVReader csvReader = new CSVReader(apiReader, "GOOG");
    List<StockInformation> stockInfo = csvReader.fileToStockInfo();
    String expectedLog = "gainLossCheck(size="
            + stockInfo.size() + ", 2021-01-01, 2021-12-31)\r\n";

    assertEquals(expectedLog, log.toString());
  }

  @Test
  public void testCheckMovingAverage() {
    String input = "2\r\nGOOG\r\n2021-01-01\r\n5\r\n6\r\n";
    Readable in = new StringReader(input);
    testController = new StocksController(testModel, testView, in);
    testController.construct();
    APIReader apiReader = new AlphaVantageAPI("GVOWNVFAUMGZOUBF", "GOOG");
    CSVReader csvReader = new CSVReader(apiReader, "GOOG");
    List<StockInformation> stockInfo = csvReader.fileToStockInfo();
    String expectedLog = "movingAverage(size="
            + stockInfo.size() + ", 2021-01-01, 5)\r\n";
    assertEquals(expectedLog, log.toString());
  }

  @Test
  public void testCheckXDayCrossovers() {
    String input = "3\r\nGOOG\r\n6\r\n2021-01-01\r\n2021-12-31\r\n6\r\n";
    Readable in = new StringReader(input);
    testController = new StocksController(testModel, testView, in);
    testController.construct();
    APIReader apiReader = new AlphaVantageAPI("GVOWNVFAUMGZOUBF", "GOOG");
    CSVReader csvReader = new CSVReader(apiReader, "GOOG");
    List<StockInformation> stockInfo = csvReader.fileToStockInfo();
    String expectedLog = "crossoverDates(size="
            + stockInfo.size() + ", 2021-01-01, 2021-12-31, 6)\r\n";
    assertEquals(expectedLog, log.toString());
  }

}