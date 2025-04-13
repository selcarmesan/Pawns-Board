# Pawns Board Game

## 1) Overview:

Pawns Board is a two-player strategy game inspired by Queen's Blood. Throughout gameplay, players take turns placing
cards on a rectangular grid, using their influence to manipulate pawns and control the board. The game enforces specific
rules regarding board setup, deck configuration, pawn influence, and scoring.
In terms of its components, the game is made up of:
- A board with a predefined number of rows and columns
- Two players (Red and Blue), each with a deck of cards and own hands of cards
- A set of cards with influence patterns, which affect the board state
- A scoring system that determines the winner based on card placements and possible moves

In terms of flexibility, the game allows for the board size to be completely customized (only restriction is it must be
bigger than a 3x3 grid for game and scoring purposed). Additionally, implementation also allows for any textual deck
configuration to be passed, allowing players to design their own cards for gameplay.

Currently, the game is implemented only for human-to-human interaction. However, the presence of a user-player interface
allows for possible extensibility of the model, expanding to simple AI simulation players. Furthermore, the model implementation
also left the door open to the possibility of passing different types of deck configuration files (not only textual) through
the use of an interface that can differently implemented.

## 2) Quick Start:

### Running The Game:

1. Players must start by passing in a deck.config file to set up the deck
   ``` 
   String filePath = "docs" + File.separator + "deck.config";
   TextDeckReader deckReader = new TextDeckReader();
   List<Card> deck = deckReader.readDeck(filePath);
   ```
2. Initialize the game model
    ``` 
   Game game = new Game(3, 5, deck, deck);
   ```
3. Initialize and print the game textual view
    ``` 
   GameTextualView view = new GameTextualView(game);
   view.render();
    ```
4. Call playGame() method to initialize the gameplay functionality
    ``` 
   playGame(game, view);
    ```

### Test Class Example:
```
@Before
  public void setUp() throws FileNotFoundException {
    // Load deck from the real configuration file
    String filePath = "docs" + File.separator + "deck.config";
    TextDeckReader deckReader = new TextDeckReader();

    List<Card> deck = deckReader.readDeck(filePath);

    // Create game with the loaded deck
    game = new Game(3, 5, deck, deck);
  }

  @Test
  public void testCompleteGameInitialization() {
    assertNotNull(game.getBoard()); // Board should not be null
    assertEquals(3, game.getBoard().getGrid().length); // Ensure correct rows
    assertEquals(5, game.getBoard().getGrid()[0].length); // Ensure correct columns
    assertEquals(PlayerColor.RED, game.getCurrentPlayer().getColor()); // Red starts
    assertFalse(game.isGameOver()); // Game should not be over at start
  }
```

## 3) Game Mechanics:

### Board Setup:
- Board is rectangular, with a positive number of rows and an odd number of columns (minimum is 3x3)
- First column contains the Red player's pawns, while the last column contains Blue player's pawns

### Deck & Cards:
- Each player has a deck of unique cards read from a configuration file (docs/deck.config)
- Each card has:
- A name (unique identifier)
- A cost (1, 2, or 3 pawns required to place it)
- A value score (used for calculating row scores)
- A 5x5 influence grid showing its effect on adjacent cells
- There cannot exist more than 2 copies of any card in a deck

### Turn Structure:
- Game always begins with Red player's turn
- Current player draws a card from their deck, if there are cards remaining
- If the player's deck does not have any cards left, no card is drawn
- Player chooses between: passing their turn or placing a card
- If the player chooses to pass, then their turn ends and switches to the other player
- If the player chooses to place a card, they must place their card in a cell that has enough of their own pawns
to cover the cost of the card
- The game switches turn to the other player

### Card Placement Rules:
- A player may place a card only if they own enough pawns on the target cell
- The placed card removes the pawns used to pay its cost
- The card's influence grid affects nearby cells by:
- Adding pawns if the cell is empty
- Increasing pawns if the cell already belongs to the player
- Converting opponent pawns if they are present

### Scoring System:
- Each row is scored based on the total value of a player’s cards in that row
- The player with the highest row-score wins that row and gains its total points
- If row scores are tied, no points are awarded for that row
- The player with the highest total score across all rows wins

