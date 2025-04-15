package cs3500.pawnsboard;

import java.io.File;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardGUIController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.UserPlayer;
import cs3500.pawnsboard.model.adapter.PawnsBoardAdapt;
import cs3500.pawnsboard.model.strategies.StrategyFillFirst;
import cs3500.pawnsboard.model.strategies.StrategyMaximizeRowScore;
import cs3500.pawnsboard.provider.view.PawnsBoardGUI;
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
    //Initializing PawnsBoardGame
    try {
      File file1 = new File(args[0]);
      File file2 = new File(args[1]);
      List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file1);
      List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file2);
      PawnsBoardSimple model = new PawnsBoardSimple(3, 5);
      PawnsBoardAdapt providerModel = new PawnsBoardAdapt(model, redDeck, blueDeck);
      UserPlayer player1 = createPlayer(model, Player.RED, args[2]);
      UserPlayer player2 = createPlayer(model, Player.BLUE, args[3]);
      if (player1 == null || player2 == null) {
        System.out.println("Invalid commands chosen");
        return;
      }
      model.startGame(redDeck, blueDeck, 5, true);
      PawnsBoardVisualView view1 = new PawnsBoardVisualView(model, Player.RED);
      PawnsBoardGUI providerView = new PawnsBoardGUI(providerModel);
      providerView.makeVisible();
      PawnsBoardGUIController controller1 = new PawnsBoardGUIController(model, player1, view1);
      //PawnsBoardGUIController providerController = new PawnsBoardGUIController(model, player2, providerView);
    } catch (Exception e) {
      System.out.println("Incorrect entering of parameters");
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
