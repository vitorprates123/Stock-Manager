import controller.StocksController;
import model.Portfolio;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import static org.junit.Assert.assertEquals;

/**
 * A test class for the StocksController class.
 */
public class StocksControllerTest {
  private StocksController controller;
  private StringBuilder log;

  @Before
  public void setUp() {
    log = new StringBuilder();
    MockStockCalculator model = new MockStockCalculator(log);
    MockStocksView view = new MockStocksView(log);
    MockPortfolio portfolio = new MockPortfolio(log);
    Portfolio.addPortfolio(portfolio);

    String simulatedUserInputs = "5\n1\n1\nGOOG\n15\n2024\n06\n12\n3\n6\n";
    controller = new StocksController(model, view, new StringReader(simulatedUserInputs));
  }

  @Test
  public void testConstruct() {
    controller.construct();

    String expectedLog = "portfolioRefresh()\n"
            + "displayMenu()\n"
            + "getUserChoice()\n"
            + "displayPortfolios()\n"
            + "getInput(Select a portfolio to manage:)\n"
            + "loadPortfolio(0, 2024-06-12)\n"
            + "displayManagingPortfolio(Mock Portfolio)\n"
            + "getUserChoice()\n"
            + "getInput(Enter stock symbol:)\n"
            + "getInput(Enter quantity (whole number shares only):)\n"
            + "getInput(ENTER PURCHASE DATE)\n"
            + "getInput(Enter the year(yyyy): )\n"
            + "getInput(Enter the month(mm): )\n"
            + "getInput(Enter the day (dd): )\n"
            + "loadPortfolio(0, 2024-06-12)\n"
            + "addStock(GOOG, 15, 2024-06-12)\n"
            + "displayStockAddition(15, GOOG, Mock Portfolio)\n"
            + "displayManagingPortfolio(Mock Portfolio)\n"
            + "getUserChoice()\n"
            + "displayMenu()\n"
            + "getUserChoice()\n"
            + "exitProgram()\n";

    assertEquals(expectedLog, log.toString());
  }
}