package cs3500.pawnsboard.model;

/**
 * Interface for a playable card during a game of pawns board, with a unique name, cost, point
 * value, and influence pattern.  Used to make moves during a game.
 */
public interface Card {

  /**
   * Returns the unique name of the card.
   *
   * @return the card name
   */
  String getName();

  /**
   * Returns the cost in pawns to play this card on the board.
   *
   * @return the cost
   */
  int getCost();

  /**
   * Returns the value of this card.
   *
   * @return the value
   */
  int getValue();

  /**
   * Returns the influence grid for this card.
   */
  boolean[][] getInfluence();
}
