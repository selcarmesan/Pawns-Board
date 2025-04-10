package cs3500.provider.strategies;

import cs3500.provider.model.Card;
import cs3500.provider.model.Player;
import cs3500.provider.model.ReadonlyPawnsBoardModel;

/**
 * A strategy that selects a move to maximize the player's row score.
 */
public class MaximizeRowScoreStrategy implements PawnsBoardStrategy {

  @Override
  public Move chooseMove(ReadonlyPawnsBoardModel model, Player player) {
    Move bestMove = null;
    int bestScore = -1;

    for (Card card : player.getHand()) { // Iterate over available cards
      for (int i = 0; i < model.boardGetRows(); i++) {
        for (int j = 0; j < model.boardGetColumns(); j++) {
          if (model.isLegalMove(card, i, j)) { // Pass card instead of player
            int currentScore = model.getRowScore(i, player.getColor());
            if (currentScore > bestScore) {
              bestScore = currentScore;
              bestMove = new Move(card, i, j);
            }
          }
        }
      }
    }

    return bestMove;
  }
}