package cs3500.exchanged.strategies;

import cs3500.exchanged.model.Card;
import cs3500.exchanged.model.Player;
import cs3500.exchanged.model.ReadonlyPawnsBoardModel;


/**
 * A strategy that places the first available card in the first open spot on the board.
 */
public class FillFirstStrategy implements PawnsBoardStrategy {

  @Override
  public Move chooseMove(ReadonlyPawnsBoardModel model, Player player) {
    if (player.getHand().isEmpty()) {
      return null; // No cards in hand, player must pass
    }

    for (Card card : player.getHand()) { // Iterate over available cards
      for (int i = 0; i < model.boardGetRows(); i++) {
        for (int j = 0; j < model.boardGetColumns(); j++) {
          if (model.isLegalMove(card, i, j)) {
            return new Move(card, i, j);
          }
        }
      }
    }
    return null; // No valid move, player must pass
  }
}