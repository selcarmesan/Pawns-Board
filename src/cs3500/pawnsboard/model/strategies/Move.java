package cs3500.pawnsboard.model.strategies;

/**
 * Representative class for a singular action to be taken during a turn, consisting of row and col
 * to place card, as well as the index of which card to use.
 */
public class Move {
  public final int row;
  public final int col;
  public final int handIndex;

  /**
   * Creates a new move.
   * @param row row of move
   * @param col column of move
   * @param handIndex hand index to play card from
   */
  public Move(int row, int col, int handIndex) {
    this.row = row;
    this.col = col;
    this.handIndex = handIndex;
  }

}
