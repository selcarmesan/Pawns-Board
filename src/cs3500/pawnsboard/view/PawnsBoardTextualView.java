package cs3500.pawnsboard.view;

import java.util.Objects;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * PawnsBoardTextualView displays a game of PawnsBoard.
 */
public class PawnsBoardTextualView implements PawnsBoardView {

  private final PawnsBoardReadOnly model;

  /**
   * Constructs a PawnsBoardTextualView from a given PawnsBoard model.
   *
   * @param model PawnsBoard model
   */
  public PawnsBoardTextualView(PawnsBoardReadOnly model) {
    if (Objects.isNull(model)) {
      throw new IllegalArgumentException("Invalid Model");
    }
    this.model = model;
  }

  /**
   * Gets the model of the textual view.
   * @return model
   */
  public PawnsBoardReadOnly getModel() {
    return model;
  }

  @Override
  public void update() {
    // Unused
  }

  /**
   * Returns the current board and game status as a textual representation.  Each line has two
   * integers to the far left and far right, representing the total score of red (left) and total
   * score of blue (right).  For each individual cell on the board, an empty cell is represented as
   * an underscore, a cell with pawns is represented by that number of pawns, an integer 1-3, and
   * a cell with a card played is represented by the color of that card's owner, either R for red
   * or B for blue.
   * @return the textual representation of the board
   */
  @Override
  public String toString() {
    String output = "";
    for (int row = 0; row < model.getRows(); row++) {
      output = String.format(output + "%s ", model.getRowScore(Player.RED, row));
      for (int col = 0; col < model.getCols(); col++) {
        if (Objects.isNull(model.getCellAt(row, col).getOwner())) {
          output = String.format(output + "%s", "_");
        } else if (!Objects.isNull(model.getCellAt(row, col).getCard())) {
          if (model.getCellAt(row, col).getOwner() == Player.RED) {
            output = String.format(output + "%s", "R");
          }
          if (model.getCellAt(row, col).getOwner() == Player.BLUE) {
            output = String.format(output + "%s", "B");
          }
        } else {
          output = String.format(output + "%s", model.getCellAt(row, col).getPawns());
        }
      }
      output = String.format(output + " %s\n", model.getRowScore(Player.BLUE, row));
    }
    return output;
  }
}
