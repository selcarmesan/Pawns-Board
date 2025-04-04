package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import cs3500.pawnsboard.model.mocks.MockStrategyFirst;
import cs3500.pawnsboard.model.mocks.MockStrategyInvalid;
import cs3500.pawnsboard.model.mocks.MockUserPlayerSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActionSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for User Player
 */
public class UserPlayerTest {

  private UserPlayer player;
  private PawnsBoard model = new PawnsBoardSimple(3, 5);
  private StringBuilder sb;
  private UserPlayerActionSubscriber listener;

  @Before
  public void setUp() {
    sb = new StringBuilder();
    listener = new MockUserPlayerSubscriber(sb);
  }

  @Test
  public void testConstructorThrows() {
    assertThrows(IllegalArgumentException.class, () -> new UserPlayer(null, Player.RED));
    assertThrows(IllegalArgumentException.class, () -> new UserPlayer(model, null));
  }

  @Test
  public void testDecideMoveHumanPlayer() {
    player = new UserPlayer(model, Player.BLUE);
    assertThrows(IllegalStateException.class, () -> player.decideMove());
  }

  @Test
  public void testDecideMove() {
    player = new UserPlayer(model, Player.BLUE, new MockStrategyFirst());
    player.addListener(listener);
    player.decideMove();
    assertTrue(sb.toString().contains("0, 0, 0"));
  }

  @Test
  public void testSkipMove() {
    player = new UserPlayer(model, Player.BLUE, new MockStrategyInvalid());
    player.addListener(listener);
    player.decideMove();
    assertTrue(sb.toString().contains("pass"));
  }

  @Test
  public void testGetPlayer() {
    player = new UserPlayer(model, Player.RED);
    assertEquals(Player.RED, player.getThisPlayer());
    player = new UserPlayer(model, Player.BLUE);
    assertEquals(Player.BLUE, player.getThisPlayer());
  }

  @Test
  public void testIsMachine() {
    player = new UserPlayer(model, Player.BLUE);
    assertFalse(player.isMachine());
    player = new UserPlayer(model, Player.RED, new MockStrategyInvalid());
    assertTrue(player.isMachine());
  }

}
