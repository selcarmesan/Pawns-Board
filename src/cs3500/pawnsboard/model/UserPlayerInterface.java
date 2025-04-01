package cs3500.pawnsboard.model;

/**
 * The interface in which a user-player interacts with the system, either through user input or
 * through an automated player using strategies.  Has a model to delegate to when instantiated, as
 * well as a specification for which player this is.
 */
public interface UserPlayerInterface {

  /**
   * Selects the specified card from the player's hand to place in queue to be played.
   * @param index the index of the card
   * @throws IllegalArgumentException if index is not valid for player
   * @throws IllegalStateException if not currently this player's turn
   */
  void selectCard(int index);

  /**
   * Selects the specified row and column on the board to put in queue for the next play.
   * @param row the row to place the card in
   * @param col the column to place the card in
   * @throws IllegalArgumentException if row or column are out of bounds
   * @throws IllegalStateException if not currently this player's turn
   */
  void selectCell(int row, int col);

  /**
   * Plays the card based on the already selected/highlighted cell on the board, and card within the
   * hand.
   * @throws IllegalArgumentException if the move is invalid given the selected space and card
   *                                  if a cell row and column has not been selected yet
   *                                  if a card has not been selected yet
   * @throws IllegalStateException if it is not this player's turn
   */
  void makePlay();

  /**
   * Skips the current player's turn.
   * @throws IllegalStateException if it is not this player's turn
   */
  void skipTurn();
}
