package cs3500.exchanged.strategies;

import cs3500.exchanged.model.Player;
import cs3500.exchanged.model.PlayerColor;
import cs3500.exchanged.model.ReadonlyPawnsBoardModel;

/**
 * A strategy that chooses the move that maximizes the player's advantage.
 */
public class MinimaxStrategy implements PawnsBoardStrategy {

  @Override
  public Move chooseMove(ReadonlyPawnsBoardModel model, Player player) {
    Move bestMove = null;
    int bestValue = Integer.MIN_VALUE;

    if (player.getHand().isEmpty()) {
      return null; // No cards in hand, player must pass
    }

    for (int i = 0; i < model.boardGetRows(); i++) {
      for (int j = 0; j < model.boardGetColumns(); j++) {
        if (!player.getHand().isEmpty() && model.isLegalMove(player.getHand().get(0), i, j)) {
          int moveValue = minimax(model, player, i, j, 3, false); // Depth of 3
          if (moveValue > bestValue) {
            bestValue = moveValue;
            bestMove = new Move(player.getHand().get(0), i, j);
          }
        }
      }
    }

    return bestMove;
  }

  private int minimax(ReadonlyPawnsBoardModel model,
        Player player, int row, int col, int depth, boolean isMaximizing) {
    if (depth == 0 || model.isGameOver()) {
      return model.calculateScores()[0] - model.calculateScores()[1]; // Red score - Blue score
    }

    if (isMaximizing) {
      int maxEval = Integer.MIN_VALUE;
      for (int i = 0; i < model.boardGetRows(); i++) {
        for (int j = 0; j < model.boardGetColumns(); j++) {
          if (!player.getHand().isEmpty() && model.isLegalMove(player.getHand().get(0), i, j)) {
            int eval = minimax(model, player, i, j, depth - 1, false);
            maxEval = Math.max(maxEval, eval);
          }
        }
      }
      return maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      Player opponent =
            (player.getColor() == PlayerColor.RED) ? model.getBluePlayer() : model.getRedPlayer();
      for (int i = 0; i < model.boardGetRows(); i++) {
        for (int j = 0; j < model.boardGetColumns(); j++) {
          if (!opponent.getHand().isEmpty() && model.isLegalMove(opponent.getHand().get(0), i, j)) {
            int eval = minimax(model, opponent, i, j, depth - 1, true);
            minEval = Math.min(minEval, eval);
          }
        }
      }
      return minEval;
    }
  }
}