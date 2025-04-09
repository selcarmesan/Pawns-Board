package cs3500.pawnsboard.view;

/**
 * Interface for a visual representation of PawnsBoardGame.
 * Extends PawnsBoardView.
 */
public interface PawnsBoardVisual extends PawnsBoardView {

  /**
   * Sets the last cell chosen by the user with the given cell.
   * @param cell last chosen cell
   */
  void setLastChosenCell(PawnsBoardCellButton cell);

  /**
   * Sets the last card chosen by the user with the given card.
   * @param card last chosen card
   */
  void setLastChosenCard(PawnsBoardCardPanel card);

  /**
   * Gets the last cell chosen by the user.
   * @return lastChosenCell
   */
  PawnsBoardCellButton getLastChosenCell();

  /**
   * Gets the last card chosen by the user.
   * @return lastChosenCard
   */
  PawnsBoardCardPanel getLastChosenCard();

  /**
   * Notifies the GUI in a popup message of the provided message.
   * @param message the message notification
   * @throws IllegalArgumentException if message is null
   */
  void notify(String message);
}
