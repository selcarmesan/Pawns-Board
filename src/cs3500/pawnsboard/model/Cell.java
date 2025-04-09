package cs3500.pawnsboard.model;

/**
 * A singular cell on the playing board for a game of pawns board.  Each contains either a number
 * of pawns 0-3, or a played card, with the card taking priority.
 */
public interface Cell extends CellReadOnly {

  /**
   * Increments the number of pawns by one if less than 3.
   *
   * @param player the player to change ownership to, only used if no owner was previously present
   * @throws IllegalArgumentException if pawns currently not zero
   *                                  if player is null
   * @throws IllegalStateException if cell already has a played card
   */
  void addPawn(Player player);

  /**
   * Increments the number of pawns by one if less than 3.
   *
   * @throws IllegalArgumentException if the cell currently has no owner
   * @throws IllegalStateException if cell already has a played card
   */
  void addPawn();

  /**
   * Changes the ownership of the cell.  Can only do so if it has pawns on it, and no card.
   *
   * @param newOwner the new owner
   * @throws IllegalArgumentException if board has no owner currently (zero pawns)
   *                                  if the new owner is null and the current owner is not null
   * @throws IllegalStateException if cell already has a played card
   */
  void changeOwner(Player newOwner);

  /**
   * Sets the card of this cell to the given card if one has not already been played.
   *
   * @param card the card
   * @param owner the card's owner
   * @throws IllegalStateException if the cell already has a card set
   * @throws IllegalArgumentException if supplied card is null
   *                                  if supplied owner is null
   */
  void playCard(Card card, Player owner);

}
