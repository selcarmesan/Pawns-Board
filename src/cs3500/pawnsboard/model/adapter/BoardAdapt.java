package cs3500.pawnsboard.model.adapter;

import java.util.Arrays;
import java.util.Objects;

import cs3500.pawnsboard.model.Cell;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.provider.model.Board;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.PlayerColor;

/**
 * Board implementation to be used in the adapter.
 */
public class BoardAdapt implements Board {

  private final Cell[][] grid;
  private boolean isGameOver;

  /**
   * Creates a new board with the desired attributes.
   *
   * @param grid       the grid of cells
   * @param isGameOver whether the game is over
   * @throws IllegalArgumentException if grid is null or contains null
   */
  public BoardAdapt(Cell[][] grid, boolean isGameOver) {
    if (grid == null || Arrays.stream(grid).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Grid is null or contains null");
    }
    this.grid = grid;
    this.isGameOver = isGameOver;
  }

  /**
   * Returns the number of rows in a board.
   *
   * @return the number of rows.
   */
  @Override
  public int getRows() {
    return grid.length;
  }

  /**
   * Returns the number of columns in a board.
   *
   * @return the number of columns.
   */
  @Override
  public int getColumns() {
    return grid[0].length;
  }

  /**
   * Returns the cell number (row and column) on a grid when asked.
   *
   * @return the cell number (row and column) on a grid when asked.
   */
  @Override
  public cs3500.pawnsboard.provider.model.Cell[][] getGrid() {
    cs3500.pawnsboard.provider.model.Cell[][] newGrid =
            new cs3500.pawnsboard.provider.model.Cell[getRows()][getColumns()];
    for (int i = 0; i < newGrid.length; i++) {
      for (int j = 0; j < newGrid[0].length; j++) {
        newGrid[i][j] = new CellAdapt(grid[i][j]);
      }
    }
    return newGrid;
  }

  /**
   * Applies the card influence after its placement.
   *
   * @param player the player who played a card
   * @param row    the x-coordinate of the cell's position on the board
   * @param col    the y-coordinate of the cell's position on the board
   * @param card   the card that was placed
   */
  @Override
  public void applyCardInfluence(cs3500.pawnsboard.provider.model.Player player,
                                 int row, int col, Card card) {
    for (int i = -2; i < 3; i++) {
      for (int j = -2; j < 3; j++) {
        if (coordValid(row + i, col + j) && new OurCardAdapt(card).getInfluence()[i][j]) {
          Cell cell = grid[row + i][col + j];
          if (!(cell.getCard() == null && (i != 0 || j != 0))) {
            if (cell.getPawns() == 0) {
              cell.addPawn(colorToPlayer(player.getColor()));
            } else {
              cell.addPawn();
            }
          }
        }
      }
    }
  }

  private boolean coordValid(int row, int col) {
    return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
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
    return isGameOver;
  }

  /**
   * Sets the game status as desired.
   *
   * @param isGameOver true is the game is over, false if otherwise
   * @return true is the game is over, false if otherwise
   */
  @Override
  public boolean setGameOver(boolean isGameOver) {
    this.isGameOver = isGameOver;
    return isGameOver;
  }

  /**
   * Calculates the total scores for both players.
   *
   * @return An array where the first element is Red's total score
   *         and the second is Blue's total score.
   */
  @Override
  public int[] calculateTotalScores() {
    int[] scores = new int[2];
    for (int i = 0; i < grid.length; i++) {
      if (getRowScore(i, PlayerColor.RED) > getRowScore(i, PlayerColor.BLUE)) {
        scores[0] += getRowScore(i, PlayerColor.RED);
      } else if (getRowScore(i, PlayerColor.BLUE) > getRowScore(i, PlayerColor.RED)) {
        scores[1] += getRowScore(i, PlayerColor.BLUE);
      }
    }
    return scores;
  }

  /**
   * Calculates the score for a specific player in a given row.
   *
   * @param row   the row index
   * @param color the player's color - RED or BLUE
   * @return the score for the player in the specified row
   * @throws IllegalArgumentException if the row is out of bounds.
   */
  @Override
  public int getRowScore(int row, PlayerColor color) {
    int score = 0;
    for (int i = 0; i < grid[0].length; i++) {
      if (grid[row][i].getCard() != null && grid[row][i].getOwner() == colorToPlayer(color)) {
        score += grid[row][i].getCard().getValue();
      }
    }
    return score;
  }

  private cs3500.pawnsboard.model.Player colorToPlayer(PlayerColor color) {
    if (color == PlayerColor.RED) {
      return Player.RED;
    }
    return cs3500.pawnsboard.model.Player.BLUE;
  }
}
