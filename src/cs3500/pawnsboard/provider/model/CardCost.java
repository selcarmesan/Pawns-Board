package cs3500.model;

/**
 * Enum class defining the possible card costs in the game.
 * Options are: ONE (1), TWO (2), THREE (3).
 */
public enum CardCost {
  ONE(1),
  TWO(2),
  THREE(3);

  private final int cost;

  CardCost(int cost) {
    this.cost = cost;
  }

  /**
   * Returns the numerical cost associated with each CardCost type.
   * @return the numerical cost of the card
   */
  public int getNumCost() {
    return this.cost;
  }
}
