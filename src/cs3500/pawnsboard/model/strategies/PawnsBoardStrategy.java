package cs3500.pawnsboard.model.strategies;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * A playable strategy for a game of pawns board, which decides a move to make (or pass) given a
 * particular model and player.
 */
public interface PawnsBoardStrategy {

  /**
   * Returns a move to be made given this class' strategy.  A move contains the row, col, and hand
   * id of the card to be played for the given player.
   * @param board the board to base the move off of
   * @param player the player to make the move for
   * @return the move to be made
   */
  Move choosePlay(PawnsBoardReadOnly board, Player player) throws IllegalStateException;
}
