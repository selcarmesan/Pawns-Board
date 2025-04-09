package cs3500.exchanged.model;

/**
 * Represents the game board, which consists of a rectangular grid of cells.
 * The board tracks placed cards and pawns, and enforces game rules.
 */
public class Board {
  private final int rows;
  private final int columns;
  private final Cell[][] grid;
  private boolean isGameOver;

  /**
   * Constructor for the Board class.
   * @param rows       The number of rows in the board. Must be positive.
   * @param columns    The number of columns in the board. Must be odd and greater than 1.
   * @param redPlayer  The red player.
   * @param bluePlayer The blue player.
   * @throws IllegalArgumentException If the board dimensions are invalid.
   */
  public Board(int rows, int columns, Player redPlayer, Player bluePlayer) {
    if (rows <= 0 || columns <= 1 || columns % 2 == 0) {
      throw new IllegalArgumentException("Invalid board dimensions.");
    }

    this.rows = rows;
    this.columns = columns;
    this.grid = new Cell[rows][columns];
    this.isGameOver = false;

    // Initialize the board with pawns in the first and last columns
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (j == 0) {
          grid[i][j] = new Cell(redPlayer.getColor()); // First column belongs to Red
          grid[i][j].setPawns(1); // Set 1 pawn for Red
        } else if (j == columns - 1) {
          grid[i][j] = new Cell(bluePlayer.getColor()); // Last column belongs to Blue
          grid[i][j].setPawns(1); // Set 1 pawn for Blue
        } else {
          grid[i][j] = new Cell((PlayerColor) null); // Middle columns are empty
          grid[i][j].setPawns(0); // Set 0 pawns for middle columns
        }
      }
    }
  }

  /**
   * Copy constructor for the Board class.
   * Allows for deep copy of the board.
   *
   * @param original The original board to copy.
   */
  public Board(Board original) {
    this.rows = original.rows;
    this.columns = original.columns;
    this.grid = new Cell[rows][columns];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        Cell originalCell = original.grid[i][j];
        this.grid[i][j] = new Cell(originalCell.getOwner());
        this.grid[i][j].setPawns(originalCell.getPawns());
        this.grid[i][j].setCard(originalCell.getCard(), originalCell.getCard().getOwner());
      }
    }
  }

  /**
   * Returns the number of rows in a board.
   * @return the number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns the number of columns in a board.
   * @return the number of columns.
   */
  public int getColumns() {
    return columns;
  }

  /**
   * Returns the cell number (row and column) on a grid when asked.
   * @return the cell number (row and column) on a grid when asked.
   */
  public Cell[][] getGrid() {
    return grid;
  }

  /**
   * Applies the card influence after its placement.
   * @param player the player who played a card
   * @param row the x-coordinate of the cell's position on the board
   * @param col the y-coordinate of the cell's position on the board
   * @param card the card that was placed
   */
  public void applyCardInfluence(Player player, int row, int col, Card card) {
    InfluenceType[][] influenceGrid = card.getInfluenceGrid();

    // Mirror blue's grid
    if (player.getColor() == PlayerColor.BLUE) {
      influenceGrid = mirrorGrid(influenceGrid);
    }

    // Iterate over the influence grid
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {

        InfluenceType influence = influenceGrid[i][j];

        int targetRow = row + (i - 2);
        int targetCol = col + (j - 2);

        // Bounds check
        if (targetRow >= 0 && targetRow < rows && targetCol >= 0 && targetCol < columns) {
          Cell targetCell = grid[targetRow][targetCol];

          // Apply influence
          if (influence == InfluenceType.INFLUENCE && targetCell.getCard() == null) {
            targetCell.addPawn(player.getColor());
          }
        }
      }
    }
  }

  private InfluenceType[][] mirrorGrid(InfluenceType[][] grid) {
    InfluenceType[][] mirrored = new InfluenceType[5][5];

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        mirrored[i][j] = grid[i][4 - j]; // Reverse each row
      }
    }
    return mirrored;
  }

  /**
   * Checks if the game is over depending on the current
   * board state.
   * @return true if all cells of the board have been filled
   *         and if there are no more pawns left, false if otherwise.
   */
  public boolean isGameOver() {
    if (isGameOver) {
      return true;
    }
    for (Cell[] row : grid) {
      for (Cell cell : row) {
        if (cell.getCard() == null && cell.getPawns() > 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Sets the game status as desired.
   * @param isGameOver true is the game is over, false if otherwise
   * @return true is the game is over, false if otherwise
   */
  public boolean setGameOver(boolean isGameOver) {
    return this.isGameOver = isGameOver;
  }

  /**
   * Calculates the total scores for both players.
   * @return An array where the first element is Red's total score
   *         and the second is Blue's total score.
   */
  public int[] calculateTotalScores() {
    int redTotal = 0;
    int blueTotal = 0;

    // Iterate over each row
    for (Cell[] row : grid) {
      int redRowScore = 0;
      int blueRowScore = 0;

      // Iterate over each cell in the row
      for (Cell cell : row) {
        if (cell.getCard() != null && cell.getOwner() != null) { // Ensure cell has a card and owner
          if (cell.getOwner() == PlayerColor.RED) {
            redRowScore += cell.getCard().getValueScore();
          } else if (cell.getOwner() == PlayerColor.BLUE) {
            blueRowScore += cell.getCard().getValueScore();
          }
        }
      }

      // Add row scores to total scores
      if (redRowScore > blueRowScore) {
        redTotal += redRowScore;
      } else if (blueRowScore > redRowScore) {
        blueTotal += blueRowScore;
      }
      // If row scores are equal, neither player gets points
    }

    return new int[]{redTotal, blueTotal};
  }

  /**
   * Calculates the score for a specific player in a given row.
   *
   * @param row the row index
   * @param color the player's color - RED or BLUE
   * @return the score for the player in the specified row
   * @throws IllegalArgumentException if the row is out of bounds.
   */
  public int getRowScore(int row, PlayerColor color) {
    if (row < 0 || row >= rows) {
      throw new IllegalArgumentException("Invalid row index.");
    }

    int score = 0;
    for (int j = 0; j < columns; j++) {
      Cell cell = grid[row][j];
      if (cell.getCard() != null && cell.getOwner() != null &&
              cell.getOwner() == color) {
        score += cell.getCard().getValueScore();
      }
    }
    return score;
  }
}