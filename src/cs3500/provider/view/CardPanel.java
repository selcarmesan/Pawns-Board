package cs3500.provider.view;

import cs3500.provider.model.Card;
import cs3500.provider.model.InfluenceType;
import cs3500.provider.model.PlayerColor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Panel for the Card visual representation.
 */
public class CardPanel extends JPanel {
  private Card card;

  /**
   * Constructs the Card panel.
   * @param card the card this panel is representing
   */
  public CardPanel(Card card) {
    this.card = card;
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    setCardBackground();
    setLayout(new BorderLayout());

    add(createInfoPanel(), BorderLayout.NORTH);
    add(createInfluenceGridPanel(), BorderLayout.CENTER);
  }

  // Sets a custom background color depending on the current player
  private void setCardBackground() {
    Color customBlue = new Color(180, 200, 255);
    Color customRed = new Color(255, 200, 210);

    if (card.getOwner() == PlayerColor.BLUE) {
      setBackground(customBlue);
    } else if (card.getOwner() == PlayerColor.RED) {
      setBackground(customRed);
    }
  }

  // Creates the information displayed on each card -- name cost, value
  private JPanel createInfoPanel() {
    JPanel infoPanel = new JPanel(new GridLayout(3, 1));
    infoPanel.setOpaque(false);

    // Name
    JLabel nameLabel = new JLabel(card.getName(), SwingConstants.CENTER);
    nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    // Cost
    JLabel costLabel = new JLabel("Cost: " + card.getCost().getNumCost(), SwingConstants.CENTER);
    costLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    // Score
    JLabel scoreLabel = new JLabel("Score: " + card.getValueScore(), SwingConstants.CENTER);
    scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

    infoPanel.add(nameLabel);
    infoPanel.add(costLabel);
    infoPanel.add(scoreLabel);

    return infoPanel;
  }

  // Designs the influence grid of each card in the game
  private JPanel createInfluenceGridPanel() {
    Color customYellow = new Color(255, 153, 0);
    Color customGreen = new Color(10, 190, 0);

    InfluenceType[][] grid = card.getInfluenceGrid();

    // Mirror the grid for Blue player
    if (card.getOwner() == PlayerColor.BLUE) {
      grid = mirrorGrid(grid);
    }

    JPanel gridPanel = new JPanel(new GridLayout(grid.length, grid[0].length));
    gridPanel.setOpaque(false);

    for (InfluenceType[] row : grid) {
      for (InfluenceType cell : row) {
        JLabel label = new JLabel(getSymbol(cell), SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 18));

        if (cell == InfluenceType.CENTER) {
          label.setForeground(customYellow);
        } else if (cell == InfluenceType.INFLUENCE) {
          label.setForeground(customGreen);
        } else {
          label.setForeground(Color.GRAY);
        }

        gridPanel.add(label);
      }
    }

    return gridPanel;
  }

  // Mirrors the influence grid horizontally.
  private InfluenceType[][] mirrorGrid(InfluenceType[][] grid) {
    InfluenceType[][] mirrored = new InfluenceType[grid.length][grid[0].length];

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        mirrored[i][j] = grid[i][grid[i].length - 1 - j]; // Reverse each row
      }
    }

    return mirrored;
  }

  // Returns the character associated with each influence type
  private String getSymbol(InfluenceType type) {
    switch (type) {
      case CENTER:
        return "C";
      case INFLUENCE:
        return "I";
      default:
        return "X";
    }
  }

  /**
   * Repaints the view.
   */
  public void refresh() {
    repaint();
  }
}
