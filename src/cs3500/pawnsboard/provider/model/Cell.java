package cs3500.pawnsboard.provider.model;

/**
 * Interface for a board cell in the game of Pawns Boards.
 */
public interface Cell {
  /**
   * Returns the amount of pawns in this cell.
   * @return the amount of pawns in the cell
   */
  int getPawns();

  /**
   * Sets the amount of pawns in this cell.
   * Ensures a limit of 3 pawns at maximum.
   * @param pawns the updated amount of pawns in the cell
   * @throws IllegalArgumentException if the amount of pawns is < 1 or > 3
   */
  void setPawns(int pawns);

  /**
   * Adds a given amount of pawns to the cell's current number of pawns.
   * @param color the player who owns the pawns/cell
   */
  void addPawn(PlayerColor color);

  /**
   * Returns the card currently in the cell.
   * @return the card placed in the cell
   */
  Card getCard();

  /**
   * Sets the card to be placed in the cell,
   * if one currently does not exist.
   * @param card the card to place
   * @throws IllegalArgumentException if a card is already placed at the position
   */
  void setCard(Card card, PlayerColor owner);

  /**
   * Returns the player who owns the cell.
   * @return the player owning the cell
   */
  PlayerColor getOwner();

  /**
   * Sets the cell's owner to the given player.
   * @param owner the player who will own the cell
   */
  void setOwner(PlayerColor owner);
}
