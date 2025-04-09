package cs3500.pawnsboard.model.mocks;

import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.Cell;
import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.PawnsCard;
import cs3500.pawnsboard.model.Player;

/**
 * Mock board which is meant to test the strategy for maximizing row-score.
 */
public class MockBoardRowScore implements PawnsBoardReadOnly {

  private final StringBuilder log;

  public MockBoardRowScore(StringBuilder sb) {
    this.log = sb;
  }

  /**
   * Returns the number of rows on the board.
   *
   * @return the rows
   */
  @Override
  public int getRows() {
    return 3;
  }

  /**
   * Returns the number of columns on the board.
   *
   * @return the columns
   */
  @Override
  public int getCols() {
    return 3;
  }

  /**
   * Returns the current total score of the specified player.  Cancels out per row if they have less
   * than the opposing player, and both are cancelled out if equal.
   *
   * @param player the player whose score is returned
   * @return the player's score
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public int getTotalScore(Player player) {
    return 0;
  }

  /**
   * Returns the current score of the specified player in the particular row.
   *
   * @param player the player whose score is returned
   * @param row    the row to score
   * @return the player's score
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if row is out of bounds
   *                                  if player is null
   */
  @Override
  public int getRowScore(Player player, int row) {
    if (player == Player.RED) {
      if (row == 2) {
        return -1;
      }
      return -2;
    }
    return 0;
  }

  /**
   * Returns the current hand of cards belonging to the specified player.
   *
   * @param player the player whose hand is returned
   * @return the player's hand
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public List<Card> getHand(Player player) {
    Card c1 = new PawnsCard("mock1", 1, 1, new boolean[5][5]);
    Card c2 = new PawnsCard("mock2", 1, 2, new boolean[5][5]);
    return List.of(c1, c2, c1);
  }

  /**
   * Returns whether a move for the current player is valid, a move being the placement of
   * a card onto a location on the board.  A move is valid if the card exists, the location is on
   * the board, and the location has enough owned pawns by the player to cover the cost of the card.
   * Cards can also only be played in cells that do not have a card played already.
   *
   * @param row    the starting cell's row
   * @param col    the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if coordinates for row and column are out of bounds.
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public boolean isMoveValid(int row, int col, int handId, Player player) {
    log.append("Checked move: " + row + ", " + col + ", " + handId + "\n");
    return true;
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getCurrentTurn() {
    return null;
  }

  /**
   * Tells whether game is still in progress.
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public boolean isGameOver() {
    return false;
  }

  /**
   * Returns the winner, the player with the greater score, or null if tied.
   *
   * @return the winning player
   * @throws IllegalStateException if game is not in progress
   *                               if game is not over
   */
  @Override
  public Player getWinner() {
    return null;
  }

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Cell[][] getBoard() {
    return new Cell[0][];
  }

  /**
   * Returns a copy of the specified cell.
   *
   * @param row the row
   * @param col the column
   * @return the cell in question
   * @throws IllegalArgumentException if row and column pair marks an invalid space
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public Cell getCellAt(int row, int col) {
    return null;
  }
}
