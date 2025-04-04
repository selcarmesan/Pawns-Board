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
 * PawnsBoard creates a new game of PawnsBoardGame and plays till it is completed.
 */
public class PawnsBoardGame {

  /**
   * Creates a new PawnsBoardGame game.
   * Runs the new game with predetermined moves until completion.
   */
  public static void main(String[] args) {
    //Check read from config and reading inputs
    File file = new File("docs" + File.separator + "deckRed.config");
    List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file);
    List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file);

    Readable in = new InputStreamReader(System.in);
    Scanner sc = new Scanner(in);
    System.out.println("Type two words, one for the RED player, and one for the BLUE player");
    System.out.println("Options: ");
    System.out.println("1. 'human' - for a GUI controlled player");
    System.out.println("2. 'strategy1' - for a machine player which picks the first open option");
    System.out.println("3. 'strategy2' - for a machine player which maximizes row score");
    //Initializing PawnsBoardGame
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
