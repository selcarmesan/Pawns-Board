package cs3500.exchanged.view;

import cs3500.exchanged.model.PlayerColor;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Panel for the Information visual representation.
 */
public class InfoPanel extends JPanel {
  private JLabel infoLabel;

  /**
   * Constructs the visual information panel.
   */
  public InfoPanel() {
    setPreferredSize(new Dimension(600, 50));
    setLayout(new FlowLayout(FlowLayout.CENTER));
    infoLabel = new JLabel("Player: RED", SwingConstants.CENTER);
    infoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    add(infoLabel);
  }

  /**
   * Updates the player turn information.
   * @param color which player is currently playing
   */
  public void updateInfo(PlayerColor color) {
    if (color == PlayerColor.RED) {
      infoLabel.setText("RED Player's Turn");
    } else if (color == PlayerColor.BLUE) {
      infoLabel.setText("BLUE Player's Turn");
    }
  }

  /**
   * Repaints the view.
   */
  public void refresh() {
    repaint();
  }
}
