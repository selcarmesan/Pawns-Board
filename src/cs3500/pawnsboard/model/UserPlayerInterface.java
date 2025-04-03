package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.strategies.PawnsBoardStrategy;

/**
 * Necessary actions a UserPlayer can take that are unrelated to human-player-driven events.
 */
public interface UserPlayerInterface {

  /**
   * Decides and plays the next move for this player.
   *
   * @throws IllegalStateException if this player is not a machine
   */
  void decideMove();

  /**
   * Returns which player color that this player represents.
   * @return the player color
   */
  Player getThisPlayer();

  /**
   * Returns whether this player represents a machine or not.
   * @return if it is a machine
   */
  boolean isMachine();
}
