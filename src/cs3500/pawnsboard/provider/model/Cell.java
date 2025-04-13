package cs3500.pawnsboard.provider.model;

/**
 * Represents a cell on the board. A cell can contain pawns or a card
 * and is owned by one of the players.
 */
public class Cell {
  private int pawns;
  private Card card;
  private PlayerColor owner; // Owner of the cell (Red or Blue)

  /**
   * Constructor of cell class.
   * @param owner the player owner of the cell
   */
  public Cell(PlayerColor owner) {
    this.pawns = 1;
    this.card = null;
    this.owner = owner;
  }

  /**
   * Copy of original Cell class constructors.
   * Allow for deep copies to prevent mutation.
   * @param other the original cell to be copied.
   */
  public Cell(Cell other) {
    this.pawns = other.pawns;
    this.card = other.card != null ? new Card(other.card) : null;
    this.owner = other.owner;
  }

  /**
   * Returns the amount of pawns in this cell.
   * @return the amount of pawns in the cell
   */
  public int getPawns() {
    return pawns;
  }

  /**
   * Sets the amount of pawns in this cell.
   * Ensures a limit of 3 pawns at maximum.
   * @param pawns the updated amount of pawns in the cell
   * @throws IllegalArgumentException if the amount of pawns is < 1 or > 3
   */
  public void setPawns(int pawns) {
    if (pawns < 0 || pawns > 3) {
      throw new IllegalArgumentException("Pawns must be between 0 and 3.");
    }
    this.pawns = pawns;
  }

  /**
   * Adds a given amount of pawns to the cell's current number of pawns.
   * @param color the player who owns the pawns/cell
   */
  public void addPawn(PlayerColor color) {
    this.pawns = Math.min(3, this.pawns + 1);
    this.owner = color; // Ownership updates as soon as pawn is present!
  }

  /**
   * Returns the card currently in the cell.
   * @return the card placed in the cell
   */
  public Card getCard() {
    return card;
  }

  /**
   * Sets the card to be placed in the cell,
   * if one currently does not exist.
   * @param card the card to place
   * @throws IllegalArgumentException if a card is already placed at the position
   */
  public void setCard(Card card, PlayerColor owner) {
    if (this.card != null) {
      throw new IllegalStateException("A card is already placed in this cell.");
    }
    this.card = card;
    this.owner = owner;
  }

  /**
   * Returns the player who owns the cell.
   * @return the player owning the cell
   */
  public PlayerColor getOwner() {
    return owner;
  }

  /**
   * Sets the cell's owner to the given player.
   * @param owner the player who will own the cell
   */
  public void setOwner(PlayerColor owner) {
    this.owner = owner;
  }
}