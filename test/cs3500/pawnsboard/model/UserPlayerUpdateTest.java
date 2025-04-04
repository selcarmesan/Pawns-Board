package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import cs3500.pawnsboard.model.mocks.MockUserPlayerSubscriber;
import cs3500.pawnsboard.model.observer.UserPlayerActions;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for user player action updates.
 */
public class UserPlayerUpdateTest {

  private StringBuilder sb;
  private UserPlayerActions up;

  @Before
  public void setUp() {
    sb = new StringBuilder();
    up = new UserPlayer(new PawnsBoardSimple(3, 5), Player.RED);
    up.addListener(new MockUserPlayerSubscriber(sb));
  }

  @Test
  public void testMakeMove() {
    up.makePlay(0, 0, 0);
    assertTrue(sb.toString().contains("0, 0, 0"));
  }

  @Test
  public void testPassTurn() {
    up.skipTurn();
    assertTrue(sb.toString().contains("pass"));
  }

  @Test
  public void testAddListenerThrowsNullListener() {
    assertThrows(IllegalArgumentException.class, () -> up.addListener(null));
  }
}
