package cs3500.exchanged.model;

import cs3500.exchanged.players.ModelStatusListener;

/**
 * Represents the code functionality of the game model.
 * Made as an interface to allow for future game versions.
 */
public interface PawnsBoardModel extends ReadonlyPawnsBoardModel {

  /**
   * Allows the current player to make a move by placing a card on the board.
   * @param card the card to be placed
   * @param row the x-coordinate of the cell's position on the board
   * @param col the y-coordinate of the cell's position on the board
   */
  void playTurn(Card card, int row, int col);

  /**
   * Switch turns between the players.
   * Allows players to pass their turn.
   */
  void passTurn();

  /**
   * Creates a duplicated version o the board at the time.
   * @return the game board's copy
   */
  Board copyBoard();

  /**
   * Adds a listener for model status changes.
   * @param listener the listener to add
   */
  void addModelStatusListener(ModelStatusListener listener);

  /**
   * Strats the model of Pawns Boards with the default state.
   */
  void startGame();
}
