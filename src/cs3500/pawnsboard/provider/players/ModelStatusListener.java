package cs3500.players;

import cs3500.model.PlayerColor;

/**
 * Interface for listening to model status changes.
 * Implemented by controllers to be notified when it's their player's turn.
 */
public interface ModelStatusListener {
  /**
   * Called when it becomes a specific player's turn.
   *
   * @param playerColor the player whose turn it is
   */
  void onTurnChange(PlayerColor playerColor);

  /**
   * Called when the game ends.
   *
   * @param winner       the winning player
   * @param winningScore the winning player's total final score
   */
  void onGameEnd(PlayerColor winner, int winningScore);

  /**
   * Called when the game starts.
   */
  void onGameStart();
}