## 4) Key Components:

### Model (cs3500.model):
- Game logic - Game.java: manages board state, card placements, and rule enforcement
- Board representation - Board.java: stores and updates cell states depending on pawns, cards, and ownership
- Cell.java represents individual cells of the board, class allows for specific cell manipulation
- Card system - Card.java: defines card properties, costs, values, and influence mechanics
- Player interaction - Player.java: creates the players of the game (Red and Blue) and defines methods that allow players
to interact with the game model (such as placing cards, drawing cards, etc.)
- Deck configuration - DeckConfigReader.java & TextDeckReader.java: parses to the game customized cards given by the user
through a text file (deck.config) placed inside the docs directory

### View (cs3500.view):
- Textual rendering -  GameTextualView.java: displays the current state of the game after every move, represents a textual
rendering of the board and row scores

### Main (cs3500.main):
- Entry point - PawnsBoard.java: initializes and run the game, handles turn transitions and ensures rules are followed
- Reads deck configuration from docs/deck.config

### Examples Class (cs300):
- Exemplification - ExamplePawnsBoard.java: gives a simplified overlook at how the code functions, its methods, and how
the game is run

### Tests (test.cs3500):
- Implementation specific tests for the main model class: ModelImplementationTest.java (cs3500.model)
- Public interface feature tests for the main model class: ModelPublicFeaturesTest.java

## 5) Source Organization:

```
project/
│── src/
│   ├── cs3500/
│   │   ├── model/  # Game logic implementation
│   │   ├── view/   # Visual GUI + Textual GUI
│   │   ├── main/   # Entry point
│   │   ├── strategies/  # Strategy implementation
│   │   ├── controller/  # Stub controller + Deck reading
│   │   ├── ExamplePawnsBoard.java # Examples class
│   │   ├── UserPlayerInterface.java # Interface for user interaction
│── test/
│   ├── cs3500/
│   │   ├── model/  # Implementation Tests
│   │   ├── view/  # Textual view tests
│   │   ├── strategies/  # Combined strategies tests
│   ├── ModelPublicFeaturesTest.java # Public Interface Tests
│   ├── StrategyTest.java # Public strategy testing
│── docs/
│   ├── deck.config  # Card definitions
│   ├── strategy-transcript-frist  # Strategy transcript
│   ├── strategy-transcript-score  # Strategy transcript
```

## 6) Deck Configuration File:

Each deck contains multiple cards definitions, formatted as follows:

CARD_NAME COST VALUE
XXXXX
XXIXX
XICIX
XXIXX
XXXXX

- CARD_NAME: unique name of the card
- COST: number of pawns required to place the card
- VALUE: the score assigned to the card 
- Influence Grid: a 5x5 text matrix with the following symbols:
  - X: no influence
  - I: affects the cell
  - C: card's position (always center)

## 7) Changes for Part 2:

When reflecting on the required functionalities, and overall looking at what is expected from an MVC design approach,
it was noticeable that the previous Part-1 model needed some improvements. Key game functionalities were missing from the
main model class, while simultaneously, the model was being responsible for tasks that needed to be delegated to other
components of MVC (such as the Controller). Hence, these changes were made:
- **_Separated the model interfaces into two: ReadonlyPawnsBoardModel and PawnsBoardModel_**
  - One handles all observations and the other allows for methods that directly mutate the game state
- **_Added extra methods to ensure model covers all necessary functionality_**
  - Read-only methods: get board rows and columns, get specific cells given a position, get players' hands, get the owner of a given cell, calculate specific row scores, get a chosen player's score, and get winning player 
    - Changed previously implemented methods to ensure the model returns a copy of the intended object - prevents mutations
  - Mutating methods: copy current board
- **_Refactored deck.config file reading to the controller component_**
  - Ensures better MVC design since the Model should not handle user-input, that is the controller's job
- **_Made changes to the Cards implementation_**
  - Changed the cost related to Cards from an int to an enum class (with values 1, 2, 3) to limit room for user error
  - Added an owner attribute to all cards
    - Fixed a previous model logic error - hw5 model assumed board cells had owners, which is not true given game rules
    - Allows to correct calculation of players' row scores
