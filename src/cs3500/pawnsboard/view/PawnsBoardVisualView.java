package cs3500.pawnsboard.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardReadOnly;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActions;

/**
 * A simple graphical interface for a game of PawnsBoard.
 */
public class PawnsBoardVisualView extends JFrame
        implements PawnsBoardVisual, KeyListener, UserPlayerActions {

  PawnsBoardReadOnly model;
  private int width;
  private int height;
  private final Player player;
  private PawnsBoardCellButton lastChosenCell;
  private PawnsBoardCardPanel lastChosenCard;
  private final List<UserPlayerActionSubscriber> listeners;

  /**
   * A constructor for PawnsBoardVisualView, which takes in a game of PawnsBoard in read only.
   * Sets up the initial board and card.
   * @param model PawnsBoardReadOnly
   * @throws IllegalArgumentException when the model is null
   */
  public PawnsBoardVisualView(PawnsBoardReadOnly model, Player player) {
    if (Objects.isNull(model) || Objects.isNull(player)) {
      throw new IllegalArgumentException("Invalid Model or player");
    }
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("PawnsBoard");
    this.setSize(1280, 720);
    this.width = this.getWidth();
    this.height = this.getHeight();
    this.player = player;
    this.model = model;
    this.setLayout(null);
    //this.setResizable(false);
    this.setVisible(true);
    this.addKeyListener(this);
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        width = getWidth();
        height = getHeight();
        update();
      }
    });
    this.setTitle(String.format("Turn: %s", model.getCurrentTurn()));
    lastChosenCell = null;
    lastChosenCard = null;
    generateBoard();
    generateCards();
    listeners = new ArrayList<>();
  }

  @Override
  public void update() {
    //Update the title
    this.setTitle(String.format("Turn: %s", model.getCurrentTurn()));

    //Remove Previous Board
    this.getContentPane().removeAll();

    //Get New Board
    this.generateBoard();
    this.generateCards();

    //Window On Top
    if (model.getCurrentTurn().equals(player)) {
      setVisible(true);
    }

    //Update JFrame to Display Changes
    this.repaint();
    this.revalidate();
  }

  /**
   * Generates a board, taking in the current state of the model.
   */
  private void generateBoard() {
    int numRow = model.getRows();
    int numCol = model.getCols();

    int cellSizeX = width / (numCol + 2);
    int cellSizeY = height / (numRow + 2);

    for (int row = 0; row < numRow; row++) {
      JLabel rowScore = new JLabel();
      String value = String.format("%s", model.getRowScore(Player.RED, row));
      rowScore.setFont(new Font("Arial", Font.BOLD, 18));
      rowScore.setText(value);
      rowScore.setBackground(Color.WHITE);
      rowScore.setOpaque(true);
      rowScore.setHorizontalAlignment(JLabel.CENTER);
      rowScore.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
      rowScore.setBounds(0, row * cellSizeY, cellSizeX, cellSizeY);
      this.add(rowScore);
      for (int col = 0; col < numCol; col++) {
        PawnsBoardCellButton cell = new PawnsBoardCellButton(row, col,
                model.getCellAt(row, col), this);
        cell.setBounds((col + 1) * cellSizeX, row * cellSizeY, cellSizeX, cellSizeY);
        this.add(cell);
      }
      rowScore = new JLabel();
      value = String.format("%s", model.getRowScore(Player.BLUE, row));
      rowScore.setFont(new Font("Arial", Font.BOLD, 18));
      rowScore.setText(value);
      rowScore.setBackground(Color.WHITE);
      rowScore.setOpaque(true);
      rowScore.setHorizontalAlignment(JLabel.CENTER);
      rowScore.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
      rowScore.setBounds((numCol + 1) * cellSizeX, row * cellSizeY, cellSizeX, cellSizeY);
      this.add(rowScore);
    }
  }

  private void generateCards() {
    List<Card> hand = model.getHand(player);
    int cellSizeX = width / hand.size();
    int cellSizeY = height / (model.getRows() + 2);
    for (int i = 0; i < hand.size(); i++) {
      PawnsBoardCardPanel card = new PawnsBoardCardPanel(hand.get(i), player,
              i * cellSizeX, model.getRows() * cellSizeY,
              cellSizeX, cellSizeY * 2, i, this);
      this.add(card);
    }
  }

  @Override
  public PawnsBoardReadOnly getModel() {
    return model;
  }

  @Override
  public void setLastChosenCell(PawnsBoardCellButton cell) {
    if (Objects.isNull(lastChosenCell)) {
      lastChosenCell = cell;
    } else if (Objects.isNull(cell)) {
      changeCellColor();
      lastChosenCell = null;
    } else if (lastChosenCell.equals(cell)) {
      changeCellColor();
      lastChosenCell = null;
    } else {
      changeCellColor();
      cell.setBackground(Color.CYAN);
      lastChosenCell = cell;
    }
  }

  /**
   * Returns the color of the lastChosenCell to its original color.
   */
  private void changeCellColor() {
    Player lastPlayer = lastChosenCell.getCell().getOwner();
    if (Objects.isNull(lastPlayer) || lastChosenCell.getCell().getPawns() != 0) {
      lastChosenCell.setBackground(Color.GRAY);
    } else if (lastPlayer.equals(Player.RED)) {
      lastChosenCell.setBackground(Color.RED);
    } else {
      lastChosenCell.setBackground(Color.BLUE);
    }
  }

  @Override
  public void setLastChosenCard(PawnsBoardCardPanel card) {
    if (Objects.isNull(lastChosenCard)) {
      lastChosenCard = card;
    } else if (lastChosenCard.equals(card)) {
      if (lastChosenCard.getPlayer().equals(Player.RED)) {
        lastChosenCard.setBackground(Color.RED);
      } else {
        lastChosenCard.setBackground(Color.BLUE);
      }
      lastChosenCard = null;
    } else {
      if (lastChosenCard.getPlayer().equals(Player.RED)) {
        lastChosenCard.setBackground(Color.RED);
      } else {
        lastChosenCard.setBackground(Color.BLUE);
      }
      lastChosenCard = card;
    }
  }

  @Override
  public PawnsBoardCellButton getLastChosenCell() {
    return lastChosenCell;
  }

  @Override
  public PawnsBoardCardPanel getLastChosenCard() {
    return lastChosenCard;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Placeholder
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      if (lastChosenCell == null || lastChosenCard == null) {
        notify("You must select a cell and card to play.");
      } else {
        makePlay(lastChosenCell.getRow(), lastChosenCell.getCol(), lastChosenCard.getIndex());
        lastChosenCell = null;
        lastChosenCard = null;
      }
    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      skipTurn();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Placeholder
  }

  /**
   * Plays the card based on the already selected/highlighted cell on the board, and card within the
   * hand.
   *
   * @param row   the row
   * @param col   the column
   * @param index the hand index
   * @throws IllegalArgumentException if the move is invalid given the selected space and card
   * @throws IllegalStateException    if it is not this player's turn
   */
  @Override
  public void makePlay(int row, int col, int index) {
    for (UserPlayerActionSubscriber listener : listeners) {
      listener.makeMove(row, col, index);
    }
  }

  /**
   * Skips the current player's turn.
   *
   * @throws IllegalStateException if it is not this player's turn
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
      throw new IllegalArgumentException("Listener must not be null");
    }
    this.listeners.add(listener);
  }

  /**
   * Notifies the GUI in a popup message of the provided message.
   * @param message the message notification
   */
  @Override
  public void notify(String message) {
    System.out.println("Popup for " + player + ": " + message);
    // Add some code to have a popup or some other component to notify with the message
    JOptionPane.showMessageDialog(null, message,
            "Popup for " + player + ":", JOptionPane.INFORMATION_MESSAGE);
  }
}
