package cs3500.pawnsboard.provider.view;

import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.PlayerColor;
import cs3500.pawnsboard.provider.model.ReadonlyPawnsBoardModel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for the Hand visual representation.
 */
public class HandPanel extends JPanel {
  private List<Card> hand;
  private Card selectedCard;
  private CardClickActions cardClickListener;
  private CardPanel selectedCardPanel;

  /**
   * Constructs the hand panel.
   * @param model the read-only game model
   */
  public HandPanel(ReadonlyPawnsBoardModel model) {
    this.hand = model.getPlayerHand(model.getCurrentPlayer().getColor());
    setPreferredSize(new Dimension(600, 350));
    setHandBackground(model);
    MatteBorder bottomBorder = BorderFactory.createMatteBorder(10, 0, 0, 0, Color.BLACK);
    this.setBorder(bottomBorder);
    updateHand(hand);
    selectedCardPanel = null;
    selectedCard = null;
  }

  private void setHandBackground(ReadonlyPawnsBoardModel model) {
    Color customBlue = new Color(50, 140, 240);
    Color customRed = new Color(255, 100, 120);

    if (model.getCurrentPlayer().getColor() == PlayerColor.BLUE) {
      setBackground(customBlue);
    } else if (model.getCurrentPlayer().getColor() == PlayerColor.RED) {
      setBackground(customRed);
    }
  }

  /**
   * Sets a listener to be notified when a card is clicked.
   * @param listener the CardClickListener that will handle card click events
   */
  public void setCardClickListener(CardClickActions listener) {
    this.cardClickListener = listener;
  }

  /**
   * Updates the hand display when the turn changes.
   * @param newHand The new player's hand
   */
  public void updateHand(List<Card> newHand) {
    Color customOutline = new Color(253,254,2);

    this.hand = newHand;
    removeAll();
    setLayout(new GridLayout(1, hand.size(), 5, 0));

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

      int cardIndex = i;

      cardPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (selectedCard == card) {
            selectedCard = null;
            selectedCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            selectedCardPanel = null;
          } else {
            if (selectedCardPanel != null) {
              selectedCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }

            // Select the new card
            selectedCard = card;
            selectedCardPanel = cardPanel;
            cardPanel.setBorder(BorderFactory.createLineBorder(customOutline, 5));

            if (cardClickListener != null) {
              cardClickListener.onCardClicked(selectedCard, cardIndex);
            }
          }
          repaint();
        }
      });

      add(cardPanel);
    }

    revalidate();
    repaint();
  }

  /**
   * Updates the hand panel's background depending on player turns.
   * @param color the current player's color
   */
  public void updateBackground(PlayerColor color) {
    if (color == PlayerColor.BLUE) {
      setBackground(new Color(50, 140, 240));
    } else if (color == PlayerColor.RED) {
      setBackground(new Color(255, 100, 120));
    }
  }

  /**
   * Gets which card the player selected.
   * @return the selected card
   */
  public Card getSelectedCard() {
    return selectedCard;
  }

  /**
   * Gets the player's selected card index within their hand.
   * Indexes are 0-based from left to right.
   * @return the index of the selected card
   */
  public int getSelectedCardIndex() {
    return hand.indexOf(selectedCard);
  }

  /**
   * Removes a card from the visual hand.
   * @param card the card to be removed
   */
  public void removeCard(Card card) {
    hand.remove(card); // Remove from the list
    updateHand(hand); // Refresh hand display
    revalidate();
    repaint();
  }

  /**
   * Deselects a card.
   */
  public void clearSelection() {
    if (selectedCardPanel != null) {
      selectedCardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }
    selectedCard = null;
    selectedCardPanel = null;
    updateHand(hand);
  }

  /**
   * Repaints the view.
   */
  public void refresh() {
    repaint();
  }
}
