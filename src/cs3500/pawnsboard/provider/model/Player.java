package cs3500.pawnsboard.provider.model;

import java.util.List;

/**
 * Interface for player actions in the game of Pawns Boards.
 */
public interface Player {

  /**
   * Returns the player's color.
   * Enum class defines PlayerColor as either RED or BLUE.
   * @return the PlayerColor associated with the player
   */
  PlayerColor getColor();

  /**
   * Returns the player's hand.
   * @return a list of cards present in the player's hand
   */
  List<Card> getHand();

  /**
   * Returns the player's individual deck.
   * @return a list of cards present in the player's deck
   */
  List<Card> getDeck();

  /**
   * Draws a card from the player's deck.
   */
  void drawCard();

  /**
   * Checks if the player can place a card based on pawn requirements.
   * @param card the card to be played
   * @param cell the cell (position) on the board
   * @return true if the cell has enough of the player's own pawns
   *         to cover the cost of the card, false if otherwise
   */
  boolean canPlayCard(Card card, Cell cell);

  /**
   * Plays a card from the player's hand, if valid.
   * @param card the card to be played
   * @param cell the cell (position) on the board
   * @param board the board used in the game
   * @param row the x-coordinate of the cell's position on the board
   * @param col the y-coordinate of the cell's position on the board
   * @throws IllegalArgumentException if the player cannot play the given card
   */
  void playCard(Card card, Cell cell, Board board, int row, int col);
}
