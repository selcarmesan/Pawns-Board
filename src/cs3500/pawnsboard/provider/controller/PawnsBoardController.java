package cs3500.pawnsboard.provider.controller;

import cs3500.pawnsboard.provider.model.PlayerColor;
import cs3500.pawnsboard.provider.view.PawnsBoardGUI;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.model.GameModel;
import cs3500.pawnsboard.provider.players.IPlayer;
import cs3500.pawnsboard.provider.players.ModelStatusListener;
import cs3500.pawnsboard.provider.players.PlayerActionListener;
import cs3500.pawnsboard.provider.view.CardClickActions;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Stub controller for testing view functions.
 * Will be replaced with an actual controller.
 */
public class PawnsBoardController extends
        KeyAdapter implements CardClickActions, ModelStatusListener, PlayerActionListener {
  private final GameModel model;
  private final PawnsBoardGUI view;
  private Card selectedCard;
  private Point selectedCell;
  private IPlayer player;

  /**
   * Constructs the stub controller for Pawns Boards.
   * @param model the game model
   * @param view the game GUI
   */
  public PawnsBoardController(GameModel model, PawnsBoardGUI view, IPlayer player) {
    if (model == null || view == null || player == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.player = player;
    this.selectedCell = null;
    this.selectedCard = null;
    this.view.getHandPanel().setCardClickListener(this);

    // Board cell click listener
    view.getBoardPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point selectedCell = view.getBoardPanel().getSelectedCell();
        if (selectedCell != null) {
          System.out.println(
                  "Cell Selected: (" + selectedCell.x + ", " + selectedCell.y + ")");
          onCellSelected(selectedCell.y, selectedCell.x);
        }
      }
    });

    this.model.addModelStatusListener(this);
    this.player.addPlayerActionListener(this);
  }

  /**
   * Starts the game of Pawns Boards.
   */
  public void startGame() {
    view.makeVisible();
    view.addKeyListener(this);
    view.setFocusable(true);
    view.requestFocusInWindow();
  }

  @Override
  public void onCardClicked(Card selectedCard, int index) {
    System.out.println("Card Selected: Index "
            + index + ", Owner: " + selectedCard.getOwner());
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      Card selectedCard = view.getHandPanel().getSelectedCard();
      Point selectedCell = view.getBoardPanel().getSelectedCell();

      if (selectedCard != null && selectedCell != null) {
        int row = selectedCell.y;
        int col = selectedCell.x;

        try {
          // 1. Apply card influence + placement logic in Model
          model.playTurn(selectedCard, row, col);

          // 2. Fetch updated data
          view.getBoardPanel().setPawnCounts(model.getFullPawnCounts());
          view.getBoardPanel().setCellOwnership(model.getOwnerships());
          view.getBoardPanel().setPlayerRowScores(model.getPlayerRowScores());

          // 3. Update view with card placement
          view.getBoardPanel().placeCardOnCell(selectedCard, selectedCell);

          view.refresh();
          view.switchTurns();
          System.out.println("Move confirmed!");
        } catch (IllegalArgumentException iae) {
          view.printErrorMessage(iae.getMessage(), "Invalid Move");
          clearSelections();
        }
      } else {
        System.out.println("Invalid move!");
        view.printErrorMessage(
              "You must select both a card and a cell to make a move.", "Invalid Move");
        clearSelections();
      }
    } else if (e.getKeyCode() == KeyEvent.VK_P) {
      System.out.println("Player passed!");
      // 1. Notify model to switch players
      model.passTurn();

      // 2. Update view after switch
      view.refresh();
      System.out.println(model.getCurrentPlayer().getColor() + "'s turn!");
    }
    checkGameState();
  }

  private void clearSelections() {
    this.selectedCard = null;
    this.selectedCell = null;
    view.getBoardPanel().clearSelection();
    view.getHandPanel().clearSelection();
  }

  private void checkGameState() {
    if (model.isGameOver()) {
      view.printGameOverMessage();
      view.disableInput();
    }
  }

  private boolean isPlayerTurn() {
    return model.getCurrentPlayer().equals(player.getColor());
  }

  @Override
  public void onTurnChange(PlayerColor playerColor) {
    // Only allow interaction if it's this player's turn
    if (!isPlayerTurn()) {
      view.disableInput();
    } else {
      view.enableInput();
    }
    view.refresh();
  }

  @Override
  public void onGameEnd(PlayerColor winner, int winningScore) {
    checkGameState();
  }

  @Override
  public void onGameStart() {
    startGame();
  }

  @Override
  public void onCardSelected(Card card) {
    if (!isPlayerTurn()) {
      return;
    }
    this.selectedCard = card;
  }

  @Override
  public void onCellSelected(int row, int col) {
    if (!isPlayerTurn()) {
      return;
    }
    this.selectedCell = new Point(col, row);
    view.getBoardPanel().setSelectedCell(selectedCell);
  }

  @Override
  public void onMoveConfirmed() {
    if (!isPlayerTurn()) {
      return;
    }
    if (selectedCard == null || selectedCell == null) {
      view.printErrorMessage(
            "You must select both a card and a cell to make a move.", "Invalid Move");
      return;
    }

    try {
      int row = selectedCell.y;
      int col = selectedCell.x;
      model.playTurn(selectedCard, row, col);
      clearSelections();
      view.refresh();
    } catch (IllegalArgumentException e) {
      view.printErrorMessage(e.getMessage(), "Invalid Move!");
    }
  }

  @Override
  public void onTurnPassed() {
    if (!isPlayerTurn()) {
      return;
    }
    clearSelections();
    model.passTurn();
    view.refresh();
  }
}