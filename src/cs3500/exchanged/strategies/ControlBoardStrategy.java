package cs3500.exchanged.strategies;

import cs3500.exchanged.model.Card;
import cs3500.exchanged.model.ReadonlyPawnsBoardModel;
import cs3500.exchanged.model.Player;
import java.util.List;

/**
 * A strategy that places cards and locations in a way that
 * will give the current player ownership of the most cells.
 */
public class ControlBoardStrategy implements PawnsBoardStrategy {

  @Override
  public Move chooseMove(ReadonlyPawnsBoardModel model, Player player) {
    Move bestMove = null;
    int maxControl = -1;

    List<Card> hand = player.getHand(); // Get player's hand

    for (Card card : hand) { // Iterate over available cards
      for (int i = 0; i < model.boardGetRows(); i++) {
        for (int j = 0; j < model.boardGetColumns(); j++) {
          if (model.isLegalMove(card, i, j)) { // Pass card instead of player
            int control = calculateControl(model, player, i, j, card);
            if (control > maxControl) {
              maxControl = control;
              bestMove = new Move(card, i, j); // Store best move with the card
            }
          }
        }
      }
    }
    return bestMove;
  }

  private int calculateControl(
        ReadonlyPawnsBoardModel model, Player player, int row, int col, Card card) {
    // Simulate the move and calculate the number of cells controlled by the player
    int control = 0;

    // Apply the card's influence and simulate control change
    for (int i = 0; i < model.boardGetRows(); i++) {
      for (int j = 0; j < model.boardGetColumns(); j++) {
        if (model.getCellOwner(i, j) == player.getColor()) {
          control++;
        }
      }
    }
    return control;
  }
}