package cs3500.provider.view;

import cs3500.provider.model.Card;

/**
 * Interface to notify the controller when a card is clicked.
 */
public interface CardClickActions {
  /**
   * Called when a card in the hand panel is clicked.
   * @param selectedCard the card that was clicked, or null if no card is selected
   * @param index the index of the selected card in the hand, or -1 if not found
   */
  void onCardClicked(Card selectedCard, int index);
}