- **_Coordinate System Explained_** 
  - The board uses a row-column grid. 
  - The top-left cell is (0,0), with rows increasing downward and columns increasing to the right.
- **_Invariants Documented_**
  - The board must be at least 3x3.
  - Columns must be odd.
  - Cards must contain a 5x5 influence grid with a single 'C' at the center.
  - Maximum two copies of a card per deck.

## 8) View Interactions
During gameplay, the current view contains several keyboard and mouse interactions. These include:

### Making a move:
- Players can play cards on the board by clicking on the desired card from their hand (card will highlight in a yellow outline)
- Then, the player must click on the desired board cell (cell will highlight entirely in yellow)
- To confirm the move, the player must press **_ENTER_** on their keyboard. If the move is valid, the score value of the chosen card will appear on the chosen board cell.
  - If the move is not valid, the placement of the card will not happen and the player must pick another position/card.
- At any time, players can deselect cards or board cells by clicking on them again (highlight will disappear and the object is not selected anymore).

### Passing their turn:
- Players can also choose to pass their turn during gameplay. This can be done by pressing the **_SPACE BAR_** on their keyboard.
  - This would switch turns to the next player.

## 9) Required Game Strategies + Extra Credit Strategies

Implemented advanced strategy players to enhance AI functionality. All strategy classes are located in `src/strategies/`.

- `FillFirstStrategy.java`: Selects the first valid card-location pair.
- `MaximizeRowScoreStrategy.java`: Chooses a move that maximizes the row score differential.
- `ControlBoardStrategy.java` (**extra credit**): Selects the move that results in controlling the most cells.
- `MinimaxStrategy.java` (**extra credit**): Minimizes the opponent’s maximum score potential on their next move.

Strategies are composable and designed to be reusable.

_**Transcripts for strategies are found within the `docs` directory.**_

### Strategy Testing:

Tests are located in the `test/StrategyTest/` directory. 
Where every test goes in-depth of making sure they work, and also work when you  recombine strategies to make more complex ones.

## 10) Changes for Part 3:

In order to adapt our existing code to fit the observer pattern, a series of changes were made to the previous versions:
- Added observer interfaces for player actions, view actions, and model changes
  - This included enhancing the existing view class with the ability to add listeners and notify the controller of changes
  - Modified the existing model class also with the ability to add listeners and modify the controller/player
- Made the controller specific to a given player
  - Allows different types of players to control their interactions -- especially considering machine players
- Implemented the two types of players: human and machine
- Fully implemented the controller based on previous "stub" code
- Made small changes to the model classes taking into consideration the feedback received on the previous assignment
  - Most importantly, added the game over logic if both players pass consecutively

## 11) Players Implementation

The game now supports multiple types of players through a flexible Player interface, allowing for human-controlled gameplay. This enhancement makes the system significantly more extensible, enabling support for automated simulations, single-player matches, etc.

Two core implementations of the Player interface are provided: HumanPlayer and MachinePlayer. The HumanPlayer class is designed to work in tandem with the view and controller, relying entirely on external user input to make moves. During their turn, human players do not trigger any actions automatically. Instead, they respond to events like mouse clicks and keyboard inputs, allowing the user to select cards, choose board positions, and confirm moves. The view communicates these choices to the player instance, and from there to the game model via the controller.

On the other hand, the MachinePlayer class automates the decision-making process by using a strategy-based system. As soon as its startTurn() method is called, a MachinePlayer consults its assigned strategy to evaluate possible moves and automatically selects and performs the most appropriate one. If no valid moves exist, the machine player will pass its turn. To maintain clean separation between the model and controller layers, the machine player communicates its choices through a PlayerActionListener interface. This includes methods such as onCardSelected, onCellSelected, onMoveConfirmed, and onTurnPassed, which are invoked depending on the machine's chosen action.

Both types of players interact with the game using this shared listener interface, ensuring that the rest of the system (like the controller or game loop) can remain agnostic to the specific player type. This design promotes modularity and makes it easy to swap or combine different player types during initialization.