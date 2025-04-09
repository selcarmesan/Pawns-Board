package cs3500.pawnsboard.model;

import java.util.List;

/**
 * The readable components for a game of pawns board. This board consists of cells, with two players
 * to keep track of information for, and their respective scores, pawns, and placed cards across the
 * board.
 */
public interface PawnsBoardReadOnly {

  /**
   * Returns the number of rows on the board.
   * @return the rows
   */
  int getRows();

  /**
   * Returns the number of columns on the board.
   * @return the columns
   */
  int getCols();

  /**
   * Returns the current total score of the specified player.  Cancels out per row if they have less
   * than the opposing player, and both are cancelled out if equal.
   *
   * @param player the player whose score is returned
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  int getTotalScore(Player player);

  /**
   * Returns the current score of the specified player in the particular row.
   *
   * @param player the player whose score is returned
   * @param row the row to score
   * @return the player's score
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if row is out of bounds
   *                                  if player is null
   */
  int getRowScore(Player player, int row);

  /**
   * Returns the current hand of cards belonging to the specified player.
   *
   * @param player the player whose hand is returned
   * @return the player's hand
   * @throws IllegalStateException if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  List<Card> getHand(Player player);

  /**
   * Returns whether a move for the current player is valid, a move being the placement of
   * a card onto a location on the board.  A move is valid if the card exists, the location is on
   * the board, and the location has enough owned pawns by the player to cover the cost of the card.
   * Cards can also only be played in cells that do not have a card played already.
   *
   * @param row the starting cell's row
   * @param col the starting cell's column
   * @param handId the index of the card in the player's hand
   * @param player the player to check validity for
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if player is null
   *                                  if coordinates for row and column are out of bounds.
   * @throws IllegalStateException if game is not in progress
   */
  boolean isMoveValid(int row, int col, int handId, Player player);

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  Player getCurrentTurn();

  /**
   * Tells whether game is still in progress.
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  boolean isGameOver();


  /**
   * Returns the winner, the player with the greater score, or null if tied.
   *
   * @return the winning player
   * @throws IllegalStateException if game is not in progress
   *                               if game is not over
   */
  Player getWinner();

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  Cell[][] getBoard();

  /**
   * Returns a copy of the specified cell.
   *
   * @param row the row
   * @param col the column
   * @return the cell in question
   * @throws IllegalArgumentException if row and column pair marks an invalid space
   * @throws IllegalStateException if game is not in progress
   */
  Cell getCellAt(int row, int col);
}
