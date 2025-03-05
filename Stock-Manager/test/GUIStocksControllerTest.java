
import org.junit.Before;
import org.junit.jupiter.api.Test;

import controller.GUIStocksController;
import model.Portfolio;

import javax.swing.JButton;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test class to test the GUIStocksController class.
 */
public class GUIStocksControllerTest {
  private StringBuilder log;
  private GUIStocksController controller;

  @Before
  public void setUp() {
    log = new StringBuilder();
    MockStockCalculator model = new MockStockCalculator(log);
    MockStocksGUIView view = new MockStocksGUIView(log);
    MockPortfolio portfolio = new MockPortfolio(log);
    Portfolio.addPortfolio(portfolio);
  }

  @Test
  public void testCreatePortfolio() {
    ActionEvent event = new ActionEvent(new JButton("Create Portfolio"),
            ActionEvent.ACTION_PERFORMED, "Create Portfolio");
    controller.actionPerformed(event);
    assertEquals("setListener(controller.GUIStocksController@1)\n"
            + "displayPortfolioCreation(New Portfolio)\n", log.toString());
  }

  @Test
  public void testManagePortfolios() {
    ActionEvent event = new ActionEvent(new JButton("Manage Portfolios"),
            ActionEvent.ACTION_PERFORMED, "Manage Portfolios");
    controller.actionPerformed(event);
    assertEquals("setListener(controller.GUIStocksController@1)\n"
            + "displayManagingPortfolio(Sample Portfolio)\n", log.toString());
  }

  @Test
  public void testQuit() {
    ActionEvent event = new ActionEvent(new JButton("Quit"), ActionEvent.ACTION_PERFORMED, "Quit");
    controller.actionPerformed(event);
    assertEquals("setListener(controller.GUIStocksController@1)\n"
            + "quitProgram()\n", log.toString());
  }
}
