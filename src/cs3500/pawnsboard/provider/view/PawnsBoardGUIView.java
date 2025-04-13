package cs3500.view;

/**
 * Interface to design the GUI view of Pawns Board.
 */
public interface PawnsBoardGUIView {

  /**
   * Redraws the game board with its pieces: cards, pawns, player hands etc.
   */
  void refresh();

  /**
   * Sets the view to visible.
   */
  void makeVisible();

  /**
   * Allows view to alternate between players.
   */
  void switchTurns();

  /**
   * Updates the scores for each row of both players.
   * @param row the row to be updated
   * @param redScore the new row score for red
   * @param blueScore the new row score for blue
   */
  void updateRowScores(int row, int redScore, int blueScore);

  /**
   * Gets the BoardPanel.
   * @return the board panel
   */
  BoardPanel getBoardPanel();

  /**
   * Gets the HandPanel.
   * @return the hand panel
   */
  HandPanel getHandPanel();

  /**
   * Generates a pop-up message to the player's screen when an error occurs.
   * @param message the error message
   * @param title the window title
   */
  void printErrorMessage(String message, String title);

  /**
   * Generates a pop-up message to the player's screen with game over information.
   * Winning player + score or "Tie game"
   */
  void printGameOverMessage();

  /**
   * Disables user interaction with the board and hand panels.
   */
  void disableInput();

  /**
   * Enables user interaction with the board and hand panels.
   */
  void enableInput();
}
