package cs3500.pawnsboard.model.strategies;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * Strategy for a game of pawns board that attempts to choose the first available option which
 * wins over the scoring of a row, i.e. overtaking the opponent's current total value in that row
 * with the player's own current total value for the row.
 */
public class StrategyMaximizeRowScore implements PawnsBoardStrategy {

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
    Player other;
    if (player == Player.RED) {
      other = Player.BLUE;
    } else {
      other = Player.RED;
    }
    for (int r = 0; r < board.getRows(); r++) {
      if (board.getRowScore(player, r) <= board.getRowScore(other, r)) {
        for (int c = 0; c < board.getCols(); c++) {
          for (int i = 0; i < board.getHand(player).size(); i++) {
            if (board.isMoveValid(r, c, i, player)) {
              int value = board.getHand(player).get(i).getValue();
              if (board.getRowScore(player, r) + value > board.getRowScore(other, r)) {
                return new Move(r, c, i);
              }
            }
          }
        }
      }
    }
    throw new IllegalStateException("No available moves found");
  }
}
