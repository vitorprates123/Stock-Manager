The Stock Analyzer is designed to be modular and adaptable. The basic functionality described in
README.html is just a basic implementation of the Stock Analyzer.

The Controller, Model, and View are all implementing interfaces that allow for functionality
to be added to the Stock Analyzer in the future or a Stock Analyzer that behaves differently
to be implemented alongside our current one.

In the Model package, the calculations and analysis of the data also implements an interface allowing for
different calculations or ways of calculating the basic stock properties to be accounted for.

TheAPI Reader and FileCreator interfaces allow for different ways of creating or reading a file to
be accounted for as well. It also allows for an existing API implementation to be used
with a different file type and vice versa.

The IPortfolio interface allows for different types of portfolios to
implemented as well.

In the View package, the View interface allows for different types of displays to be
developed.

In the Controller package, the Controller interface allows for different types of
controller to be developed.

All of this modularity allows for different implementations of the code
to work with already existing implementations without major refactoring or changes.


CHANGES:
Stocks class - We decided to add a simple method in this class "equals" which will compare stocks to help remove stocks in a portfolio.
Before, we were using the "contains" method in the portfolio class to remove stocks. This was not working as expected because the "contains" method was not comparing the stocks correctly. We decided to try making an equals method to compare the stocks and remove them from the portfolio. This change was made to the Stocks class, and the Portfolio class was updated to use the equals method to remove stocks.

IPortfolio and Portfolio class - several enhancements and additions were made to increase its functionality and adaptability. Here's a summary of the changes:

1. Distribution Check: A method was added to check the distribution of a portfolio. This allows users to understand the proportion of each stock in their portfolio, which is crucial for risk management and investment strategy.

2. Composition Check: A method was added to check the composition of the portfolio. This provides users with a detailed view of their portfolio, including the types and quantities of stocks it contains.

3. Rebalancing: Logic was added to rebalance the portfolio to its intended weights. This is an important feature for maintaining a specific risk level or investment strategy over time, as market fluctuations can cause a portfolio's actual weights to deviate from its target weights.

4. Performance Check: A method was added to check the performance of a portfolio. This allows users to track the returns of their portfolio over time, which is essential for evaluating the success of their investment strategy.

5. Stock Removal: Functionality was added to remove a stock from a portfolio. This gives users more control over their portfolio and allows them to easily adjust their investment strategy.

6. Adding Stocks with Specific Dates: The `addStock` method was updated to include an extra parameter for the date. This allows users to add stocks to their portfolio on specific dates, providing more accurate and flexible portfolio management.

These changes were justified as they significantly enhance the functionality of the `Portfolio` class, providing users with more control over their portfolios and more information to guide their investment decisions. They also improve the adaptability of the class, allowing it to handle a wider range of portfolio management tasks.

StocksController - In this class we basically updated the controller to manage the updated portfolio class. We added the logic to check the distribution of the portfolio, the composition of the portfolio, the performance of the portfolio, and the rebalance of the portfolio. We also added the logic to remove a stock from the portfolio. Also, we added more cases to our construct method to handle the new functionalities, and see if the inputs work. To justify our changes, the reason we did this is that our approach for the StocksController is already simplified down since we have a lot of helper methods, and we wanted to keep that same ideology when handling the new functionalities from the portfolio, and it actually keeps the code simple and well-designed as well because its only little changes we are adding to the controller.

View & StocksView- In our interface, we updated the interface to allow more displays to be shown because it needed to handle the updated functionality of our portfolio class. We added the displayDistribution, displayComposition, displayPerformance, and displayRebalance methods to the interface. We also added the displayRemoveStock method to the interface. We also added the displayAddStock method to the interface to handle the new functionality of adding a stock to the portfolio on a specific date. This way, we can have different displays and keep the code simple and concise, as well as adding minimalistic changes to the interface and class itself. This way, we don't have to extract code or simplify by adding more classes, when the class itself handles the displays and new functionality correctly and efficiently!

UPDATED CHANGES FOR ASSIGNMENT 6:
GUIView and GUIStocksView - We decided to add another interview for the GUIView to handle the code for the GUI separately as we knew that we wouldn't need some of the methods that were in the original view because they were specific to the text-based interface, this way, we were also able to implement GUIView simply and concisely as well. We wanted to make clear that our approach, though added more classes, was to make it so nothing was confusing, and everything was clear in our eyes.

GUIStocksController - This class was updated to manage the new functionalities of the Portfolio class. Logic was added to check the composition and to remove a stock from the portfolio. More cases were also added to the construct method to handle these new functionalities and check if the inputs work, but we handled it, so it works with a graphical user interface.

ALSO, our file is always AUTO save everytime we add a stock or sell a stock from portfolio, so we do not need a button for that to save a portfolio when it auto saves after every action, so that is fine, and the same thing happens for when you are trying to load a portfolio that you already created. If you rerun the program, and wanted to access a portfolio that you created, all you have to do is click manage portfolios, and your portfolio will be saved in there. BUT, if you wanted to get a portfolio that was outside the res file, and outside of res/portfolios, you would need to drag the portfolio in the res/portfolios file, and then when you rerun the GUI, and click manage portfolios, it will be there.

AND ALSO FOR THE NOTE ABOVE ^  TA Maxwell Nurzia said that my program was fine the way I set it up for how it auto saves and autoloads a portfolio, and he says that it is fine to not have BUTTONS for loading and saving a portfolio since I have this implementation in my code.