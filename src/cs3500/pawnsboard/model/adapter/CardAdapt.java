package cs3500.pawnsboard.model.adapter;

import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.provider.model.CardCost;
import cs3500.pawnsboard.provider.model.InfluenceType;
import cs3500.pawnsboard.provider.model.PlayerColor;

/**
 * Card implementation to be used in the adapter.
 */
public class CardAdapt implements cs3500.pawnsboard.provider.model.Card {

  private final cs3500.pawnsboard.model.Card card;
  private final Player color;

  /**
   * Creates a new adapted card from a pawns card.
   * @param card the card to adapt to
   * @param color the card's color
   * @throws IllegalArgumentException if card or color are null
   */
  public CardAdapt(cs3500.pawnsboard.model.Card card, Player color) {
    if (card == null || color == null) {
      throw new IllegalArgumentException("Card and color cannot be null");
    }
    this.card = card;
    this.color = color;
  }

  /**
   * Returns the card's name.
   *
   * @return the card's name
   */
  @Override
  public String getName() {
    return card.getName();
  }

  /**
   * Returns the card's cost in pawns.
   *
   * @return the card's cost
   */
  @Override
  public CardCost getCost() {
    switch (card.getCost()) {
      case 1:
        return CardCost.ONE;
      case 2:
        return CardCost.TWO;
      default:
        return CardCost.THREE;
    }
  }

  /**
   * Returns the card's value score.
   *
   * @return the card's value score
   */
  @Override
  public int getValueScore() {
    return card.getValue();
  }

  /**
   * Returns the card's 5x5 influence grid description.
   *
   * @return the card's influence grid
   */
  @Override
  public InfluenceType[][] getInfluenceGrid() {
    InfluenceType[][] grid = new InfluenceType[5][5];
    boolean[][] oldGrid = card.getInfluence();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (oldGrid[i][j]) {
          grid[i][j] = InfluenceType.INFLUENCE;
        } else if (i == 2 || j == 2) {
          grid[i][j] = InfluenceType.CENTER;
        } else {
          grid[i][j] = InfluenceType.NONE;
        }
      }
    }
    return grid;
  }

  /**
   * Returns the card's owner, meaning the player who has it.
   *
   * @return the player who owns the card - RED or BLUE
   */
  @Override
  public PlayerColor getOwner() {
    if (color == Player.RED) {
      return PlayerColor.RED;
    } else {
      return PlayerColor.BLUE;
    }
  }
}
