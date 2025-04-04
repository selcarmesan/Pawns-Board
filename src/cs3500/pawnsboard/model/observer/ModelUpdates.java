package cs3500.pawnsboard.model.observer;

import cs3500.pawnsboard.model.Player;

/**
 * Collection of notable notifications needed within the model of a Pawns Board game that the
 * player needs to be aware of.
 */
public interface ModelUpdates {

  /**
   * Notification for when the turn of player red has just begun.
   */
  void turnStartedRed();

  /**
   * Notification for when the turn of player blue has just begun.
   */
  void turnStartedBlue();

  /**
   * Notification for when the game has ended.
   */
  void gameEnded();

  /**
   * Adds a listener for the model update events for the particular player.
   * @param listener the listener to add
   * @param player the player to give updates for
   * @throws IllegalArgumentException if listener is null
   *                                  if player is null
   */
  void addListener(ModelUpdateSubscriber listener, Player player);

}
