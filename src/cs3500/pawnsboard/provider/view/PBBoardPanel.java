package cs3500.pawnsboard.provider.view;

import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.PlayerColor;
import java.awt.Point;

/**
 * Interface to design the panel responsible for showcasing the board of Pawns Board.
 */
public interface PBBoardPanel {

  /**
   * Redraws the given game panel/component (cards, hands, scores, etc.)
   */
  void refresh();

  /**
   * Clears any cells selected.
   */
  void clearSelection();

  /**
   * Dynamically sets the board size.
   * Allows for window resizing and board size customization.
   * @param rows the number of rows in the board.
   * @param cols the number of columns in the board.
   */
  void setBoardSize(int rows, int cols);

  /**
   * Sets the individual row score for a specific row position in the board.
   * @param row the row to be updated
   * @param score the new score
   */
  void boardSetRowScore(int row, int score);

  /**
   * Updates the pawn counts from the controller.
   * @param pawnCounts stores the number of pawns on each cell on the board
   */
  void setPawnCounts(int[][] pawnCounts);

  /**
   * Updates the cell ownership from the controller.
   * @param ownership stores the ownership of each cell on the board
   */
  void setCellOwnership(PlayerColor[][] ownership);

  /**
   * Places cards on cell after a player's valid move.
   * Cards are represented on the board by their score value.
   * @param card the card to be placed
   * @param selectedCell the cell/position on the board to place
   */
  void placeCardOnCell(Card card, Point selectedCell);

  /**
   * Gets the player's selected cell.
   * @return the player's selected cell.
   */
  Point getSelectedCell();

  /**
   * Gets a 2D-Array of player row scores.
   * Index 0 represents Red's scores and index 1 represents Blue's scores.
   * @return a 2D-Array with both player's row scores
   */
  int[][] getPlayerRowScores();

  /**
   * Updates the row scores for both players and repaints the board.
   * @param scores array with the updated scores -- Red [0] and Blue [1]
   */
  void setPlayerRowScores(int[][] scores);
}
