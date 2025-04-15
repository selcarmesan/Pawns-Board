package cs3500.pawnsboard.model.mocks;

import java.io.File;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.Cell;
import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.Player;

/**
 * Pawns board transcript for the sake of generating a transcript.
 */
public class MockBoardTranscript implements PawnsBoard {

  PawnsBoard board;
  StringBuilder log;

  /**
   * Creates a new mock board transcript.
   */
  public MockBoardTranscript(StringBuilder sb) {
    board = new PawnsBoardSimple(3, 5);
    log = sb;
  }

  /**
   * Returns the number of rows on the board.
   *
   * @return the rows
   */
  @Override
  public int getRows() {
    return board.getRows();
  }

  /**
   * Returns the number of columns on the board.
   *
   * @return the columns
   */
  @Override
  public int getCols() {
    return board.getCols();
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
    return board.getTotalScore(player);
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
    log.append("Got score for player " + player + " at row " + row + " of "
            + board.getRowScore(player, row) + "\n");
    return board.getRowScore(player, row);
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
    log.append("Got hand " + player + "\n");
    return board.getHand(player);
  }

  /**
   * Returns the remaining deck of cards belonging to the specific player.
   *
   * @param player the player whose deck is returned
   * @return the player's deck
   * @throws IllegalStateException    if game is not in progress
   * @throws IllegalArgumentException if player is null
   */
  @Override
  public List<Card> getRemainingDeck(Player player) {
    return null;
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
   * @param player the player to check validity for
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if player is null
   *                                  if coordinates for row and column are out of bounds.
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public boolean isMoveValid(int row, int col, int handId, Player player) {
    log.append("Checked move: " + row + ", " + col + ", " + handId + "\n");
    return board.isMoveValid(row, col, handId, player);
  }

  /**
   * Returns the player whose turn it is.
   *
   * @return the turn
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Player getCurrentTurn() {
    return board.getCurrentTurn();
  }

  /**
   * Tells whether game is still in progress.
   *
   * @return if the game state is over
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public boolean isGameOver() {
    return board.isGameOver();
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
    return board.getWinner();
  }

  /**
   * Returns a copy of the current playing board.
   *
   * @return the board
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public Cell[][] getBoard() {
    return board.getBoard();
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
    log.append("Checked cell: " + row + ", " + col + "\n");
    return board.getCellAt(row, col);
  }

  /**
   * Initializes a new game of pawns board.  Both players are supplied their own decks containing
   * playing cards of their color, and dealt an equal amount of cards into their starting hand.
   * A new board is initialized which is empty, except for a singular red pawn in each cell in the
   * leftmost column, and a singular blue pawn in each cell in the rightmost column.  Red has the
   * first turn.  The deck arguments are intended to be fetched using the PawnsCardReader class,
   * with a config file specified when calling that class' method, however it is possible to
   * manually create a deck as well.
   *
   * @param redDeck    red player's deck
   * @param blueDeck   blue player's deck
   * @param handSize   the starting hand size for both players
   * @param randomDraw whether each new card is drawn randomly or from the front
   * @throws IllegalArgumentException if either decks are null or contain null
   *                                  if the hand size is less than 1
   *                                  if either deck contains fewer cards than the number of cells
   *                                  if there are more than 2 copies of the same card in one deck
   *                                  if hand size is greater than a third of either deck size
   * @throws IllegalStateException    if game is already in progress
   */
  @Override
  public void startGame(List<Card> redDeck, List<Card> blueDeck, int handSize, boolean randomDraw) {
    File red = new File("docs" + File.separator + "deckRed.config");
    File blue = new File("docs" + File.separator + "deckBlue.config");
    List<Card> redCard = PawnsCardReader.readCards(Player.RED, red);
    List<Card> blueCard = PawnsCardReader.readCards(Player.BLUE, blue);
    board.startGame(redCard, blueCard, 5, false);
  }

  /**
   * Influences the given board starting at the specified row and column.  Each card reaches at
   * most 2 in any direction from the starting space, while the specifics vary by card.
   * A card can be placed if the pawns cost for it are within the desired space, and the pawns
   * belong to the player attempting to place the card.  A new card is drawn to replace it after,
   * if any remain in the player's deck.
   *
   * @param row    the starting cell's row
   * @param col    the starting cell's column
   * @param handId the index of the card in the player's hand
   * @throws IllegalArgumentException if hand ID is invalid
   *                                  if coordinates for row and column are out of bounds.
   *                                  if desired cell does not have enough owned pawns for the cost
   *                                  if desired cell has pawns that do not belong to the player
   * @throws IllegalStateException    if game is not in progress
   */
  @Override
  public void placeCard(int row, int col, int handId) {
    board.placeCard(row, col, handId);
  }

  /**
   * Skips the turn of the current player, rather than placing a new card.
   *
   * @throws IllegalStateException if game is not in progress
   */
  @Override
  public void skipTurn() {
    board.skipTurn();
  }
}
