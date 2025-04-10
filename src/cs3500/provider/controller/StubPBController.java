package cs3500.provider.controller;

import cs3500.provider.model.Card;
import cs3500.provider.model.GameModel;
import cs3500.provider.view.CardClickActions;
import cs3500.provider.view.PawnsBoardGUI;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Stub controller for testing view functions.
 * Will be replaced with an actual controller.
 */
public class StubPBController extends KeyAdapter implements CardClickActions {
  private final GameModel model;
  private PawnsBoardGUI view;

  /**
   * Constructs the stub controller for Pawns Boards.
   * @param model the game model
   * @param view the game GUI
   */
  public StubPBController(GameModel model, PawnsBoardGUI view) {
    this.model = model;
    this.view = view;
    setupListeners();
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

  private void setupListeners() {
    // Controller listens for card clicks
    view.getHandPanel().setCardClickListener(this);

    // Board cell click listener
    view.getBoardPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point selectedCell = view.getBoardPanel().getSelectedCell();
        if (selectedCell != null) {
          System.out.println(
                  "Cell Selected: (" + selectedCell.x + ", " + selectedCell.y + ")");
        }
      }
    });
  }

  @Override
  public void onCardClicked(Card selectedCard, int index) {
    System.out.println("Card Selected: Index "
            + index + ", Owner: " + selectedCard.getOwner());
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      System.out.println("Move confirmed!");
      Card selectedCard = view.getHandPanel().getSelectedCard();
      Point selectedCell = view.getBoardPanel().getSelectedCell();

      if (selectedCard != null && selectedCell != null) {
        int row = selectedCell.y;
        int col = selectedCell.x;

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

      } else {
        System.out.println("Invalid move!");
      }
    } else if (e.getKeyCode() == KeyEvent.VK_P) {
      System.out.println("Player passed!");
      model.passTurn();
      view.refresh();
      view.switchTurns();
      System.out.println(model.getCurrentPlayer().getColor() + "'s turn!");
    }
  }
}