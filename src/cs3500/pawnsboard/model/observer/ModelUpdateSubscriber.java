package cs3500.pawnsboard.model.observer;

import cs3500.pawnsboard.model.Player;

/**
 * Houses the necessary methods for reacting to updates from the model about the game state.
 */
public interface ModelUpdateSubscriber {

  /**
   * Notifies the GUI in a popup message of the provided message.
   * @param message the message notification
   */
  void notifyView(String message);

  /**
   * Responds to the change of a turn, likely disabling inputs from the other player.
   */
  void changeTurn();
}
