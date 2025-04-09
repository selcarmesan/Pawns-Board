package cs3500.exchanged.view;

import cs3500.exchanged.model.PlayerColor;
import cs3500.exchanged.model.ReadonlyPawnsBoardModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Main view class for Pawns Board.
 * Represents the main GUI frame.
 */
public class PawnsBoardGUI extends JFrame implements PawnsBoardGUIView {
  private ReadonlyPawnsBoardModel model;
  private BoardPanel boardPanel;
  private HandPanel handPanel;
  private InfoPanel infoPanel;

  /**
   * Constructs the main GUI frame.
   * @param model the read-only model
   */
  public PawnsBoardGUI(ReadonlyPawnsBoardModel model) {
    this.model = model;

    setTitle("Pawns Board");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create panels
    handPanel = new HandPanel(model);
    boardPanel = new BoardPanel(model);
    infoPanel = new InfoPanel();

    // Add panels to frame
    add(boardPanel, BorderLayout.CENTER);
    add(handPanel, BorderLayout.SOUTH);
    add(infoPanel, BorderLayout.NORTH);

    boardPanel.setFocusable(true);
    boardPanel.setRequestFocusEnabled(true);
  }

  @Override
  public void refresh() {
    boardPanel.setBoardSize(model.boardGetRows(), model.boardGetColumns());
    boardPanel.setPawnCounts(model.getFullPawnCounts());
    boardPanel.setCellOwnership(model.getOwnerships());

    // Update row scores for both players
    int[][] rowScores = model.getPlayerRowScores();
    for (int row = 0; row < model.boardGetRows(); row++) {
      boardPanel.boardSetRowScore(
            row, rowScores[model.getCurrentPlayer().getColor() == PlayerColor.RED ? 0 : 1][row]);
    }

    handPanel.updateHand(model.getCurrentPlayer().getHand());
    handPanel.updateBackground(model.getCurrentPlayer().getColor());
    infoPanel.updateInfo(model.getCurrentPlayer().getColor());

    repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void switchTurns() {
    handPanel.updateHand(model.getCurrentPlayer().getHand()); // Update displayed hand
    handPanel.updateBackground(model.getCurrentPlayer().getColor());
    infoPanel.updateInfo(model.getCurrentPlayer().getColor());
    repaint();
  }

  @Override
  public void updateRowScores(int row, int redScore, int blueScore) {
    int[][] updatedScores = boardPanel.getPlayerRowScores(); // Get current scores
    updatedScores[0][row] = redScore; // Update Red's score
    updatedScores[1][row] = blueScore; // Update Blue's score
    boardPanel.setPlayerRowScores(updatedScores); // Send to BoardPanel
  }

  @Override
  public BoardPanel getBoardPanel() {
    return boardPanel;
  }

  @Override
  public HandPanel getHandPanel() {
    return handPanel;
  }

  @Override
  public void printErrorMessage(String message, String title) {
    showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void printGameOverMessage() {
    if (model.getWinner() == null) {
      showMessageDialog(
            this, "Tie game!", "GAME OVER!", JOptionPane.INFORMATION_MESSAGE);
    } else {
      showMessageDialog(this, "Winner: "
            + model.getWinner() + " with score: "
            + model.getWinnerScore(), "GAME OVER!", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  @Override
  public void disableInput() {
    boardPanel.setEnabled(false);
    handPanel.setEnabled(false);
    for (MouseListener ml : boardPanel.getMouseListeners()) {
      boardPanel.removeMouseListener(ml);
    }
    for (MouseListener ml : handPanel.getMouseListeners()) {
      handPanel.removeMouseListener(ml);
    }
    this.setFocusable(false);
  }

  @Override
  public void enableInput() {
    boardPanel.setEnabled(true);
    handPanel.setEnabled(true);
    for (MouseListener ml : boardPanel.getMouseListeners()) {
      boardPanel.addMouseListener(ml);
    }
    for (MouseListener ml : handPanel.getMouseListeners()) {
      handPanel.addMouseListener(ml);
    }
    this.setFocusable(false);
  }
}
