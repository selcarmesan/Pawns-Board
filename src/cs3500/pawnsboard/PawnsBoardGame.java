package cs3500.pawnsboard;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import cs3500.pawnsboard.controller.PawnsBoardGUIController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.UserPlayer;
import cs3500.pawnsboard.model.strategies.StrategyFillFirst;
import cs3500.pawnsboard.model.strategies.StrategyMaximizeRowScore;
import cs3500.pawnsboard.view.PawnsBoardVisualView;

/**
 * Allows for the specified creation of a new game of pawns board through two GUI menus.
 * This requires first specifying the file locations for both player's decks, and then which player
 * is controlled by what, whether human, or which numbered strategy strategy1-strategy2.
 * Strategy1 has the machine player simply pick the first available option to play.
 * Strategy2 has the machine player maximize its row score by playing first in rows where its score
 * will overtake the other player's after playing.
 */
public class PawnsBoardGame {

  /**
   * Creates a new PawnsBoardGame game.
   * Runs the new game with predetermined moves until completion.
   */
  public static void main(String[] args) {
    Readable in = new InputStreamReader(System.in);
    Scanner sc = new Scanner(in);
    System.out.println("Type four words, two for the file paths for RED and BLUE");
    System.out.println("And one for the RED player's AI, and one for the BLUE player's AI");
    System.out.println("Options: ");
    System.out.println("1. 'human' - for a GUI controlled player");
    System.out.println("2. 'strategy1' - for a machine player which picks the first open option");
    System.out.println("3. 'strategy2' - for a machine player which maximizes row score");
    //Initializing PawnsBoardGame
    File file1 = new File(sc.next());
    File file2 = new File(sc.next());
    try {
      List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file1);
      List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file2);
      PawnsBoardSimple model = new PawnsBoardSimple(3, 5);
      UserPlayer player1 = createPlayer(model, Player.RED, sc.next());
      UserPlayer player2 = createPlayer(model, Player.BLUE, sc.next());
      if (player1 == null || player2 == null) {
        System.out.println("Invalid commands chosen");
        return;
      }
      model.startGame(redDeck, blueDeck, 5, true);
      PawnsBoardVisualView viewPlayer1 = new PawnsBoardVisualView(model, Player.RED);
      PawnsBoardVisualView viewPlayer2 = new PawnsBoardVisualView(model, Player.BLUE);
      PawnsBoardGUIController controller1 = new PawnsBoardGUIController(model, player1, viewPlayer1);
      PawnsBoardGUIController controller2 = new PawnsBoardGUIController(model, player2, viewPlayer2);
    } catch (IllegalArgumentException e) {
      System.out.println("Incorrect file format or location");
    }
  }

  private static UserPlayer createPlayer(PawnsBoardSimple model, Player player, String name) {
    switch (name) {
      case "human":
        return new UserPlayer(model, player);
      case "strategy1":
        return new UserPlayer(model, player, new StrategyFillFirst());
      case "strategy2":
        return new UserPlayer(model, player, new StrategyMaximizeRowScore());
      default:
        return null;
    }
  }
}
