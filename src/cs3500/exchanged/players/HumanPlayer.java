package cs3500.exchanged.players;

import cs3500.exchanged.model.Card;
import cs3500.exchanged.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IPlayer for human players.
 * Delegates actual moves to the view/controller through PlayerActionListener.
 */
public class HumanPlayer implements IPlayer {
  private final PlayerColor color;
  private final List<Card> hand;
  private final List<PlayerActionListener> listeners = new ArrayList<>();

  public HumanPlayer(PlayerColor color, List<Card> hand) {
    this.color = color;
    this.hand = new ArrayList<>(hand);
  }

  /**
   * Gets the color of the player.
   *
   * @return the color of the player.
   */
  @Override
  public PlayerColor getColor() {
    return color;
  }

  /**
   * Listens to the Players action to determine next move.
   *
   * @param listener the listener to add players move.
   */
  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    listeners.add(listener);
  }

  /**
   * Officially starts players turn to reciprocate.
   */
  @Override
  public void startTurn() {
    // Human player makes moves through UI, so nothing to do here
  }

  /**
   * Gets the hand of a player.
   *
   * @return the hand of a player.
   */
  @Override
  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }
}