package cs3500.pawnsboard.provider.players;

import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of IPlayer for machine players.
 * Uses a simple strategy to make moves automatically.
 */
public class MachinePlayer implements IPlayer {
  private final PlayerColor color;
  private final List<Card> hand;
  private final List<PlayerActionListener> listeners = new ArrayList<>();
  private final Random random = new Random();

  public MachinePlayer(PlayerColor color, List<Card> hand) {
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
    // Simple strategy: pick a random card and random valid position
    if (!hand.isEmpty()) {
      Card card = hand.get(random.nextInt(hand.size()));
      listeners.forEach(l -> l.onCardSelected(card));

      // For simplicity, just pick random cell (in real game would need valid cells)
      int row = random.nextInt(5); // Assuming 5 rows
      int col = random.nextInt(7); // Assuming 7 columns
      listeners.forEach(l -> l.onCellSelected(row, col));

      listeners.forEach(PlayerActionListener::onMoveConfirmed);
    } else {
      listeners.forEach(PlayerActionListener::onTurnPassed);
    }
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