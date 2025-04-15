package cs3500.pawnsboard.provider.model;

/**
 * Interface for the game board of Pawns Boards.
 */
public interface Board {
  /**
   * Returns the number of rows in a board.
   * @return the number of rows.
   */
  int getRows();

  /**
   * Returns the number of columns in a board.
   * @return the number of columns.
   */
  int getColumns();

  /**
   * Returns the cell number (row and column) on a grid when asked.
   * @return the cell number (row and column) on a grid when asked.
   */
  Cell[][] getGrid();

  /**
   * Applies the card influence after its placement.
   * @param player the player who played a card
   * @param row the x-coordinate of the cell's position on the board
   * @param col the y-coordinate of the cell's position on the board
   * @param card the card that was placed
   */
  void applyCardInfluence(Player player, int row, int col, Card card);

  /**
   * Checks if the game is over depending on the current
   * board state.
   * @return true if all cells of the board have been filled
   *         and if there are no more pawns left, false if otherwise.
   */
  boolean isGameOver();

  /**
   * Sets the game status as desired.
   * @param isGameOver true is the game is over, false if otherwise
   * @return true is the game is over, false if otherwise
   */
  boolean setGameOver(boolean isGameOver);

  /**
   * Calculates the total scores for both players.
   * @return An array where the first element is Red's total score
   *         and the second is Blue's total score.
   */
  int[] calculateTotalScores();

  /**
   * Calculates the score for a specific player in a given row.
   *
   * @param row the row index
   * @param color the player's color - RED or BLUE
   * @return the score for the player in the specified row
   * @throws IllegalArgumentException if the row is out of bounds.
   */
  int getRowScore(int row, PlayerColor color);
}
