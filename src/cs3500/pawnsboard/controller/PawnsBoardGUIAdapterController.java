package cs3500.pawnsboard.controller;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.UserPlayer;
import cs3500.pawnsboard.model.observer.ModelUpdateSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;
import cs3500.pawnsboard.provider.model.Card;
import cs3500.pawnsboard.provider.players.PlayerActionListener;
import cs3500.pawnsboard.provider.view.CardClickActions;
import cs3500.pawnsboard.provider.view.PawnsBoardGUIView;

public class PawnsBoardGUIAdapterController implements ModelUpdateSubscriber, PlayerActionListener,
        UserPlayerActionSubscriber, CardClickActions {

  private final PawnsBoardSimple model;
  private final UserPlayer player;
  private final PawnsBoardGUIView view;

  private Card card;
  private int index;
  private int row;
  private int col;

  public PawnsBoardGUIAdapterController(PawnsBoardSimple model, UserPlayer player, PawnsBoardGUIView view) {
    if (model == null || player == null || view == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }
    model.addListener(this, player.getThisPlayer());
    view.getBoardPanel().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point selectedCell = view.getBoardPanel().getSelectedCell();
        if (selectedCell != null) {
          onCellSelected(selectedCell.y, selectedCell.x);
        }
      }
    });
    view.getHandPanel().setCardClickListener(this);
    view.getBoardPanel().addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          onTurnPassed();
        }
      }
    });
    view.getBoardPanel().addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          onMoveConfirmed();
        }
      }
    });
    player.addListener(this);
    this.model = model;
    this.player = player;
    this.view = view;
    changeTurn();
    if (model.getCurrentTurn() == this.player.getThisPlayer()) {
      notifyView("Your turn has started");
    }
  }

  /**
   * Notifies the GUI in a popup message of the provided message.
   *
   * @param message the message notification
   */
  @Override
  public void notifyView(String message) {
    if (message.toLowerCase().contains("game has ended")) {
      view.printGameOverMessage();
    } else {
      view.printErrorMessage(message, "Information Popup");
    }
  }

  /**
   * Responds to the change of a turn, likely disabling inputs from the other player.
   */
  @Override
  public void changeTurn() {
    updateRowScores();
    view.refresh();
    view.switchTurns();
  }

  private void updateRowScores() {
    for (int i = 0; i < model.getRows(); i++) {
      view.updateRowScores(i, model.getRowScore(Player.RED, i), model.getRowScore(Player.BLUE, i));
    }
  }

  /**
   * Called when a player selects a card to play.
   *
   * @param card the selected card
   */
  @Override
  public void onCardSelected(Card card) {
    this.card = card;
  }

  private void onCardSelectedIndex(int index) {
    this.index = index;
  }

  /**
   * Called when a player selects a cell to play on.
   *
   * @param row the row of the selected cell
   * @param col the column of the selected cell
   */
  @Override
  public void onCellSelected(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Called when a player confirms their move.
   */
  @Override
  public void onMoveConfirmed() {
    if (model.getCurrentTurn() == this.player.getThisPlayer()) {
      try {
        if (model.isMoveValid(row, col, index, model.getCurrentTurn())) {
          model.placeCard(row, col, index);
          changeTurn();
          view.refresh();
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        notifyView("Invalid move");
      }

    } else {
      notifyView("Valid space and card not selected, or it is not your turn");
    }
  }

  /**
   * Called when a player passes their turn.
   */
  @Override
  public void onTurnPassed() {
    if (model.getCurrentTurn() == this.player.getThisPlayer()) {
      model.skipTurn();
      changeTurn();
      view.refresh();
    } else {
      notifyView("It is not your turn!");
    }
  }

  /**
   * Calls for a move to be made for the player.
   *
   * @param row   the row
   * @param col   the column
   * @param index the hand index
   */
  @Override
  public void makeMove(int row, int col, int index) {
    this.row = row;
    this.col = col;
    this.index = index;
    onMoveConfirmed();
  }

  /**
   * Calls for a turn to be skipped for the player.
   */
  @Override
  public void passMove() {
    onTurnPassed();
  }

  /**
   * Called when a card in the hand panel is clicked.
   *
   * @param selectedCard the card that was clicked, or null if no card is selected
   * @param index        the index of the selected card in the hand, or -1 if not found
   */
  @Override
  public void onCardClicked(Card selectedCard, int index) {
    this.index = index;
    this.card = selectedCard;
  }
}
