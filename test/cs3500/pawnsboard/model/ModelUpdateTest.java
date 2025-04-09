package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import cs3500.pawnsboard.controller.PawnsCardReader;
import cs3500.pawnsboard.model.mocks.MockModelSubscriber;
import cs3500.pawnsboard.model.observer.ModelUpdateSubscriber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for model observer updates.
 */
public class ModelUpdateTest {

  private StringBuilder sb;
  private ModelUpdateSubscriber listener;
  private PawnsBoardSimple model;

  @Before
  public void setup() {
    sb = new StringBuilder();
    listener = new MockModelSubscriber(sb);
    model = new PawnsBoardSimple(3, 5);
    File file = new File("docs/deckRed.config");
    model.startGame(PawnsCardReader.readCards(Player.RED, file),
            PawnsCardReader.readCards(Player.BLUE, file), 5, false);
  }

  @Test
  public void testModelUpdateGameOverTie() {
    model.addListener(listener, Player.RED);
    model.addListener(listener, Player.BLUE);
    model.skipTurn();
    model.skipTurn();
    assertTrue(sb.toString().contains("Game has ended. Tie game."));
  }

  @Test
  public void testModelUpdateGameOverWinner() {
    model.addListener(listener, Player.RED);
    model.addListener(listener, Player.BLUE);
    model.placeCard(0, 0, 0);
    model.skipTurn();
    model.skipTurn();
    assertTrue(sb.toString().contains("Game has ended. Winner is "));
  }

  @Test
  public void testModelUpdateStartTurnRed() {
    model.addListener(listener, Player.RED);
    model.placeCard(0, 0, 0);
    assertFalse(sb.toString().contains("Your turn has started!"));
    model.skipTurn();
    assertTrue(sb.toString().contains("Your turn has started!"));
  }

  @Test
  public void testModelUpdateStartTurnBlue() {
    model.addListener(listener, Player.BLUE);
    model.placeCard(0, 0, 0);
    assertTrue(sb.toString().contains("Your turn has started!"));
    sb.setLength(0);
    model.skipTurn();
    assertFalse(sb.toString().contains("Your turn has started!"));
  }

  @Test
  public void testModelUpdateAddListenerThrows() {
    assertThrows(IllegalArgumentException.class, () -> model.addListener(null, Player.RED));
    assertThrows(IllegalArgumentException.class, () -> model.addListener(listener, null));
  }
}
