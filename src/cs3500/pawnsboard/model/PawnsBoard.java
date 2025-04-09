package cs3500.pawnsboard.model;

import java.util.List;

/**
 * A game of pawns board.  The game begins with an empty board of specified dimensions, with the
 * restrictions being that the board is rectangular, with a positive number of rows, and an odd
 * number of columns greater than 1.
 * Gameplay begins by dealing each of the two players a hand and allowing them to take turns
 * placing one card at a time or skipping their turn until the board is full or no player can go.
 * Score is row individual.  The one with more value based on their placed cards per row is the
 * winner of that row, and receives their total value, while the opponent receives nothing.  The
 * player with the most points from the rows they claimed is the victor at the end, or it ends in a
 * draw.
 */
public interface PawnsBoard extends PawnsBoardReadOnly {

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks containing
   * playing cards of their color, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.  The deck arguments are intended to be fetched using the PawnsCardReader class,
   * with a config file specified when calling that class' method, however it is possible to
   * manually create a deck as well.
   *
   * @param redDeck red player's deck
   * @param blueDeck blue player's deck
   * @param handSize the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if either decks are null or contain null
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException if game is already in progress
   */
  void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize, boolean randomDraw);

  /**
   * Influences the given board starting at the specified row and column.  Each card reaches at
   * most 2 in any direction from the starting space, while the specifics vary by card.
   * A card can be placed if the pawns cost for it are within the desired space, and the pawns
   * belong to the player attempting to place the card.  A new card is drawn to replace it after,
   * if any remain in the player's deck.
   *
   * @param row the starting cell's row
   * @param col the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if coordinates for row and column are out of bounds.
   *                                  if desired cell does not have enough owned pawns for the cost
   *                                  if desired cell has pawns that do not belong to the player
   * @throws IllegalStateException if game is not in progress
   */
  void placeCard(int row, int col, int handId);

  /**
   * Skips the turn of the current player, rather than placing a new card.
   *
   * @throws IllegalStateException if game is not in progress
   */
  void skipTurn();
}
