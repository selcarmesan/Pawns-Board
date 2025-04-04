package cs3500.pawnsboard.view;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.PawnsBoardSimple;
import cs3500.pawnsboard.model.PawnsCard;
import cs3500.pawnsboard.model.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for PawnsBoardView.
 */
public class PawnsBoardTextualViewTest {

  PawnsBoard board;
  List<Card> redCards;
  List<Card> redConfigDeck;
  List<Card> blueCards;
  List<Card> blueConfigDeck;

  @Before
  public void setUp() {
    board = new PawnsBoardSimple(2, 3);
    boolean[][] testGrid = new boolean[5][5];
    testGrid[2][2] = true;
    Card test1Red = new PawnsCard("test1", 1, 1, testGrid);
    Card test1Blue = new PawnsCard("test1", 1, 1, testGrid);
    Card test2Red = new PawnsCard("test2", 1, 1, testGrid);
    Card test2Blue = new PawnsCard("test2", 1, 1, testGrid);
    Card test3Red = new PawnsCard("test3", 1, 1, testGrid);
    Card test3Blue = new PawnsCard("test3", 1, 1, testGrid);
    redCards = new ArrayList<>();
    redCards.add(test1Red);
    redCards.add(test1Red);
    redCards.add(test2Red);
    redCards.add(test2Red);
    redCards.add(test3Red);
    redCards.add(test3Red);
    blueCards = new ArrayList<>();
    blueCards.add(test1Blue);
    blueCards.add(test1Blue);
    blueCards.add(test2Blue);
    blueCards.add(test2Blue);
    blueCards.add(test3Blue);
    blueCards.add(test3Blue);
    File fileRed = new File("docs" + File.separator + "deckRed.config");
    File fileBlue = new File("docs" + File.separator + "deckBlue.config");
    redConfigDeck = PawnsCardReader.readCards(Player.RED, fileRed);
    blueConfigDeck = PawnsCardReader.readCards(Player.BLUE, fileBlue);
  }

  @Test
  public void PawnsBoardTextualView() {
    //Invalid Constructor
    assertThrows("Invalid Model", IllegalArgumentException.class,
        () -> new PawnsBoardTextualView(null)
    );

    //Correct Implementation
    PawnsBoardView correct = new PawnsBoardTextualView(board);
    assertEquals(board, correct.getModel());
  }

  @Test
  public void testToStringEmpty() {
    board.startGame(redCards, blueCards, 1, false);
    PawnsBoardView view = new PawnsBoardTextualView(board);
    String emptyExpected = "0 1_1 0\n"
            + "0 1_1 0\n";
    assertEquals(emptyExpected, view.toString());
  }

  @Test
  public void testToStringCardPlaced() {
    board.startGame(redCards, blueCards, 1, false);
    PawnsBoardView view = new PawnsBoardTextualView(board);
    board.placeCard(0, 0, 0);
    String cardExpected = "1 R_1 0\n"
            + "0 1_1 0\n";
    assertEquals(cardExpected, view.toString());
    board.placeCard(1, 2, 0);
    cardExpected = "1 R_1 0\n"
            + "0 1_B 1\n";
    assertEquals(cardExpected, view.toString());
  }
}