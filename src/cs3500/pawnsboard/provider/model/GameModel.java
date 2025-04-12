package cs3500.pawnsboard.provider.model;

import cs3500.pawnsboard.provider.players.ModelStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game state, including the board, players, and turn-taking logic.
 *
 * <p>Class Invariants:
 * 1. Each player's deck must contain at least enough cards to fill the board
 * and no more than 2 copies of any card.
 * 2. Players can only place cards on cells if they have enough pawns to cover the cost.
 * 3. The game must always start with the Red player.
 */
public class GameModel implements PawnsBoardModel {
  private final Board board;
  private final Player redPlayer;
  private final Player bluePlayer;
  private Player currentPlayer;
  private int[][] playersRowScores;
  private final int rows;
  private final int columns;
  private int consecutivePasses;
  private final List<ModelStatusListener> modelStatusListeners;

  /**
   * Constructs the game.
   * @param rows number of rows in the board
   * @param columns number of columns in the board
   * @param redDeck red player's deck of cards
   * @param blueDeck blue player's deck of cards
   */
  public GameModel(int rows, int columns, List<Card> redDeck, List<Card> blueDeck) {
    if (redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("Decks cannot be null.");
    }
    if (redDeck.size() < (rows * columns / 2) || blueDeck.size() < (rows * columns / 2)) {
      throw new IllegalArgumentException(
            "Decks must have enough cards to cover half of the board.");
    }
    this.rows = rows;
    this.columns = columns;
    this.redPlayer = new Player(PlayerColor.RED, redDeck);
    this.bluePlayer = new Player(PlayerColor.BLUE, blueDeck);
    this.board = new Board(rows, columns, redPlayer, bluePlayer);
    this.currentPlayer = redPlayer; // Red always goes first
    this.modelStatusListeners = new ArrayList<>();
    playersRowScores = new int[2][rows];
  }

  @Override
  public void startGame() {
    consecutivePasses = 0;
    board.setGameOver(false);

    // Notify listeners about the game start
    for (ModelStatusListener listener : modelStatusListeners) {
      listener.onGameStart();
    }

    // Start with the Red player's turn
    notifyTurnChanged();
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public Player getRedPlayer() {
    return redPlayer;
  }

  @Override
  public Player getBluePlayer() {
    return bluePlayer;
  }

  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public int getNumberOfPawns(int row, int col) {
    Cell cell = board.getGrid()[row][col];
    return cell.getPawns();
  }

  @Override
  public int[][] getFullPawnCounts() {
    int[][] pawnCounts = new int[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        pawnCounts[row][col] = board.getGrid()[row][col].getPawns();
      }
    }
    return pawnCounts;
  }

  private void switchTurn() {
    currentPlayer = (currentPlayer == redPlayer) ? bluePlayer : redPlayer;
  }

  @Override
  public void playTurn(Card card, int row, int col) {
    if (!isLegalMove(card, row, col)) {
      throw new IllegalArgumentException("You can't play this card.");
    }
    Cell cell = board.getGrid()[row][col];
    currentPlayer.playCard(card, cell, board, row, col);

    // Update the correct row score
    int updatedScore = board.getRowScore(row, currentPlayer.getColor());
    int playerIndex = (currentPlayer.getColor() == PlayerColor.RED) ? 0 : 1;
    playersRowScores[playerIndex][row] = updatedScore;

    this.currentPlayer.drawCard();
    this.switchTurn();
    notifyTurnChanged();
  }

  @Override
  public Board copyBoard() {
    return new Board(this.board);
  }

  @Override
  public int boardGetRows() {
    return board.getRows();
  }

  @Override
  public int boardGetColumns() {
    return board.getColumns();
  }

  @Override
  public Cell getCell(int row, int col) {
    validatePosition(row, col);
    Cell originalCell = board.getGrid()[row][col];
    return new Cell(originalCell);
  }

  @Override
  public List<Card> getPlayerHand(PlayerColor color) {
    return (color == PlayerColor.RED) ? redPlayer.getHand() : bluePlayer.getHand();
  }

  @Override
  public PlayerColor getCellOwner(int row, int col) {
    validatePosition(row, col);
    return board.getGrid()[row][col].getOwner();
  }

  @Override
  public PlayerColor[][] getOwnerships() {
    PlayerColor[][] ownerships = new PlayerColor[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        ownerships[row][col] = board.getGrid()[row][col].getOwner();
      }
    }
    return ownerships;
  }

  @Override
  public boolean isLegalMove(Card card, int row, int col) {
    validatePosition(row, col);
    Cell cell = board.getGrid()[row][col];
    return currentPlayer.canPlayCard(card, cell);
  }

  @Override
  public int getRowScore(int row, PlayerColor color) {
    int playerIndex = (color == PlayerColor.RED) ? 0 : 1;
    return playersRowScores[playerIndex][row];
  }

  @Override
  public int[] calculateScores() {
    return board.calculateTotalScores();
  }

  @Override
  public int[][] getPlayerRowScores() {
    return playersRowScores;
  }

  @Override
  public void passTurn() {
    consecutivePasses++;
    if (consecutivePasses >= 2) {
      board.setGameOver(true);
      notifyGameOver();
    }
    switchTurn();
    notifyTurnChanged();
  }

  @Override
  public boolean isGameOver() {
    if (consecutivePasses >= 2) {
      board.setGameOver(true);
    }
    return board.isGameOver();
  }

  @Override
  public PlayerColor getWinner() {
    int[] scores = calculateScores();
    if (scores[0] > scores[1]) {
      return PlayerColor.RED; // Red wins
    } else if (scores[1] > scores[0]) {
      return PlayerColor.BLUE; // Blue wins
    } else {
      return null; // Tie
    }
  }

  @Override
  public int getWinnerScore() {
    int[] scores = calculateScores();
    PlayerColor winner = getWinner();
    if (winner == null) {
      return 0; // Tie
    }
    return (winner == PlayerColor.RED) ? scores[0] : scores[1];
  }

  private void validatePosition(int row, int col) {
    if (row < 0 || row >= board.getRows() || col < 0 || col >= board.getColumns()) {
      throw new IllegalArgumentException("Invalid board position.");
    }
  }

  @Override
  public void addModelStatusListener(ModelStatusListener listener) {
    modelStatusListeners.add(listener);
  }

  private void notifyTurnChanged() {
    for (ModelStatusListener listener : modelStatusListeners) {
      listener.onTurnChange(currentPlayer.getColor());
    }
  }

  private void notifyGameOver() {
    PlayerColor winner = getWinner();
    int score = getWinnerScore();
    for (ModelStatusListener listener : modelStatusListeners) {
      listener.onGameEnd(winner, score);
    }
  }
}