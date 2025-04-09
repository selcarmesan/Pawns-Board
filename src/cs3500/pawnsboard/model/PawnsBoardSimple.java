package cs3500.pawnsboard.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import cs3500.pawnsboard.model.observer.ModelUpdateSubscriber;
import cs3500.pawnsboard.model.observer.ModelUpdates;

/**
 * A game of pawns board.  The game begins with an empty board of specified dimensions, with the
 * restrictions being that the board is rectangular, with a positive number of rows, and an odd
 * number of columns greater than 1.
 * Gameplay begins by dealing each of the two players a hand and allowing them to take turns
 * placing one card at a time or skipping their turn until the board is full or no player can go.
 * Score is row individual.  The one with more value based on their placed cards per row is the
 * winner of that row, and receives their total value, while the opponent receives nothing.  The
 * player with the most points from the rows they claimed is the victor at the end, or it ends in a
 * draw.
 */
public class PawnsBoardSimple implements PawnsBoard, ModelUpdates {

  //<editor-fold desc="Fields">
  private final int rows;
  private final int cols;
  private final Random rand;
  private boolean randomDraw;
  private boolean gameStarted;
  private boolean gameOver;

  private final List<ModelUpdateSubscriber> redListeners;
  private final List<ModelUpdateSubscriber> blueListeners;

  // Is 0 indexed, and (row, col) ordered for all calls
  private Cell[][] board;

  private List<Card> redDeck;
  private List<Card> redHand;
  private List<Card> blueDeck;
  private List<Card> blueHand;
  private Player currentTurn;
  private boolean lastPassed = false;
  private boolean firstTurnOver = false;
  //</editor-fold>

  //<editor-fold desc="Constructors">
  /**
   * Creates a new pawns board game with a random seed.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @throws IllegalArgumentException if rows or columns is not positive
   *                                  if columns is not odd and greater than two
   */
  public PawnsBoardSimple(int rows, int cols) {
    if (rows < 1) {
      throw new IllegalArgumentException("rows must be greater than 0");
    }
    if (cols < 3 || cols % 2 != 1) {
      throw new IllegalArgumentException("columns must be odd and greater than two");
    }
    this.rows = rows;
    this.cols = cols;
    this.rand = new Random();
    this.redListeners = new ArrayList<>();
    this.blueListeners = new ArrayList<>();
  }

  /**
   * Creates a new pawns board game using the given random as a seed.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @param rand random used for drawing new cards
   * @throws IllegalArgumentException if rows or columns is not positive
   *                                  if columns is not odd and greater than two
   *                                  if the given random is null
   */
  public PawnsBoardSimple(int rows, int cols, Random rand) {
    if (rows < 1) {
      throw new IllegalArgumentException("rows must be greater than 0");
    }
    if (cols < 3 || cols % 2 != 1) {
      throw new IllegalArgumentException("columns must be odd and greater than two");
    }
    if (rand == null) {
      throw new IllegalArgumentException("random must not be null");
    }
    this.rows = rows;
    this.cols = cols;
    this.rand = rand;
    this.redListeners = new ArrayList<>();
    this.blueListeners = new ArrayList<>();
  }
  //</editor-fold>

