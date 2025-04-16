package cs3500.pawnsboard.model.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.provider.model.Board;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.Cell;
import cs3500.pawnsboard.provider.model.PawnsBoardModel;
import cs3500.pawnsboard.provider.model.Player;
import cs3500.pawnsboard.provider.model.PlayerColor;
import cs3500.pawnsboard.provider.players.ModelStatusListener;

/**
 * Adapter class that acts as the provider's game, delegating to ours.
 */
public class PawnsBoardAdapt implements PawnsBoardModel {

  private final PawnsBoard model;

  private final List<cs3500.pawnsboard.model.Card> startingRedDeck;
  private final List<cs3500.pawnsboard.model.Card> startingBlueDeck;

  private final List<ModelStatusListener> listeners;

  /**
   * Creates a new adapted pawns board.
   *
   * @param model    the model to adapt
   * @param redDeck  the red player's starting deck
   * @param blueDeck the blue player's starting deck
   * @throws IllegalArgumentException if model or decks are null, or if decks contain null
   */
  public PawnsBoardAdapt(PawnsBoard model, List<cs3500.pawnsboard.model.Card> redDeck,
                         List<cs3500.pawnsboard.model.Card> blueDeck) {
    if (model == null || redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("model and decks cannot be null");
    }
    if (redDeck.stream().anyMatch(Objects::isNull) || blueDeck.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("decks cannot contain null");
    }
    this.model = model;
    this.startingRedDeck = redDeck;
    this.startingBlueDeck = blueDeck;
    listeners = new ArrayList<>();
  }

  /**
   * Allows the current player to make a move by placing a card on the board.
   *
   * @param card the card to be placed
   * @param row  the x-coordinate of the cell's position on the board
   * @param col  the y-coordinate of the cell's position on the board
   */
  @Override
  public void playTurn(Card card, int row, int col) {
    int index = getIndexFromCard(card);
    model.placeCard(row, col, index);
    // CANNOT accurately adapt/implement this, as theirs takes in a card for placing, while ours
    // uses an index for which hand slot to use.  It could be functionally implemented by checking
    // their hand to see if a matching card can be found, yet there is no way to ascertain it is
    // the same card that is desired to be used, if there are duplicates.
  }

  private int getIndexFromCard(Card card) {
    cs3500.pawnsboard.model.Card newCard = new OurCardAdapt(card);
    for (int i = 0; i < model.getHand(model.getCurrentTurn()).size(); i++) {
      cs3500.pawnsboard.model.Card handCard = model.getHand(model.getCurrentTurn()).get(i);
      if (newCard.getName().equals(handCard.getName()) && newCard.getCost() == handCard.getCost()
              && newCard.getValue() == handCard.getValue()
              && newCard.getInfluence() == handCard.getInfluence()
      ) {
        return i;
      }
    }
    throw new IllegalArgumentException("Card not found");
  }

  /**
   * Switch turns between the players.
   * Allows players to pass their turn.
   */
  @Override
  public void passTurn() {
    model.skipTurn();
  }

  /**
   * Creates a duplicated version o the board at the time.
   *
   * @return the game board's copy
   */
  @Override
  public Board copyBoard() {
    return new BoardAdapt(model.getBoard(), model.isGameOver());
  }

  /**
   * Adds a listener for model status changes.
   *
   * @param listener the listener to add
   */
  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  /**
   * Strats the model of Pawns Boards with the default state.
   */
  @Override
  public void startGame() {
    model.startGame(startingRedDeck, startingBlueDeck,
            Math.min(startingRedDeck.size() / 3, startingBlueDeck.size() / 3), false);
    // Is technically a full implementation, yet it is worth nothing that their implementation lacks
    // the necessary functionality for custom deck size and drawing cards.
  }

  /**
   * Returns the board used in the game.
   *
   * @return the game's board
   */
  @Override
  public Board getBoard() {
    return copyBoard();
    // Could not implement properly, as this adapter does not use an actual Board object for
    // keeping track of the game state, due to being an adapter.
  }

  /**
   * Returns the red player in the game.
   *
   * @return the red player
   */
  @Override
  public Player getRedPlayer() {
    cs3500.pawnsboard.model.Player p = cs3500.pawnsboard.model.Player.RED;
    return new PlayerAdapt(p, model.getRemainingDeck(p), model.getHand(p));
  }

  /**
   * Returns the blue player in the game.
   *
   * @return the blue player
   */
  @Override
  public Player getBluePlayer() {
    cs3500.pawnsboard.model.Player p = cs3500.pawnsboard.model.Player.BLUE;
    return new PlayerAdapt(p, model.getRemainingDeck(p), model.getHand(p));
  }

  /**
   * Returns the player who is currently playing.
   *
   * @return the player in the current turn
   */
  @Override
  public Player getCurrentPlayer() {
    if (model.getCurrentTurn() == cs3500.pawnsboard.model.Player.RED) {
      return getRedPlayer();
    } else {
      return getBluePlayer();
    }
  }

  /**
   * Checks if the game is over depending on the current
   * board state.
   *
   * @return true if all cells of the board have been filled
   *         and if there are no more pawns left, false if otherwise.
   */
  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  /**
   * Gets the number of rows in the board.
   *
   * @return the number of rows
   */
  @Override
  public int boardGetRows() {
    return model.getRows();
  }

