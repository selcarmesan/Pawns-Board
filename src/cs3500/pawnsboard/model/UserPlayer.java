package cs3500.pawnsboard.model;

/**
 * Represents one of the two players within a game of Pawns Board.  Can be either a human player or
 * a machine player depending on usage.  Houses all needed actions a player can take during a game.
 */
public class UserPlayer implements UserPlayerInterface {

  private final Player player;
  private final PawnsBoard model;
  private int row;
  private int col;
  private int index;

  /**
   * Creates a new player for the designated game of a certain color.
   * @param model the model
   * @param player the player
   * @throws IllegalArgumentException if model is null
   *                                  if player is null
   */
  public UserPlayer(PawnsBoard model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null");
    }
    this.model = model;
    this.player = player;
    this.row = -1;
    this.col = -1;
    this.index = -1;
  }

  /**
   * Selects the specified card from the player's hand to place in queue to be played.
   *
   * @param index the index of the card
   * @throws IllegalArgumentException if index is not valid for player
   * @throws IllegalStateException if not currently this player's turn
   */
  @Override
  public void selectCard(int index) {
    if (model.getCurrentTurn() != player) {
      throw new IllegalStateException("It is not your turn!");
    }
    if (index < 0 || index >= model.getHand(player).size()) {
      throw new IllegalArgumentException("Selected card invalid");
    }
    this.index = index;
  }

  /**
   * Selects the specified row and column on the board to put in queue for the next play.
   *
   * @param row the row to place the card in
   * @param col the column to place the card in
   * @throws IllegalArgumentException if row or column are out of bounds
   * @throws IllegalStateException if not currently this player's turn
   */
  @Override
  public void selectCell(int row, int col) {
    if (model.getCurrentTurn() != player) {
      throw new IllegalStateException("It is not your turn!");
    }
    if (row < 0 || col < 0 || row >= model.getRows() || col >= model.getCols()) {
      throw new IllegalArgumentException("Selected cell invalid");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Plays the card based on the already selected/highlighted cell on the board, and card within the
   * hand.
   *
   * @throws IllegalArgumentException if the move is invalid given the selected space and card
   *                                  if a cell row and column has not been selected yet
   *                                  if a card has not been selected yet
   * @throws IllegalStateException    if it is not this player's turn
   */
  @Override
  public void makePlay() {
    if (model.getCurrentTurn() != player) {
      throw new IllegalStateException("It is not your turn!");
    }
    if (index < 0) {
      throw new IllegalArgumentException("Card not selected");
    }
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Cell not selected");
    }
    try {
      model.placeCard(row, col, index);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Selected move invalid");
    }
  }

  /**
   * Skips the current player's turn.
   *
   * @throws IllegalStateException if it is not this player's turn
   */
  @Override
  public void skipTurn() {
    if (model.getCurrentTurn() != player) {
      throw new IllegalStateException("It is not your turn!");
    }
    model.skipTurn();
  }
}
