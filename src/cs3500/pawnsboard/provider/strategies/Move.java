package cs3500.pawnsboard.provider.strategies;

import cs3500.pawnsboard.provider.model.Card;

/**
 * Represents a move in the Pawns Board game.
 * A move consists of a card being placed on a specific row and column on the board.
 */
public class Move {
  private final Card card;
  private final int row;
  private final int col;

  /**
   * Constructs a move with the specified card and board position.
   *
   * @param card the card being placed
   * @param row  the row where the card is placed
   * @param col  the column where the card is placed
   */
  public Move(Card card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  /**
   * Gets the card associated with this move.
   *
   * @return the card played in this move
   */
  public Card getCard() {
    return card;
  }

  /**
   * Gets the row where the card is placed.
   *
   * @return the row index of the move
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the column where the card is placed.
   *
   * @return the column index of the move
   */
  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return "Move{" +
            "card=" + card.getName() +
            ", row=" + row +
            ", col=" + col +
            '}';
  }
}