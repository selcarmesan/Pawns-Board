package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.Player;

/**
 * An interface panel that displays PawnsBoardGame information.
 */
public interface PawnsBoardPanel {

  /**
   * Gets the current player assigned to the panel.
   * @return player of the panel
   */
  Player getPlayer();

  /**
   * Gets the current index of the panel.
   * @return index of the panel
   */
  int getIndex();
}
