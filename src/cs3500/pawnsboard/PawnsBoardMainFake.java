package cs3500.pawnsboard;

import java.io.File;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsBoardGUIAdapterController;
import cs3500.pawnsboard.controller.PawnsBoardGUIController;
import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.UserPlayer;
import cs3500.pawnsboard.model.adapter.PawnsBoardAdapt;
import cs3500.pawnsboard.provider.view.PawnsBoardGUI;
import cs3500.pawnsboard.view.PawnsBoardVisualView;

public class PawnsBoardMainFake {

  public static void main(String[] args) {
//    try {
      File file1 = new File("docs/deckRed.config");
      File file2 = new File("docs/deckBlue.config");
      List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file1);
      List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file2);
      PawnsBoardSimple model = new PawnsBoardSimple(3, 5);
      PawnsBoardAdapt providerModel = new PawnsBoardAdapt(model, redDeck, blueDeck);
      UserPlayer player1 = new UserPlayer(model, Player.RED);
      UserPlayer player2 = new UserPlayer(model, Player.BLUE);
      model.startGame(redDeck, blueDeck, 5, true);
      PawnsBoardVisualView view1 = new PawnsBoardVisualView(model, Player.RED);
      PawnsBoardGUI providerView = new PawnsBoardGUI(providerModel);
      providerView.makeVisible();
      PawnsBoardGUIController controller1 = new PawnsBoardGUIController(model, player1, view1);
      PawnsBoardGUIAdapterController controller2 =
              new PawnsBoardGUIAdapterController(model, player2, providerView);
//    } catch (Exception e) {
//      System.out.println("Incorrect entering of parameters");
//    }
  }
}
