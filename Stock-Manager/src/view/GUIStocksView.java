package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

//GUIView and GUIStocksView - We decided to add another interview
// for the GUIView to handle the code for the GUI separately as we
// knew that we wouldn't need some of the methods that were in the
// original view because they were specific to the text-based interface,
// this way, we were also able to implement GUIView simply and concisely
// as well. We wanted to make clear that our approach, though added
// more classes, was to make it so nothing was confusing,
// and everything was clear in our eyes.
/**
 * GUIStocksView is a class that extends JFrame and implements the GUIView interface.
 * It represents the (GUI) view of the application.
 * It contains buttons for creating and managing portfolios, and quitting the application.
 * It also contains a text field for user input, a.
 * text area for output display, and a label for input instructions.
 * The layout of the GUI is set to BorderLayout.
 */
public class GUIStocksView extends JFrame implements GUIView {
  private final JButton createPortfolioButton;
  private final JButton managePortfoliosButton;
  private final JButton quitButton;
  private final JTextField inputField;
  private final JTextArea outputArea;
  private final JLabel inputLabel;

  /**
   * Constructs a new GUIStocksView with the specified title.
   * Initializes the buttons, text field, text area, and label.
   * Sets the layout of the GUI and adds the components to it.
   *
   * @param title the title for the JFrame.
   */
  public GUIStocksView(String title) {
    super(title);
    this.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputLabel = new JLabel("");
    inputPanel.add(inputLabel);
    inputField = new JTextField(20);
    inputPanel.add(inputField);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(3, 1));

    createPortfolioButton = new JButton("Create Portfolio");
    buttonPanel.add(createPortfolioButton);

    managePortfoliosButton = new JButton("Manage Portfolios");
    buttonPanel.add(managePortfoliosButton);

    quitButton = new JButton("Quit");
    buttonPanel.add(quitButton);

    outputArea = new JTextArea();
    outputArea.setEditable(false);

    this.add(inputPanel, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.CENTER);
    this.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

    this.setSize(1920, 1080);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * this method sets the listener for the buttons.
   *
   * @param listener the listener for the buttons.
   */
  @Override
  public void setListener(ActionListener listener) {
    inputField.addActionListener(listener);
    createPortfolioButton.addActionListener(listener);
    managePortfoliosButton.addActionListener(listener);
    quitButton.addActionListener(listener);
  }

  /**
   * this method sets the input label for the user.
   *
   * @param labelText the label text for the input.
   */
  @Override
  public void setInputLabel(String labelText) {
    inputLabel.setText(labelText);
  }

  /**
   * this method clears the input field.
   */
  @Override
  public void clearInputs() {
    inputField.setText("");
  }

  /**
   * this method displays the output to the user.
   */
  @Override
  public void display() {
    this.setVisible(true);
  }

  /**
   * this method gets the input from the user and outputs it.
   *
   * @param output the output to be displayed after user inputs.
   */
  @Override
  public void getInput(String output) {
    outputArea.append(output + "\n");
  }

  /**
   * this method displays the creation of a portfolio.
   *
   * @param name the name of the portfolio is displayed.
   */
  @Override
  public void displayPortfolioCreation(String name) {
    outputArea.append("Portfolio '" + name + "' created successfully.\n");
  }

  /**
   * this method displays the list of all portfolios.
   *
   * @param portfolios the list of all portfolios.
   * @return the number of the chosen portfolio.
   */
  @Override
  public int displayPortfolios(List<String> portfolios) {
    outputArea.append("Available Portfolios:\n");
    for (int i = 0; i < portfolios.size(); i++) {
      outputArea.append((i + 1) + ". " + portfolios.get(i) + "\n");
    }

    String[] portfolioArray = portfolios.toArray(new String[0]);
    String selectedPortfolio = (String)
            JOptionPane.showInputDialog(null,
                    "Select a portfolio to manage:",
                    "Manage Portfolios", JOptionPane.QUESTION_MESSAGE,
                    null, portfolioArray, portfolioArray[0]);

    if (selectedPortfolio != null) {
      return portfolios.indexOf(selectedPortfolio);
    }
    return -1;
  }

  /**
   * this method displays the management of a given portfolio the user chooses.
   *
   * @param name the name of the portfolio to be managed.
   */
  @Override
  public void displayManagingPortfolio(String name) {
    outputArea.append("Managing Portfolio: " + name + "\n");
  }

  /**
   * this method displays the addition of a stock to a portfolio.
   *
   * @param quantity      the shares of stock added.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is added.
   */
  @Override
  public void displayStockAddition(int quantity,
                                   String symbol, String portfolioName) {
    outputArea.append(String.format("Added %d shares"
                    + " of %s to portfolio '%s'.%n",
            quantity, symbol, portfolioName));
  }

  /**
   * this method displays the removal of a stock from a portfolio.
   *
   * @param quantity      the shares of stock removed.
   * @param symbol        the ticker symbol of the stock.
   * @param portfolioName the name of the portfolio where the stock is removed.
   */
  @Override
  public void displayStockRemoval(int quantity,
                                  String symbol, String portfolioName) {
    outputArea.append(String.format("Removed %d shares"
                    + " of %s from portfolio '%s'.%n",
            quantity, symbol, portfolioName));
  }

  /**
   * this method displays the composition of your portfolio on a given date.
   *
   * @param portfolioName the name of the portfolio where the composition is displayed.
   * @param date          is the date we look at to get the composition.
   * @param composition   is the composition of the portfolio.
   */
  @Override
  public void displayPortfolioComposition(String portfolioName,
                                          LocalDate date, String composition) {
    outputArea.append(String.format("The composition of"
                    + " portfolio '%s' on %s is: %s%n",
            portfolioName, date, composition));
  }

  /**
   * this method displays the total value of a portfolio.
   *
   * @param portfolioName the name of the portfolio.
   * @param date          the date of the total value.
   * @param totalValue    the total value of the portfolio.
   */
  @Override
  public void displayPortfolioValue(String portfolioName,
                                    LocalDate date, double totalValue) {
    outputArea.append(String.format("The total value of"
                    + " portfolio '%s' on %s is: $%.2f%n",
            portfolioName, date, totalValue));
  }

  /**
   * this method displays an error message whenever there is one given.
   *
   * @param message the error message to be displayed.
   */
  @Override
  public void displayError(String message) {
    outputArea.append("Error: " + message + "\n");
  }

  /**
   * this method quits the program.
   */
  @Override
  public void quitProgram() {
    System.exit(0);
  }

}
