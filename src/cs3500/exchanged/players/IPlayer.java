package cs3500.exchanged.players;

import cs3500.exchanged.model.Card;
import cs3500.exchanged.model.PlayerColor;

import java.util.List;

/**
 * Interface representing a player in the game, either human or machine.
 */
public interface IPlayer {
  /**
   * Gets the color of this player.
   *
   * @return the player's color
   */
  PlayerColor getColor();

  /**
   * Adds a listener for player actions.
   *
   * @param listener the listener to add
   */
  void addPlayerActionListener(PlayerActionListener listener);

  /**
   * Called when it's this player's turn to make a move.
   */
  void startTurn();

  /**
   * Gets the current hand of this player.
   *
   * @return list of cards in the player's hand
   */
  List<Card> getHand();
}