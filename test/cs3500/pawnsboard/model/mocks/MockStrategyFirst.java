package cs3500.pawnsboard.model.mocks;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.strategies.Move;
import cs3500.pawnsboard.model.strategies.PawnsBoardStrategy;

/**
 * Mock strategy for testing.
 */
public class MockStrategyFirst implements PawnsBoardStrategy {

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
    return new Move(0, 0, 0);
  }
}
