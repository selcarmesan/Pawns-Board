package cs3500.pawnsboard.model.mocks;

import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;

/**
 * Mock class for testing user player action subscriptions.
 */
public class MockUserPlayerSubscriber implements UserPlayerActionSubscriber {

  private StringBuilder log;

  /**
   * Creates a new mock.
   * @param log log for updates
   */
  public MockUserPlayerSubscriber(StringBuilder log) {
    this.log = log;
  }

  /**
   * Calls for a move to be made for the player.
   *
   * @param row   the row
   * @param col   the column
   * @param index the hand index
   */
  @Override
  public void makeMove(int row, int col, int index) {
    log.append(row + ", " + col + ", " + index);
  }

  /**
   * Calls for a turn to be skipped for the player
   */
  @Override
  public void passMove() {
    log.append("pass");
  }
}
