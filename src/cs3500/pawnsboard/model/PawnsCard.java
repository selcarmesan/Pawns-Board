package cs3500.pawnsboard.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of a card for a game of pawns board.  Has a unique name, cost, point
 * value, and influence pattern.  Used to make moves during a game.
 */
public class PawnsCard implements Card {

  private final String name;
  private final int cost;
  private final int value;
  private final boolean[][] influence;

  /**
   * Creates a new game card.
   *
   * @param name the name of the card
   * @param cost the cost of the card, being 1-3 pawns
   * @param value the positive value of the card
   * @param influence the 5x5 grid that represents where the card affects on the board when played
   * @throws IllegalArgumentException if name or influence are null
   *                                  if pawn cost is not between 1 and 3
   *                                  if value is not positive
   *                                  if influence is not a 5x5 grid
   */
  public PawnsCard(String name, int cost, int value, boolean[][] influence) {
    if (name == null || influence == null) {
      throw new IllegalArgumentException("Name, color, and influence must be defined");
    }
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Cost must be between 1 and 3");
    }
    if (value < 1) {
      throw new IllegalArgumentException("Value must be positive");
    }
    if ((influence.length != influence[0].length) || (influence.length != 5)) {
      throw new IllegalArgumentException("Influence must be a 5x5 grid");
    }
    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influence = new boolean[5][5];
    for (int i = 0; i < 5; i++) {
      System.arraycopy(influence[i], 0, this.influence[i], 0, 5);
    }
  }

  /**
   * Returns the unique name of the card.
   *
   * @return the card name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Returns the cost in pawns to play this card on the board.
   *
   * @return the cost
   */
  @Override
  public int getCost() {
    return cost;
  }

  /**
   * Returns the value of this card.
   *
   * @return the value
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * Returns the influence grid for this card.
   */
  @Override
  public boolean[][] getInfluence() {
    boolean[][] copy = new boolean[5][5];
    for (int i = 0; i < 5; i++) {
      System.arraycopy(influence[i], 0, copy[i], 0, 5);
    }
    return copy;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PawnsCard) {
      PawnsCard other = (PawnsCard) o;
      return (this.cost == other.cost && this.value == other.value && this.name.equals(other.name)
              && Arrays.deepEquals(this.influence, other.influence));
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cost, value, Arrays.deepHashCode(influence));
  }
}
