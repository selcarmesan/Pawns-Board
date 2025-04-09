package cs3500.exchanged.main;

import cs3500.exchanged.controller.PawnsBoardController;
import cs3500.exchanged.controller.TextDeckReader;
import cs3500.exchanged.model.Card;
import cs3500.exchanged.model.GameModel;
import cs3500.exchanged.model.PawnsBoardModel;
import cs3500.exchanged.model.PlayerColor;
import cs3500.exchanged.players.HumanPlayer;
import cs3500.exchanged.players.IPlayer;
import cs3500.exchanged.players.MachinePlayer;
import cs3500.exchanged.view.PawnsBoardGUI;
import cs3500.exchanged.view.PawnsBoardGUIView;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * The entry point for running the game.
 * Initializes the model, reads the deck configuration,
 * and simulates a full game session using a textual representation.
 */
public class PawnsBoard {
  /**
   * Main method, main entry point of the program.
   * @param args the command line arguments:
   *             args[0] - path to red deck config
   *             args[1] - path to blue deck config
   *             args[2] - red player type ("human" or "machine")
   *             args[3] - blue player type ("human" or "machine")
   */
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length < 4) {
      System.err.println(
            "Usage: java PawnsBoard <redDeckPath> <blueDeckPath> <redPlayerType> <bluePlayerType>");
      System.exit(1);
    }

    // Read decks
    TextDeckReader deckReader = new TextDeckReader();
    List<Card> redDeck = deckReader.readDeck(args[0], PlayerColor.RED);
    List<Card> blueDeck = deckReader.readDeck(args[1], PlayerColor.BLUE);

    // Create model
    PawnsBoardModel model = new GameModel(5, 7, redDeck, blueDeck);

    // Create players based on command line args
    IPlayer redPlayer = createPlayer(args[2], PlayerColor.RED, redDeck);
    IPlayer bluePlayer = createPlayer(args[3], PlayerColor.BLUE, blueDeck);

    // Create view
    PawnsBoardGUIView view = new PawnsBoardGUI(model);

    // Create controllers
    PawnsBoardController redController =
          new PawnsBoardController((GameModel) model, (PawnsBoardGUI) view, redPlayer);
    PawnsBoardController blueController =
          new PawnsBoardController((GameModel) model, (PawnsBoardGUI) view, bluePlayer);

    // Start game
    redController.startGame();
    blueController.startGame();
    model.startGame();

    // Working version of the game:
    // StubPBController controller = new StubPBController((GameModel) model, (PawnsBoardGUI) view);
    // controller.startGame();
  }

  private static IPlayer createPlayer(String playerType, PlayerColor color, List<Card> deck) {
    switch (playerType.toLowerCase()) {
      case "human":
        return new HumanPlayer(color, deck);
      case "machine":
        return new MachinePlayer(color, deck);
      default:
        throw new IllegalArgumentException("Invalid player type: " + playerType);
    }
  }
}