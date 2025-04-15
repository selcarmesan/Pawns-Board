package cs3500.pawnsboard.model.adapter;

import cs3500.pawnsboard.model.BoardCell;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.PlayerColor;

/**
 * Cell implementation to be used in the adapter.
 */
public class CellAdapt implements cs3500.pawnsboard.provider.model.Cell {

  private final cs3500.pawnsboard.model.Cell cell;

  /**
   * Creates a new Cell adapted from the original model's cell.
   * @param cell the cell to adapt
   * @throws IllegalArgumentException if cell is null
   */
  public CellAdapt(cs3500.pawnsboard.model.Cell cell) {
    if (cell == null) {
      throw new IllegalArgumentException("cell cannot be null");
    }
    this.cell = cell;
  }

  /**
   * Returns the amount of pawns in this cell.
   *
   * @return the amount of pawns in the cell
   */
  @Override
  public int getPawns() {
    return cell.getPawns();
  }

  /**
   * Sets the amount of pawns in this cell.
   * Ensures a limit of 3 pawns at maximum.
   *
   * @param pawns the updated amount of pawns in the cell
   * @throws IllegalArgumentException if the amount of pawns is < 1 or > 3
   */
  @Override
  public void setPawns(int pawns) {
    cs3500.pawnsboard.model.Cell newCell = new BoardCell();
    newCell.playCard(cell.getCard(), cell.getOwner());
    for (int i = 0; i < pawns; i++) {
      if (i == 0) {
        newCell.addPawn(cell.getOwner());
      } else {
        cell.addPawn();
      }
    }
  }

  /**
   * Adds a given amount of pawns to the cell's current number of pawns.
   *
   * @param color the player who owns the pawns/cell
   */
  @Override
  public void addPawn(PlayerColor color) {
    if (cell.getPawns() == 0) {
      cell.addPawn(getPlayerFromColor(color));
    } else {
      cell.addPawn();
    }
  }

  /**
   * Returns the card currently in the cell.
   *
   * @return the card placed in the cell
   */
  @Override
  public cs3500.pawnsboard.provider.model.Card getCard() {
    if (cell.getCard() == null) {
      return null;
    }
    return new CardAdapt(cell.getCard(), cell.getOwner());
  }

  /**
   * Sets the card to be placed in the cell,
   * if one currently does not exist.
   *
   * @param card  the card to place
   * @param owner the new cell owner
   * @throws IllegalArgumentException if a card is already placed at the position
   */
  @Override
  public void setCard(Card card, PlayerColor owner) {
    cs3500.pawnsboard.model.Card newCard = new OurCardAdapt(card);
    cell.playCard(newCard, getPlayerFromColor(owner));
  }

  /**
   * Returns the player who owns the cell.
   *
   * @return the player owning the cell
   */
  @Override
  public PlayerColor getOwner() {
    return getPlayerColorFromPlayer(cell.getOwner());
  }

  /**
   * Sets the cell's owner to the given player.
   *
   * @param owner the player who will own the cell
   */
  @Override
  public void setOwner(PlayerColor owner) {
    cell.changeOwner(getPlayerFromColor(owner));
  }

  private PlayerColor getPlayerColorFromPlayer(Player color) {
    if (color == Player.RED) {
      return PlayerColor.RED;
    }
    return PlayerColor.BLUE;
  }

  private Player getPlayerFromColor(PlayerColor color) {
    if (color == PlayerColor.RED) {
      return Player.RED;
    }
    return Player.BLUE;
  }
}
