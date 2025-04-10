package cs3500.provider.view;

import cs3500.provider.model.Board;
import cs3500.provider.model.Cell;
import cs3500.provider.model.GameModel;
import cs3500.provider.model.PlayerColor;

/**
 * A textual view for displaying the Pawns Board game state.
 */
public class GameTextualView {
  private final GameModel game;

  /**
   * Constructs a textual view for the given game model.
   *
   * @param game The game model to render.
   * @throws IllegalArgumentException if the game is null.
   */
  public GameTextualView(GameModel game) {
    if (game == null) {
      throw new IllegalArgumentException("Game model cannot be null.");
    }
    this.game = game;
  }

  /**
   * Renders the current board state to the console.
   */
  public void render() {
    System.out.println(this);
  }

  /**
   * Generates a string representation of the board state.
   *
   * @return The textual representation of the board.
   */
  @Override
  public String toString() {
    Board board = game.getBoard();
    Cell[][] grid = board.getGrid();
    StringBuilder sb = new StringBuilder();

    // Iterate over each row
    for (int i = 0; i < board.getRows(); i++) {
      StringBuilder rowText = new StringBuilder();

      // Iterate over each cell in the row
      for (int j = 0; j < board.getColumns(); j++) {
        Cell cell = grid[i][j];

        if (cell.getCard() != null) {
          // Cell contains a card -- Red or Blue owner
          rowText.append(cell.getOwner() == PlayerColor.RED ? "R" : "B");
        } else if (cell.getPawns() > 0) {
          // Cell contains pawns
          rowText.append(cell.getPawns()); // Number of pawns (1, 2, or 3)
        } else {
          // Cell is empty
          rowText.append("_");
        }
      }

      // Calculate row scores for Red and Blue
      int redScore = calculateRowScore(grid[i], PlayerColor.RED);
      int blueScore = calculateRowScore(grid[i], PlayerColor.BLUE);

      // Format: Red_RowScore   Row_Content   Blue_RowScore
      sb.append(redScore).append(" ").append(rowText).append(" ").append(blueScore).append("\n");
    }
    // Return the board output as a textual view (string)
    return sb.toString();
  }

  /**
   * Calculates the row score for a specific player.
   *
   * @param row         The row of cells.
   * @param playerColor The player's color ("Red" or "Blue").
   * @return The row score for the player.
   */
  private static int calculateRowScore(Cell[] row, PlayerColor playerColor) {
    int score = 0;

    for (Cell cell : row) {
      if (cell.getCard() != null && cell.getOwner() == playerColor) {
        score += cell.getCard().getValueScore();
      }
    }
    return score;
  }
}