package cs3500.pawnsboard.model.observer;

/**
 * The interface in which a user-player interacts with the system, either through user input or
 * through an automated player using strategies.  Has a model to delegate to when instantiated, as
 * well as a specification for which player this is.
 */
public interface UserPlayerActions {

  /**
   * Plays the card based on the already selected/highlighted cell on the board, and card within the
   * hand.
   *
   * @param row the row
   * @param col the column
   * @param index the hand index
   */
  void makePlay(int row, int col, int index);

  /**
   * Skips the current player's turn.
   */
  void skipTurn();

  /**
   * Subscribes the listener to these action events.
   * @param listener the listener
   * @throws IllegalArgumentException if listener is null
   */
  void addListener(UserPlayerActionSubscriber listener);
}
