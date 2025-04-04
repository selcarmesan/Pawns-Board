package cs3500.pawnsboard;

import java.io.File;
import java.util.List;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

/**
 * PawnsBoard creates a new game of PawnsBoardGame and plays till it is completed.
 */
public class PawnsBoard {

  /**
   * Creates a new PawnsBoardGame game.
   * Runs the new game with predetermined moves until completion.
   */
  public static void main(String[] args) {
    //Check read from deck.config
    File file = new File("docs" + File.separator + "deckRed.config");
    List<Card> redDeck = PawnsCardReader.readCards(Player.RED, file);
    List<Card> blueDeck = PawnsCardReader.readCards(Player.BLUE, file);

    //Initializing PawnsBoardGame
    PawnsBoardSimple game = new PawnsBoardSimple(3, 5);
    game.startGame(redDeck, blueDeck, 5, false);

    //Initializing PawnsBoardTextualView
    PawnsBoardTextualView view = new PawnsBoardTextualView(game);

    //One Full Game of PawnsBoardGame
    game.placeCard(0, 0, 2);
    System.out.println(view);
    game.placeCard(0, 4, 0);
    System.out.println(view);
    game.placeCard(1, 0, 0);
    System.out.println(view);
    game.placeCard(0, 2, 1);
    System.out.println(view);
    game.placeCard(1, 1, 0);
    System.out.println(view);
    game.placeCard(1, 2, 3);
    System.out.println(view);
    game.placeCard(2, 0, 1);
    System.out.println(view);
    game.placeCard(0, 1, 2);
    System.out.println(view);
    game.placeCard(2, 2, 2);
    System.out.println(view);
    game.placeCard(2, 1, 3);
    System.out.println(view);
    game.placeCard(1, 3, 1);
    System.out.println(view);
    game.placeCard(0, 3, 4);
    System.out.println(view);
    game.skipTurn();
    System.out.println(view);
    game.placeCard(1, 4, 0);
    System.out.println(view);
    game.skipTurn();
    System.out.println(view);
    game.placeCard(2, 4, 0);
    System.out.println(view);
    game.skipTurn();
    System.out.println(view);
    game.placeCard(2, 3, 0);
    System.out.println(view);
    game.skipTurn();
    System.out.println(view);
    game.skipTurn();
    System.out.println(view);

    //Check If Game is Over
    System.out.println(game.isGameOver());

    //Check Score
    System.out.println(game.getTotalScore(Player.RED));
    System.out.println(game.getTotalScore(Player.BLUE));
  }

}