  /**
   * Gets the number of columns in the board.
   *
   * @return the number of columns
   */
  @Override
  public int boardGetColumns() {
    return model.getCols();
  }

  /**
   * Gets a cell given a specific position in the board.
   *
   * @param row row the cell is located
   * @param col column the cell is located
   * @return the specified cell
   */
  @Override
  public Cell getCell(int row, int col) {
    return new CellAdapt(model.getCellAt(row, col));
  }

  /**
   * Gets a given player's current hand.
   *
   * @param color the player's color - RED or BLUE
   * @return list of cards the player holds
   */
  @Override
  public List<Card> getPlayerHand(PlayerColor color) {
    if (color == PlayerColor.RED) {
      return getRedPlayer().getHand();
    } else {
      return getBluePlayer().getHand();
    }
  }

  /**
   * Given a specific cell in the board, return its owner.
   *
   * @param row row the cell is located
   * @param col column the cell is located
   * @return which player owns the given cell
   */
  @Override
  public PlayerColor getCellOwner(int row, int col) {
    return getColorFromPlayer(model.getCellAt(row, col).getOwner());
  }

  /**
   * Determines if a player's move is legal in the game.
   *
   * @param card the card the player wishes to use
   * @param row  row the cell is located
   * @param col  column the cell is located
   * @return true is the play is allowed, false if otherwise
   */
  @Override
  public boolean isLegalMove(Card card, int row, int col) {
    return model.isMoveValid(row, col, getIndexFromCard(card), model.getCurrentTurn());
  }

  /**
   * Calculates the total scores for both players.
   *
   * @return An array where the first element is Red's total score
   *         and the second is Blue's total score.
   */
  @Override
  public int[] calculateScores() {
    int[] scores = new int[2];
    scores[0] = model.getTotalScore(cs3500.pawnsboard.model.Player.RED);
    scores[1] = model.getTotalScore(cs3500.pawnsboard.model.Player.BLUE);
    return scores;
  }

  /**
   * Calculates the specific row score for a given player.
   *
   * @param row   the row to calculate
   * @param color the player's color - RED or BLUE
   * @return the current row score for the given player
   */
  @Override
  public int getRowScore(int row, PlayerColor color) {
    return model.getRowScore(getPlayerFromColor(color), row);
  }

  /**
   * Returns the player who won the game.
   *
   * @return the winning player - RED or BLUE
   * @throws IllegalStateException if the game is a tie
   */
  @Override
  public PlayerColor getWinner() {
    return getColorFromPlayer(model.getWinner());
  }

  /**
   * Returns the number of pawns present at a given cell.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns
   */
  @Override
  public int getNumberOfPawns(int row, int col) {
    return model.getCellAt(row, col).getPawns();
  }

  /**
   * Returns the total number of pawns on each cell of the board.
   *
   * @return a 2D-grid with the amount of pawns on each cell
   */
  @Override
  public int[][] getFullPawnCounts() {
    int[][] pawns = new int[model.getRows()][model.getCols()];
    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getCols(); j++) {
        pawns[i][j] = model.getCellAt(i, j).getPawns();
      }
    }
    return pawns;
  }

  /**
   * Returns the complete view of cell ownerships on each cell of the board.
   *
   * @return a 2D-grid with the ownership on each cell
   */
  @Override
  public PlayerColor[][] getOwnerships() {
    PlayerColor[][] colors = new PlayerColor[model.getRows()][model.getCols()];
    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getCols(); j++) {
        colors[i][j] = getColorFromPlayer(model.getCellAt(i, j).getOwner());
      }
    }
    return colors;
  }

  /**
   * Provide the current score of a player.
   * Kept in an index array -- Red is [0] and Blue is [1].
   *
   * @return the player's current score
   */
  @Override
  public int[][] getPlayerRowScores() {
    int[][] scores = new int[model.getRows()][model.getCols()];
    for (int i = 0; i < model.getRows(); i++) {
      scores[0][i] = model.getRowScore(getPlayerFromColor(PlayerColor.RED), i);
      scores[1][i] = model.getRowScore(getPlayerFromColor(PlayerColor.BLUE), i);
    }
    return scores;
    //Javadoc is unclear whether the first index is for scores, or the second index is.
  }

  /**
   * Gets the winning player's total score.
   *
   * @return the winning player's score
   */
  @Override
  public int getWinnerScore() {
    if (model.getWinner() == cs3500.pawnsboard.model.Player.RED) {
      return model.getTotalScore(cs3500.pawnsboard.model.Player.RED);
    } else if (model.getWinner() == null) {
      return 0;
    } else {
      return model.getTotalScore(cs3500.pawnsboard.model.Player.BLUE);
    }
  }

  private PlayerColor getColorFromPlayer(cs3500.pawnsboard.model.Player player) {
    if (player == cs3500.pawnsboard.model.Player.RED) {
      return PlayerColor.RED;
    } else if (player == cs3500.pawnsboard.model.Player.BLUE) {
      return PlayerColor.BLUE;
    }
    return null;
  }

  private cs3500.pawnsboard.model.Player getPlayerFromColor(PlayerColor color) {
    if (color == PlayerColor.RED) {
      return cs3500.pawnsboard.model.Player.RED;
    } else if (color == PlayerColor.BLUE) {
      return cs3500.pawnsboard.model.Player.BLUE;
    }
    return null;
  }
}
