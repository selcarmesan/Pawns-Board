package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoardReadOnly;

/**
 * Interface for a PawnsBoard game view.
 */
public interface PawnsBoardView {

  /**
   * Updates the view in some manner (e.g. as text, or as graphics, etc.)
   * Updates nothing if there is nothing to update.
   */
  void update();

  /**
   * Gets the model of the textual view.
   * @return model
   */
  PawnsBoardReadOnly getModel();
}
