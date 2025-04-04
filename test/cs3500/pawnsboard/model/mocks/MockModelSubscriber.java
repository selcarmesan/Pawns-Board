package cs3500.pawnsboard.model.mocks;

import cs3500.pawnsboard.model.observer.ModelUpdateSubscriber;

/**
 * Mock for a subscriber to a model updates class.
 */
public class MockModelSubscriber implements ModelUpdateSubscriber {

  private final StringBuilder log;

  /**
   * Creates a new mock.
   * @param log log for updates
   */
  public MockModelSubscriber(StringBuilder log) {
    this.log = log;
  }

  /**
   * Notifies the GUI in a popup message of the provided message.
   *
   * @param message the message notification
   */
  @Override
  public void notifyView(String message) {
    log.append(message);
  }

  /**
   * Responds to the change of a turn, likely disabling inputs from the other player.
   */
  @Override
  public void changeTurn() {
    log.append("turn");
  }
}
