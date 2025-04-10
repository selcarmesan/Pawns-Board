package cs3500.provider.model;

import java.util.List;

/**
 * Represents a read-only view of the game model.
 * Provides access to observe the state of the game without modifying it.
 */
public interface ReadonlyPawnsBoardModel {

  /**
   * Returns the board used in the game.
   * @return the game's board
   */
  Board getBoard();

  /**
   * Returns the red player in the game.
   * @return the red player
   */
  Player getRedPlayer();

  /**
   * Returns the blue player in the game.
   * @return the blue player
   */
  Player getBluePlayer();

  /**
   * Returns the player who is currently playing.
   * @return the player in the current turn
   */
  Player getCurrentPlayer();

  /**
   * Checks if the game is over depending on the current
   * board state.
   * @return true if all cells of the board have been filled
   *         and if there are no more pawns left, false if otherwise.
   */
  boolean isGameOver();

  /**
   * Gets the number of rows in the board.
   * @return the number of rows
   */
  int boardGetRows();

  /**
   * Gets the number of columns in the board.
   * @return the number of columns
   */
  int boardGetColumns();

  /**
   * Gets a cell given a specific position in the board.
   * @param row row the cell is located
   * @param col column the cell is located
   * @return the specified cell
   */
  Cell getCell(int row, int col);

  /**
   * Gets a given player's current hand.
   * @param color the player's color - RED or BLUE
   * @return list of cards the player holds
   */
  List<Card> getPlayerHand(PlayerColor color);

  /**
   * Given a specific cell in the board, return its owner.
   * @param row row the cell is located
   * @param col column the cell is located
   * @return which player owns the given cell
   */
  PlayerColor getCellOwner(int row, int col);

  /**
   * Determines if a player's move is legal in the game.
   * @param card the card the player wishes to use
   * @param row row the cell is located
   * @param col column the cell is located
   * @return true is the play is allowed, false if otherwise
   */
  boolean isLegalMove(Card card, int row, int col);

  /**
   * Calculates the total scores for both players.
   * @return An array where the first element is Red's total score
   *         and the second is Blue's total score.
   */
  int[] calculateScores();

  /**
   * Calculates the specific row score for a given player.
   * @param row the row to calculate
   * @param color the player's color - RED or BLUE
   * @return the current row score for the given player
   */
  int getRowScore(int row, PlayerColor color);

  /**
   * Returns the player who won the game.
   * @return the winning player - RED or BLUE
   * @throws IllegalStateException if the game is a tie
   */
  PlayerColor getWinner();

  /**
   * Returns the number of pawns present at a given cell.
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns
   */
  int getNumberOfPawns(int row, int col);

  /**
   * Returns the total number of pawns on each cell of the board.
   * @return a 2D-grid with the amount of pawns on each cell
   */
  int[][] getFullPawnCounts();

  /**
   * Returns the complete view of cell ownerships on each cell of the board.
   * @return a 2D-grid with the ownership on each cell
   */
  PlayerColor[][] getOwnerships();

  /**
   * Provide the current score of a player.
   * Kept in an index array -- Red is [0] and Blue is [1].
   * @return the player's current score
   */
  int[][] getPlayerRowScores();

  /**
   * Gets the winning player's total score.
   * @return the winning player's score
   */
  int getWinnerScore();
}