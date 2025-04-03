package cs3500.pawnsboard.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActions;
import cs3500.pawnsboard.model.strategies.Move;
import cs3500.pawnsboard.model.strategies.PawnsBoardStrategy;

/**
 * Represents one of the two players within a game of Pawns Board.  Can be either a human player or
 * a machine player depending on usage.  Houses all needed actions a player can take during a game.
 */
public class UserPlayer implements UserPlayerActions, UserPlayerInterface {

  private final List<UserPlayerActionSubscriber> listeners;
  private final PawnsBoard model;
  private final Player player;
  private final PawnsBoardStrategy strategy;

  /**
   * Creates a new human player for the designated game of a certain color.
   * @param model the model
   * @param player the player color
   * @throws IllegalArgumentException if model is null
   *                                  if player is null
   */
  public UserPlayer(PawnsBoard model, Player player) {
    this(model, player, null);
  }

  /**
   * Creates a new machine player for the designated game of a certain color that uses a
   * strategy for determining moves to be made.
   * @param model the model
   * @param player the player color
   * @param strategy the strategy to be used
   */
  public UserPlayer(PawnsBoard model, Player player, PawnsBoardStrategy strategy) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null");
    }
    this.model = model;
    this.player = player;
    this.strategy = strategy;
    this.listeners = new ArrayList<>();
  }

  /**
   * Plays the card based on the already selected/highlighted cell on the board, and card within the
   * hand.
   *
   * @param row the row
   * @param col the column
   * @param index the hand index
   */
  @Override
  public void makePlay(int row, int col, int index) {
    for (UserPlayerActionSubscriber listener : listeners) {
      listener.makeMove(row, col, index);
    }
  }

  /**
   * Skips the current player's turn.
   */
  @Override
  public void skipTurn() {
    for (UserPlayerActionSubscriber listener : listeners) {
      listener.passMove();
    }
  }

  /**
   * Subscribes the listener to these action events.
   *
   * @param listener the listener
   * @throws IllegalArgumentException if listener is null
   */
  @Override
  public void addListener(UserPlayerActionSubscriber listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }
    this.listeners.add(listener);
  }

  /**
   * Decides and plays the next move for this player.
   *
   * @throws IllegalStateException if this player is not a machine
   */
  @Override
  public void decideMove() {
    if (strategy == null) {
      throw new IllegalStateException("This player is not a machine, and does not use a strategy.");
    }
    Move move = strategy.choosePlay(model, player);
    if (move != null) {
      makePlay(move.row, move.col, move.handIndex);
    } else {
      skipTurn();
    }
  }

  /**
   * Returns which player color that this player represents.
   * @return the player color
   */
  @Override
  public Player getThisPlayer() {
    return this.player;
  }

  /**
   * Returns whether this player represents a machine or not.
   * @return if it is a machine
   */
  @Override
  public boolean isMachine() {
    return this.strategy != null;
  }
}
