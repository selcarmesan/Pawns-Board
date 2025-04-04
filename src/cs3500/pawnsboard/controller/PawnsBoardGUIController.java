package cs3500.pawnsboard.controller;

import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.UserPlayer;
import cs3500.pawnsboard.model.observer.ModelUpdateSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;
import cs3500.pawnsboard.view.PawnsBoardVisualView;

/**
 * A controller based on the GUI implementation of a Pawns Board view.
 */
public class PawnsBoardGUIController implements ModelUpdateSubscriber, UserPlayerActionSubscriber {

  private final PawnsBoardSimple model;
  private final UserPlayer player;
  private final PawnsBoardVisualView view;

  /**
   * Creates a new GUI controller for a specific player for a game of pawns board, using a premade
   * GUI.
   * @param model the model to control
   * @param player the player to take turns for
   * @param view the view to be informed from
   * @throws IllegalArgumentException if any arguments are null
   */
  public PawnsBoardGUIController(PawnsBoardSimple model, UserPlayer player,
                                 PawnsBoardVisualView view) {
    if (model == null || player == null || view == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }
    model.addListener(this, player.getThisPlayer());
    view.addListener(this);
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
    if (!player.isMachine()) {
      view.notify(message);
    }
  }

  /**
   * Responds to the change of a turn, likely disabling inputs from the other player.
   */
  @Override
  public void changeTurn() {
    if (model.isGameOver()) {
      view.setEnabled(true);
      return;
    }
    if (this.player.isMachine()) {
      view.setEnabled(false);
      if (model.getCurrentTurn() == this.player.getThisPlayer()) {
        this.player.decideMove();
      }
    } else {
      view.setEnabled(model.getCurrentTurn() == this.player.getThisPlayer());
    }
    view.update();
  }

  /**
   * Calls for a move to be made for the player.
   *
   * @param row the row
   * @param col the column
   * @param index the hand index
   */
  @Override
  public void makeMove(int row, int col, int index) {
    if (model.isGameOver()) {
      notifyView("Game is Over");
    } else if (model.getCurrentTurn() != player.getThisPlayer()) {
      notifyView("It is not your turn!");
    } else {
      if (!model.isMoveValid(row, col, index, player.getThisPlayer())) {
        notifyView("Move invalid.");
      } else {
        model.placeCard(row, col, index);
        view.update();
      }
    }
  }

  /**
   * Calls for a turn to be skipped for the player.
   */
  @Override
  public void passMove() {
    if (model.isGameOver()) {
      notifyView("Game is Over");
    } else if (model.getCurrentTurn() != player.getThisPlayer()) {
      notifyView("It is not your turn!");
    } else {
      model.skipTurn();
      view.update();
    }
  }
}
