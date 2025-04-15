package cs3500.pawnsboard.model.adapter;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.provider.model.InfluenceType;

/**
 * An adapter from the provider's card to the original model's card.
 */
public class OurCardAdapt implements Card {

  private final cs3500.pawnsboard.provider.model.Card card;

  /**
   * Constructs a new PawnsCard adapted from a provider card.
   * @param card the card
   * @throws IllegalArgumentException if card is null
   */
  public OurCardAdapt(cs3500.pawnsboard.provider.model.Card card) {
    if (card == null) {
      throw new IllegalArgumentException("card cannot be null");
    }
    this.card = card;
  }

  /**
   * Returns the unique name of the card.
   *
   * @return the card name
   */
  @Override
  public String getName() {
    return card.getName();
  }

  /**
   * Returns the cost in pawns to play this card on the board.
   *
   * @return the cost
   */
  @Override
  public int getCost() {
    return card.getCost().getNumCost();
  }

  /**
   * Returns the value of this card.
   *
   * @return the value
   */
  @Override
  public int getValue() {
    return card.getValueScore();
  }

  /**
   * Returns the influence grid for this card.
   */
  @Override
  public boolean[][] getInfluence() {
    boolean[][] grid = new boolean[5][5];
    InfluenceType[][] oldGrid = card.getInfluenceGrid();
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (oldGrid[row][col] == InfluenceType.INFLUENCE) {
          grid[row][col] = true;
        }
      }
    }
    return grid;
  }
}
