import org.junit.Before;
import org.junit.Test;

import java.util.List;

import model.APIReader;
import model.AlphaVantageAPI;
import model.CSVReader;
import model.StockInformation;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the AlphaVantageAPI and CSVReader classes.
 */
public class AlphaVantageAPIANDCSVReaderTest {
  private CSVReader csvReader;

  @Before
  public void setup() {
    APIReader apiReader = new AlphaVantageAPI("JVMZKLP2NJASHJPI", "GOOG");
    csvReader = new CSVReader(apiReader, "GOOG");
  }

  @Test
  public void csvToStockInformationTest() {
    List<StockInformation> result = csvReader.fileToStockInfo();
    assertEquals(2567, result.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void writeFileTest() {
    csvReader.writeFile();
  }
}