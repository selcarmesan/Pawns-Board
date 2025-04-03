package cs3500.pawnsboard.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

import cs3500.pawnsboard.model.CellReadOnly;
import cs3500.pawnsboard.model.Player;

/**
 * The PawnsBoardCellButton implements PawnsBoardButton and constructs JButton to represent a cell
 * in a PawnsBoardGame.
 */
public class PawnsBoardCellButton extends JButton implements PawnsBoardButton, ActionListener {

  private final int row;
  private final int col;
  private final CellReadOnly cell;
  private PawnsBoardVisualView view;

  /**
   * Creates a new PawnsBoardCellButton from the given cell.
   * The information about the cell is graphically represented.
   * It also holds information regarding the cells row and col.
   * @param row row of the cell
   * @param col col of the cell
   * @param cell cell
   * @param view parent view
   */
  public PawnsBoardCellButton(int row, int col, CellReadOnly cell, PawnsBoardVisualView view) {
    this.row = row;
    this.col = col;
    this.cell = cell;
    this.view = view;
    this.setFont(new Font("Arial", Font.BOLD, 18));
    this.setOpaque(true);
    this.setHorizontalAlignment(JLabel.CENTER);
    this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    this.setFocusable(false);
    this.addActionListener(this);
    if (Objects.isNull(this.cell.getOwner())) {
      this.setBackground(Color.GRAY);
    } else if (this.cell.getOwner().equals(Player.RED)) {
      this.setBackground(Color.RED);
      this.setForeground(Color.RED);
    } else if (this.cell.getOwner().equals(Player.BLUE)) {
      this.setBackground(Color.BLUE);
      this.setForeground(Color.BLUE);
    }
    String value;
    if (!Objects.isNull(this.cell.getCard())) {
      this.setForeground(Color.WHITE);
      value = String.format("%s", this.cell.getCard().getValue());
      this.setText(value);
    } else if (this.cell.getPawns() != 0) {
      this.setBackground(Color.GRAY);
      value = String.format("%s", this.cell.getPawns());
      this.setText(value);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this) {
      if (!this.getBackground().equals(Color.CYAN)) {
        this.setBackground(Color.CYAN);
      }
      view.setLastChosenCell(this);
    }
  }

  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int getCol() {
    return col;
  }

  @Override
  public CellReadOnly getCell() {
    return cell;
  }
}
