package cs3500.pawnsboard.provider.model;

/**
 * Interface representing a Card in the Pawns Boards game.
 */
public interface Card {

  /**
   * Returns the card's name.
   * @return the card's name
   */
  String getName();

  /**
   * Returns the card's cost in pawns.
   * @return the card's cost
   */
  CardCost getCost();

  /**
   * Returns the card's value score.
   * @return the card's value score
   */
  int getValueScore();

  /**
   * Returns the card's 5x5 influence grid description.
   * @return the card's influence grid
   */
  InfluenceType[][] getInfluenceGrid();

  /**
   * Returns the card's owner, meaning the player who has it.
   * @return the player who owns the card - RED or BLUE
   */
  PlayerColor getOwner();
}
