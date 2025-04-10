package cs3500.provider;

import cs3500.provider.model.Board;
import cs3500.provider.model.Card;
import cs3500.provider.model.Cell;
import cs3500.provider.model.GameModel;
import cs3500.provider.model.Player;
import cs3500.provider.model.PlayerColor;
import cs3500.provider.controller.TextDeckReader;
import cs3500.provider.view.GameTextualView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Examples class used to give readers an understanding of the model.
 * Shows a simulation of simple gameplay.
 */
public class ExamplePawnsBoard {
  /**
   * Main method, used as the entry point.
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // Construct an absolute path to deck.config
    File deckFile = new File("docs", "deck.config");
    String filePath = deckFile.getAbsolutePath(); // Get absolute path

    try {
      // Read the decks from the file
      TextDeckReader deckReader = new TextDeckReader();
      List<Card> redDeck = deckReader.readDeck(filePath, PlayerColor.RED);
      List<Card> blueDeck = deckReader.readDeck(filePath, PlayerColor.BLUE);

      // Show the red deck was successfully loaded
      System.out.println("Red deck successfully loaded:");
      for (Card card : redDeck) {
        System.out.println(card.getName()
              + " | Cost: " + card.getCost() + " | Value: " + card.getValueScore());
      }

      // Show the blue deck was successfully loaded
      System.out.println("Blue deck successfully loaded:");
      for (Card card : blueDeck) {
        System.out.println(card.getName()
                + " | Cost: " + card.getCost() + " | Value: " + card.getValueScore());
      }

      // Create sample players with separate deck copies
      Player red = new Player(PlayerColor.RED, new ArrayList<>(redDeck));
      Player blue = new Player(PlayerColor.BLUE, new ArrayList<>(blueDeck));

      // Create game with separate copies of the deck
      GameModel game = new GameModel(3, 5, new ArrayList<>(redDeck), new ArrayList<>(blueDeck));

      // Print initial board
      GameTextualView view = new GameTextualView(game);
      System.out.println("\nInitial Board:");
      view.render();

      // Play a valid first move
      playValidMove(game);

      // Simulate a full game, ensuring both players can pass consecutively
      int consecutivePasses = 0;
      while (!game.isGameOver() && consecutivePasses < 2) {
        if (!playValidMove(game)) {
          System.out.println(game.getCurrentPlayer().getColor() + " player passes.");
          consecutivePasses++;
        } else {
          consecutivePasses = 0; // Reset if a move is made
        }
        game.passTurn();
      }

      // Print final board
      System.out.println("\nFinal Board:");
      view.render();
    } catch (FileNotFoundException e) {
      System.out.println("Error: File not found at " + filePath);
    }
  }

  private static boolean playValidMove(GameModel game) {
    Player player = game.getCurrentPlayer();
    Board board = game.getBoard();
    Cell[][] grid = board.getGrid();

    for (int row = 0; row < board.getRows(); row++) {
      for (int col = 0; col < board.getColumns(); col++) {
        Cell cell = grid[row][col];

        // Ensure the cell is owned by the player and is empty
        if (cell.getOwner() == player.getColor() && cell.getCard() == null) {
          for (Card card : player.getHand()) {
            if (cell.getPawns() >= card.getCost().getNumCost()) {
              game.playTurn(card, row, col);
              System.out.println(player.getColor()
                    + " player places card: " + card.getName() + " at (" + row + "," + col + ")");
              return true;
            }
          }
        }
      }
    }
    return false; // No valid move found
  }
}
