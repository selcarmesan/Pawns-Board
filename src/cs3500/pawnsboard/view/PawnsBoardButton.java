package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.CellReadOnly;

/**
 * An interface button that represents PawnsBoardGame board.
 */
public interface PawnsBoardButton {

  /**
   * Gets the row of the button.
   * @return row index
   */
  int getRow();

  /**
   * Gets the col of the button.
   * @return col index
   */
  int getCol();

  /**
   * Gets the cell of the button.
   * @return cell
   */
  CellReadOnly getCell();
}