  //<editor-fold desc="Game Starters">

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks containing
   * playing cards of their color, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.  The deck arguments are intended to be fetched using the PawnsCardReader class,
   * with a config file specified when calling that class' method, however it is possible to
   * manually create a deck as well.
   *
   * @param redDeck    red player's deck
   * @param blueDeck   blue player's deck
   * @param handSize   the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if either decks are null or contain null
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException    if game is already in progress
   */
  @Override
  public void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize, boolean randomDraw) {
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    if (redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("Decks cannot be null");
    }
    if (redDeck.stream().anyMatch(Objects::isNull) || blueDeck.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Decks cannot contain null");
    }
    for (Card card : redDeck) {
      if (Collections.frequency(redDeck, card) > 2) {
        throw new IllegalArgumentException("Decks cannot contain more than 2 of the same card");
      }
    }
    for (Card card : blueDeck) {
      if (Collections.frequency(blueDeck, card) > 2) {
        throw new IllegalArgumentException("Decks cannot contain more than 2 of the same card");
      }
    }
    assertDeckValidity(redDeck, blueDeck, handSize);
    List<Card> newRedDeck = new ArrayList<>();
    List<Card> newBlueDeck = new ArrayList<>();
    for (Card card : redDeck) {
      newRedDeck.add(new PawnsCard(card.getName(), card.getCost(), card.getValue(),
              card.getInfluence()));
    }
    for (Card card : blueDeck) {
      newBlueDeck.add(new PawnsCard(card.getName(), card.getCost(), card.getValue(),
              card.getInfluence()));
    }
    this.redDeck = newRedDeck;
    this.blueDeck = newBlueDeck;
    this.randomDraw = randomDraw;
    setupGame(handSize);
  }

  private void setupGame(int handSize) {
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    for (int i = 0; i < handSize; i++) {
      drawCard(Player.RED);
      drawCard(Player.BLUE);
    }
    board = new BoardCell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new BoardCell();
        if (j == 0) {
          Cell cell = new BoardCell();
          cell.addPawn(Player.RED);
          board[i][j] = cell;
        }
        if (j == cols - 1) {
          Cell cell = new BoardCell();
          cell.addPawn(Player.BLUE);
          board[i][j] = cell;
        }
      }
    }
    currentTurn = Player.RED;
    gameStarted = true;
    gameOver = false;
    turnStartedRed();
  }

  private void assertDeckValidity(List<Card> redDeck, List<Card> blueDeck, int handSize) {
    if (handSize < 1 || handSize > Math.max(redDeck.size(), blueDeck.size()) / 3) {
      throw new IllegalArgumentException("Hand must be positive and less than a third of the deck");
    }
    if (redDeck.size() < getNumCells() || blueDeck.size() < getNumCells()) {
      throw new IllegalArgumentException("Decks must contain enough cards to fill the board");
    }
  }
  //</editor-fold>

  //<editor-fold desc="Getters">

  /**
   * Returns the number of rows on the board.
   *
   * @return the rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns the number of columns on the board.
   *
   * @return the columns
   */
  public int getCols() {
    return cols;
  }

  /**
   * Returns the current total score of the specified player.  Cancels out per row if they have less
   * than the opposing player, and both are cancelled out if equal.
   *
   * @param player the player whose score is returned
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public int getTotalScore(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    int score = 0;
    for (int i = 0; i < rows; i++) {
      if (getRowScore(player, i) > getRowScore(getOtherPlayer(player), i)) {
        score += getRowScore(player, i);
      }
    }
    return score;
  }

  /**
   * Returns the current score of the specified player in the particular row.  Does not account for
   * cancelling as with total scoring.
   *
   * @param player the player whose score is returned
   * @param row the row to score
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if row is out of bounds
   *                                  if player is null
   */
  @Override
  public int getRowScore(Player player, int row) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (!locationValid(row, 0)) {
      throw new IllegalArgumentException("Row invalid");
    }
    int score = 0;
    for (int j = 0; j < cols; j++) {
      Cell cell = getCellAt(row, j);
      if (cell.getOwner() == player && cell.getCard() != null) {
        score += cell.getCard().getValue();
      }
    }
    return score;
  }

  /**
   * Returns the current hand of cards belonging to the specified player.
   *
   * @param player the player whose hand is returned
   * @return the player's hand
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public List<Card> getHand(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    List<Card> hand = new ArrayList<>();
    if (player == Player.RED) {
      for (Card card : redHand) {
        hand.add(new PawnsCard(card.getName(), card.getCost(),
                card.getValue(), card.getInfluence()));
      }
    } else {
      for (Card card : blueHand) {
        hand.add(new PawnsCard(card.getName(), card.getCost(),
                card.getValue(), card.getInfluence()));
      }
    }
    return hand;
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getCurrentTurn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return currentTurn;
  }

  /**
   * Returns the winner, the player with the greater score, or null if tied.
   *
   * @return the winning player
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getWinner() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (!isGameOver()) {
      throw new IllegalStateException("Can only determine winner after game");
    }
    int redScore = getTotalScore(Player.RED);
    int blueScore = getTotalScore(Player.BLUE);
    if (redScore > blueScore) {
      return Player.RED;
    } else if (blueScore > redScore) {
      return Player.BLUE;
    } else {
      return null;
    }
  }

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Cell[][] getBoard() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    Cell[][] copyBoard = new BoardCell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        copyBoard[i][j] = getCellAt(i, j);
      }
    }
    return copyBoard;
  }

  /**
   * Returns a copy of the specified cell.
   *
   * @param row the row
   * @param col the column
   * @return the cell in question
   * @throws IllegalArgumentException if row and column pair marks an invalid space
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public Cell getCellAt(int row, int col) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (!locationValid(row, col)) {
      throw new IllegalArgumentException("Invalid location");
    }
    Cell cell = board[row][col];
    Cell copyCell = new BoardCell();
    if (cell.getCard() != null) {
      copyCell.playCard(cell.getCard(), cell.getOwner());
    } else if (cell.getPawns() > 0) {
      copyCell.addPawn(cell.getOwner());
      for (int i = 0; i < cell.getPawns() - 1; i++) {
        copyCell.addPawn();
      }
    }
    return copyCell;
  }

  /**
   * Tells whether game is still in progress.
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return gameOver;
  }
  //</editor-fold>

  //<editor-fold desc="Turn Options">

  /**
   * Influences the given board starting at the specified row and column.  Each card reaches at
   * most 2 in any direction from the starting space, while the specifics vary by card.
   * A card can be placed if the pawns cost for it are within the desired space, and the pawns
   * belong to the player attempting to place the card.  A new card is drawn to replace it after,
   * if any remain in the player's deck.
   *
   * @param row    the starting cell's row
   * @param col    the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException hand ID is invalid
   *                                  if coordinates for row and column are out of bounds.
   *                                  if desired cell does not have enough owned pawns for the cost
   *                                  if desired cell has pawns that do not belong to the player
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public void placeCard(int row, int col, int handId) {
    if (!isMoveValid(row, col, handId, getCurrentTurn())) {
      throw new IllegalArgumentException("Move invalid");
    }
    Card card = getCurrentPlayerHand().remove(handId);
    board[row][col].playCard(card, getCurrentTurn());
    lastPassed = false;
    influenceBoard(row, col, card);
    swapTurn();
  }

  /**
   * Returns whether a move for the current player is valid, a move being the placement of
   * a card onto a location on the board.  A move is valid if the card exists, the location is on
   * the board, and the location has enough owned pawns by the player to cover the cost of the card.
   * Cards can also only be played in cells that do not have a card played already.
   *
   * @param row the starting cell's row
   * @param col the starting cell's column
   * @param handId the index of the card in the player's hand
   * @param player the player to check validity for
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if player is null
   *                                  if coordinates for row and column are out of bounds.
   * @throws IllegalStateException if game is not in progress
   */
  public boolean isMoveValid(int row, int col, int handId, Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (!locationValid(row, col)) {
      throw new IllegalArgumentException("Invalid location");
    }
    if (handId < 0 || handId >= getCurrentPlayerHand().size()) {
      throw new IllegalArgumentException("Invalid hand id");
    }
    Card card = getHand(player).remove(handId);
    Cell cell = getCellAt(row, col);
    return !(card.getCost() > cell.getPawns() || cell.getOwner() != player);
  }

  /**
   * Skips the turn of the current player, rather than placing a new card.
   *
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public void skipTurn() {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (lastPassed) {
      gameOver = true;
      gameEnded();
    }
    lastPassed = true;
    swapTurn();
  }
  //</editor-fold>

  //<editor-fold desc="Helpers">
  private boolean locationValid(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  private int getNumCells() {
    return rows * cols;
  }

  private void drawCard(Player deck) {
    if (deck == Player.RED) {
      if (redDeck.isEmpty()) {
        return;
      }
      if (randomDraw) {
        redHand.add(redDeck.remove(rand.nextInt(redDeck.size())));
      } else {
        redHand.add(redDeck.remove(0));
      }
    } else {
      if (blueDeck.isEmpty()) {
        return;
      }
      if (randomDraw) {
        blueHand.add(blueDeck.remove(rand.nextInt(blueDeck.size())));
      } else {
        blueHand.add(blueDeck.remove(0));
      }
    }
  }

  // For getting the proper hand, not a copy unlike the public method
  private List<Card> getCurrentPlayerHand() {
    if (getCurrentTurn() == Player.RED) {
      return redHand;
    } else {
      return blueHand;
    }
  }

  private void swapTurn() {
    currentTurn = getOtherPlayer(getCurrentTurn());
    if (firstTurnOver) {
      drawCard(getCurrentTurn());
    }
    firstTurnOver = true;
    if (currentTurn == Player.RED) {
      turnStartedRed();
    } else {
      turnStartedBlue();
    }
  }

  private void influenceBoard(int row, int col, Card card) {
    boolean[][] influence = card.getInfluence();
    for (int i = -2; i <= 2; i++) {
      for (int j = -2; j <= 2; j++) {
        if (locationValid(row + i, col + j)) {
          if (influence[i + 2][j + 2]) {
            influenceCell(board[row + i][col + j], getCurrentTurn());
          }
        }
      }
    }
  }

  private void influenceCell(Cell cell, Player player) {
    if (cell.getCard() == null) {
      if (cell.getOwner() != player) {
        if (cell.getOwner() == null) {
          cell.addPawn(player);
        } else {
          cell.changeOwner(player);
        }
      } else {
        cell.addPawn();
      }
    }
  }

  private Player getOtherPlayer(Player player) {
    if (player == Player.RED) {
      return Player.BLUE;
    } else {
      return Player.RED;
    }
  }
  //</editor-fold>

  //<editor-fold desc="Notifications">
  /**
   * Notification for when the turn of player red has just begun.
   */
  @Override
  public void turnStartedRed() {
    turnStarted(Player.RED);
    if (isGameOver()) {
      return;
    }
    for (ModelUpdateSubscriber listener : redListeners) {
      listener.notifyView("Your turn has started!");
    }
  }

  /**
   * Notification for when the turn of player blue has just begun.
   */
  @Override
  public void turnStartedBlue() {
    turnStarted(Player.BLUE);
    if (isGameOver()) {
      return;
    }
    for (ModelUpdateSubscriber listener : blueListeners) {
      listener.notifyView("Your turn has started!");
    }
  }

  private void turnStarted(Player player) {
    for (ModelUpdateSubscriber listener : redListeners) {
      listener.changeTurn();
    }
    for (ModelUpdateSubscriber listener : blueListeners) {
      listener.changeTurn();
    }
  }

  /**
   * Notification for when the game has ended.
   */
  @Override
  public void gameEnded() {
    for (ModelUpdateSubscriber listener : redListeners) {
      if (getWinner() == null) {
        listener.notifyView("Game has ended. Tie game.");
      } else {
        listener.notifyView("Game has ended. Winner is " + getWinner());
      }
    }
    for (ModelUpdateSubscriber listener : blueListeners) {
      if (getWinner() == null) {
        listener.notifyView("Game has ended. Tie game.");
      } else {
        listener.notifyView("Game has ended. Winner is " + getWinner());
      }
    }
  }

  /**
   * Adds a listener for the model update events.
   * @param listener the listener to add
   * @param player the player to give updates for
   * @throws IllegalArgumentException if listener is null
   *                                  if player is null
   */
  @Override
  public void addListener(ModelUpdateSubscriber listener, Player player) {
    if (listener == null || player == null) {
      throw new IllegalArgumentException("Listener and player cannot be null");
    }
    if (player == Player.RED) {
      redListeners.add(listener);
    } else {
      blueListeners.add(listener);
    }
  }
  //</editor-fold>
}
