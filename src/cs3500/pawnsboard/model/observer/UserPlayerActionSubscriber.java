package cs3500.pawnsboard.model.observer;

/**
 * Contains the necessary methods for responding for a user method being taken.
 */
public interface UserPlayerActionSubscriber {

  /**
   * Calls for a move to be made for the player.
   * @param row the row
   * @param col the column
   * @param index the hand index
   */
  void makeMove(int row, int col, int index);

  /**
   * Calls for a turn to be skipped for the player.
   */
  void passMove();
}
