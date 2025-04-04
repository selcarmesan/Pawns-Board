package cs3500.pawnsboard;

import java.io.File;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardGUIController;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.controller.PawnsCardReader;
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
    //Check read from config
    File file = new File("docs" + File.separator + "deckRed.config");
    List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file);
    List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file);

    //Initializing PawnsBoardGame
    cs3500.pawnsboard.model.PawnsBoardGame model = new cs3500.pawnsboard.model.PawnsBoardGame(3, 5);
    UserPlayer player1 = new UserPlayer(model, Player.RED, new StrategyFillFirst());
    UserPlayer player2 = new UserPlayer(model, Player.BLUE);
    model.startGame(redDeck, blueDeck, 5, true);
    PawnsBoardVisualView viewPlayer1 = new PawnsBoardVisualView(model, Player.RED);
    PawnsBoardVisualView viewPlayer2 = new PawnsBoardVisualView(model, Player.BLUE);
    PawnsBoardGUIController controller1 = new PawnsBoardGUIController(model, player1, viewPlayer1);
    PawnsBoardGUIController controller2 = new PawnsBoardGUIController(model, player2, viewPlayer2);
  }
}
