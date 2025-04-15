package cs3500.pawnsboard.model.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.pawnsboard.provider.model.Board;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.Cell;
import cs3500.pawnsboard.provider.model.Player;
import cs3500.pawnsboard.provider.model.PlayerColor;

/**
 * Player implementation to be used in the adapter.
 */
public class PlayerAdapt implements Player {

  private final cs3500.pawnsboard.model.Player color;
  private final List<cs3500.pawnsboard.model.Card> deck;
  private final List<cs3500.pawnsboard.model.Card> hand;

  public PlayerAdapt(cs3500.pawnsboard.model.Player color, List<cs3500.pawnsboard.model.Card> deck,
                     List<cs3500.pawnsboard.model.Card> hand) {
    if (color == null || deck == null || hand == null) {
      throw new IllegalArgumentException("color and deck and hand cannot be null");
    }
    if (deck.stream().anyMatch(Objects::isNull) || hand.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("deck and hand cannot contain null");
    }
    this.color = color;
    this.deck = deck;
    this.hand = hand;
  }

  /**
   * Returns the player's color.
   * Enum class defines PlayerColor as either RED or BLUE.
   *
   * @return the PlayerColor associated with the player
   */
  @Override
  public PlayerColor getColor() {
    if (color == cs3500.pawnsboard.model.Player.RED) {
      return PlayerColor.RED;
    } else {
      return PlayerColor.BLUE;
    }
  }

  /**
   * Returns the player's hand.
   *
   * @return a list of cards present in the player's hand
   */
  @Override
  public List<Card> getHand() {
    List<Card> newHand = new ArrayList<>();
    for (cs3500.pawnsboard.model.Card card : this.hand) {
      newHand.add(new CardAdapt(card, this.color));
    }
    return newHand;
  }

  /**
   * Returns the player's individual deck.
   *
   * @return a list of cards present in the player's deck
   */
  @Override
  public List<Card> getDeck() {
    List<Card> newDeck = new ArrayList<>();
    for (cs3500.pawnsboard.model.Card card : this.deck) {
      newDeck.add(new CardAdapt(card, this.color));
    }
    return newDeck;
  }

  /**
   * Draws a card from the player's deck.
   */
  @Override
  public void drawCard() {
    hand.add(deck.remove(0));
  }

  /**
   * Checks if the player can place a card based on pawn requirements.
   *
   * @param card the card to be played
   * @param cell the cell (position) on the board
   * @return true if the cell has enough of the player's own pawns
   * to cover the cost of the card, false if otherwise
   */
  @Override
  public boolean canPlayCard(Card card, Cell cell) {
    return cell.getOwner() == getColor() && cell.getPawns() >= card.getCost().getNumCost();
  }

  /**
   * Plays a card from the player's hand, if valid.
   *
   * @param card  the card to be played
   * @param cell  the cell (position) on the board
   * @param board the board used in the game
   * @param row   the x-coordinate of the cell's position on the board
   * @param col   the y-coordinate of the cell's position on the board
   * @throws IllegalArgumentException if the player cannot play the given card
   */
  @Override
  public void playCard(Card card, Cell cell, Board board, int row, int col) {
    if (!canPlayCard(card, cell)) {
      throw new IllegalArgumentException("card cannot be played");
    }
    cell.setCard(card, getColor());
    board.applyCardInfluence(this, row, col, card);
  }
}
