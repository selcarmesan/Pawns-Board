package cs3500.exchanged.strategies;

import cs3500.exchanged.model.Player;
import cs3500.exchanged.model.ReadonlyPawnsBoardModel;

/**
 * Represents a strategy for choosing moves in the Pawns Board game.
 * Implementations of this interface define different ways to determine
 * the best possible move for a player.
 */
public interface PawnsBoardStrategy {

  /**
   * Chooses the best move for the given player based on the current board state.
   *
   * @param model  the read-only representation of the game board
   * @param player the player for whom the move is being chosen
   * @return the chosen move, or null if no valid move is available
   */
  Move chooseMove(ReadonlyPawnsBoardModel model, Player player);
}