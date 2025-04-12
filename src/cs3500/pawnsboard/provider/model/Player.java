package cs3500.pawnsboard.provider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a player in the game. Each player has a color (Red or Blue),
 * a deck of cards, and a hand to play from.
 */
public class Player {
  private final PlayerColor color; // Red or Blue
  private final List<Card> hand; // Cards in the player's hand
  private final List<Card> deck; // Cards in the player's deck

  /**
   * Creates an object representing a Player of the game.
   * @param color the player's color -- Red or Blue
   * @param deck the player's list of cards (deck)
   */
  public Player(PlayerColor color, List<Card> deck) {
    this.color = color;
    validateDeck(deck);
    this.deck = deck;
    this.hand = new ArrayList<>();

    dealInitialHand();
  }

  // Deals the initial hand (at most 1/3 of the deck size)
  private void dealInitialHand() {
    int maxHandSize = Math.min(5, deck.size());
    for (int i = 0; i < maxHandSize && !deck.isEmpty(); i++) {
      drawCard();
    }
  }

  // Ensures the deck has enough cards and no more than 2 copies of a single card
  private void validateDeck(List<Card> deck) {
    Map<String, Integer> cardCounts = new HashMap<>();
    for (Card card : deck) {
      cardCounts.put(card.getName(), cardCounts.getOrDefault(card.getName(), 0) + 1);
      if (cardCounts.get(card.getName()) > 2) {
        throw new IllegalArgumentException("A deck cannot contain more than 2 copies of any card.");
      }
    }
    if (deck.size() < 9) { // Minimum board size: 3x3, needing at least 9 cards
      throw new IllegalArgumentException("Deck must have enough cards to fill the board.");
    }
  }

  /**
   * Returns the player's color.
   * Enum class defines PlayerColor as either RED or BLUE.
   * @return the PlayerColor associated with the player
   */
  public PlayerColor getColor() {
    return color;
  }

  /**
   * Returns the player's hand.
   * @return a list of cards present in the player's hand
   */
  public List<Card> getHand() {
    return hand;
  }

  /**
   * Returns the player's individual deck.
   * @return a list of cards present in the player's deck
   */
  public List<Card> getDeck() {
    return deck;
  }

  /**
   * Draws a card from the player's deck.
   */
  public void drawCard() {
    if (!deck.isEmpty()) {
      hand.add(deck.remove(0));
    }
  }

  /**
   * Checks if the player can place a card based on pawn requirements.
   * @param card the card to be played
   * @param cell the cell (position) on the board
   * @return true if the cell has enough of the player's own pawns
   *         to cover the cost of the card, false if otherwise
   */
  public boolean canPlayCard(Card card, Cell cell) {
    return cell.getOwner() == this.color && cell.getPawns() >= card.getCost().getNumCost();
  }

  /**
   * Plays a card from the player's hand, if valid.
   * @param card the card to be played
   * @param cell the cell (position) on the board
   * @param board the board used in the game
   * @param row the x-coordinate of the cell's position on the board
   * @param col the y-coordinate of the cell's position on the board
   * @throws IllegalArgumentException if the player cannot play the given card
   */
  public void playCard(Card card, Cell cell, Board board, int row, int col) {
    if (!canPlayCard(card, cell)) {
      throw new IllegalArgumentException("You can't play this card.");
    }

    hand.remove(card);
    cell.setCard(card, card.getOwner());
    cell.setPawns(0);
    cell.setOwner(this.color);
    board.applyCardInfluence(this, row, col, card);
  }
}