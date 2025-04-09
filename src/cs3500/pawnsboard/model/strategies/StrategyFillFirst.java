package cs3500.pawnsboard.model.strategies;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * Strategy for a game of pawns board which makes the first available move possible, going across
 * each column of each row in that order.
 */
public class StrategyFillFirst implements PawnsBoardStrategy {

  /**
   * Returns a move to be made given this class' strategy.  A move contains the row, col, and hand
   * id of the card to be played for the given player.
   *
   * @param board  the board to base the move off of
   * @param player the player to make the move for
   * @return the move to be made
   */
  @Override
  public Move choosePlay(PawnsBoardReadOnly board, Player player) throws IllegalStateException {
    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c < board.getCols(); c++) {
        for (int i = 0; i < board.getHand(player).size(); i++) {
          if (board.isMoveValid(r, c, i, player)) {
            return new Move(r, c, i);
          }
        }
      }
    }
    throw new IllegalStateException("No available moves found");
  }
}
