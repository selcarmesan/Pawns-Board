package cs3500.view;

import cs3500.model.PlayerColor;
import cs3500.model.ReadonlyPawnsBoardModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel for the Board visual representation.
 */
public class BoardPanel extends JPanel implements PBBoardPanel {
  private int rows;
  private int cols;
  private int[] rowScores;
  private int[][] pawnCounts;
  private PlayerColor[][] pawnOwners;
  private Point selectedCell = null;
  private Card[][] board;
  private int[][] playerRowScores;

  /**
   * Visually constructs the Board.
   * @param model the read-only model for Pawns Boards
   */
  public BoardPanel(ReadonlyPawnsBoardModel model) {
    this.rows = model.boardGetRows();
    this.cols = model.boardGetColumns();
    this.rowScores = new int[rows];
    this.pawnCounts = new int[rows][cols];
    this.pawnOwners = new PlayerColor[rows][cols];
    this.board = new Card[rows][cols];
    this.playerRowScores = new int[2][rows];

    // Initialize pawns and their owners
    for (int row = 0; row < rows; row++) {
      pawnCounts[row][0] = 1;
      pawnOwners[row][0] = PlayerColor.RED; // First column belongs to Red

      pawnCounts[row][cols - 1] = 1;
      pawnOwners[row][cols - 1] = PlayerColor.BLUE; // Last column belongs to Blue
    }

    setPreferredSize(new Dimension(600, 400));
    setBackground(Color.LIGHT_GRAY);
    this.setFocusable(true);
    this.requestFocusInWindow();

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        int clickedCol = e.getX() / cellWidth;
        int clickedRow = e.getY() / cellHeight;

        if (selectedCell != null && selectedCell.equals(new Point(clickedCol, clickedRow))) {
          selectedCell = null; // Deselect if clicking again
        } else {
          selectedCell = new Point(clickedCol, clickedRow);
        }
        repaint();
      }
    });
  }

  @Override
  public void setBoardSize(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.rowScores = new int[rows];
    this.pawnCounts = new int[rows][cols]; // Reset pawnCounts

    // Reinitialize pawns in the first and last columns after resizing
    for (int row = 0; row < rows; row++) {
      pawnCounts[row][0] = 1; // Place a red pawn on the first column of each row
      pawnCounts[row][cols - 1] = 1; // Place a blue pawn on the last column of each row
    }
    repaint(); // Refresh view
  }

  @Override
  public void boardSetRowScore(int row, int score) {
    rowScores[row] = score;
    repaint();
  }

  @Override
  public void setPawnCounts(int[][] pawnCounts) {
    this.pawnCounts = pawnCounts;
    repaint();
  }

  @Override
  public void setCellOwnership(PlayerColor[][] ownership) {
    this.pawnOwners = ownership;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int cellWidth = getWidth() / cols;
    int cellHeight = getHeight() / rows;
    g2d.setColor(Color.BLACK);
    Color customFill = new Color(253, 254, 2);
    Color customRed = new Color(255, 100, 120);
    Color customBlue = new Color(50, 140, 240);

    // Draw grid
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int x = col * cellWidth;
        int y = row * cellHeight;

        // Highlight the selected cell with a yellow border
        if (selectedCell != null && selectedCell.equals(new Point(col, row))) {
          g2d.setColor(customFill);
          g2d.fillRect(x, y, cellWidth, cellHeight);
          g2d.setColor(Color.BLACK);
        }

        g2d.drawRect(x, y, cellWidth, cellHeight);

        // Draw card value if a card is placed
        if (board[row][col] != null) {
          // Determine cell color based on ownership
          if (pawnOwners[row][col] == PlayerColor.RED) {
            g2d.setColor(customRed);
            g2d.fillRect(x, y, cellWidth, cellHeight);
          } else if (pawnOwners[row][col] == PlayerColor.BLUE) {
            g2d.setColor(customBlue);
            g2d.fillRect(x, y, cellWidth, cellHeight);
          }
          // Add card score value to cell
          String cardValue = String.valueOf(board[row][col].getValueScore());
          g2d.setFont(new Font("SansSerif", Font.BOLD, 35));
          g2d.setColor(Color.BLACK);
          g2d.drawString(cardValue, x + cellWidth / 2 - 10, y + cellHeight / 2 + 10);
        }
      }
    }

    // Draw pawns
    drawPawns(g2d, cellWidth, cellHeight);

    // Draw row scores on the left and right side
    drawRowScores(g2d, cellHeight);
  }

  // Draws the pawns
  private void drawPawns(Graphics2D g2d, int cellWidth, int cellHeight) {
    Color customBlue = new Color(50, 140, 240);
    Color customRed = new Color(255, 100, 120);

    int pawnSize = Math.min(cellWidth, cellHeight) / 3;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int numPawns = pawnCounts[row][col];

        if (numPawns > 0) {
          int centerX = col * cellWidth + cellWidth / 2;
          int centerY = row * cellHeight + cellHeight / 2;

          // Get pawn ownership from stored data
          PlayerColor owner = pawnOwners[row][col];

          // Set color based on ownership
          Color pawnColor = (owner == PlayerColor.RED) ? customRed :
                  (owner == PlayerColor.BLUE) ? customBlue : Color.GRAY;

          for (int i = 0; i < numPawns; i++) {
            int offsetX = (i % 2 == 0) ? -pawnSize : pawnSize;
            int offsetY = (i / 2) * pawnSize;

            g2d.setColor(pawnColor);
            g2d.fillOval(centerX + offsetX, centerY + offsetY, pawnSize, pawnSize);
          }
        }
      }
    }
  }

  // Draws the row scores for both players on the left (Red) and right (Blue).
  private void drawRowScores(Graphics2D g2d, int cellHeight) {
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("SansSerif", Font.BOLD, 20));

    for (int row = 0; row < rows; row++) {
      int y = row * cellHeight + cellHeight / 2 + 5; // Positioning adjustment

      // Draw Red player's score on the left side
      g2d.drawString(String.valueOf(playerRowScores[0][row]), 10, y);

      // Draw Blue player's score on the right side
      g2d.drawString(String.valueOf(playerRowScores[1][row]), getWidth() - 30, y);
    }
  }

  @Override
  public void placeCardOnCell(Card card, Point selectedCell) {
    if (selectedCell == null || card == null) {
      return;
    }
    int row = selectedCell.y;
    int col = selectedCell.x;

    // Just place the card visually
    board[row][col] = card;

    // Remove pawn visuals on card
    pawnCounts[row][col] = 0;
    pawnOwners[row][col] = card.getOwner();

    // Deselect cell
    this.selectedCell = null;

    // Refresh hand panel
    PawnsBoardGUI gui = (PawnsBoardGUI) SwingUtilities.getWindowAncestor(this);
    if (gui != null) {
      gui.getHandPanel().removeCard(card);
      gui.getHandPanel().clearSelection();
      gui.getHandPanel().refresh();
    }

    repaint();
  }

  @Override
  public Point getSelectedCell() {
    return selectedCell;
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public int[][] getPlayerRowScores() {
    return playerRowScores;
  }

  @Override
  public void setPlayerRowScores(int[][] scores) {
    for (int row = 0; row < rows; row++) {
      playerRowScores[0][row] = scores[0][row]; // Red player's score
      playerRowScores[1][row] = scores[1][row]; // Blue player's score
    }
    repaint();
  }

  @Override
  public void clearSelection() {
    this.selectedCell = null;
    repaint();
  }

  public void setSelectedCell(Point selectedCell) {
    this.selectedCell = selectedCell;
  }
}