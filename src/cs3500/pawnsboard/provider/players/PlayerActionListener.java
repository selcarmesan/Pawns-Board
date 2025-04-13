package cs3500.players;

/**
 * Interface for listening to player actions in the game.
 * Implemented by controllers to handle player moves and turn passes.
 */
public interface PlayerActionListener {
  /**
   * Called when a player selects a card to play.
   *
   * @param card the selected card
   */
  void onCardSelected(Card card);

  /**
   * Called when a player selects a cell to play on.
   *
   * @param row the row of the selected cell
   * @param col the column of the selected cell
   */
  void onCellSelected(int row, int col);

  /**
   * Called when a player confirms their move.
   */
  void onMoveConfirmed();

  /**
   * Called when a player passes their turn.
   */
  void onTurnPassed();
}