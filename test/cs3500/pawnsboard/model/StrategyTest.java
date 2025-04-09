package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import cs3500.pawnsboard.model.mocks.MockBoardChecking;
import cs3500.pawnsboard.model.mocks.MockBoardOneValid;
import cs3500.pawnsboard.model.mocks.MockBoardRowScore;
import cs3500.pawnsboard.model.strategies.Move;
import cs3500.pawnsboard.model.strategies.PawnsBoardStrategy;
import cs3500.pawnsboard.model.strategies.StrategyFillFirst;
import cs3500.pawnsboard.model.strategies.StrategyMaximizeRowScore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for strategies.
 */
public class StrategyTest {

  PawnsBoardStrategy fillFirst;
  PawnsBoardStrategy maxRowScore;
  PawnsBoardReadOnly model;
  StringBuilder sb;

  @Before
  public void setUp() {
    fillFirst = new StrategyFillFirst();
    maxRowScore = new StrategyMaximizeRowScore();
    sb = new StringBuilder();
  }

  @Test
  public void testFillFirstChecksAllSpots() {
    model = new MockBoardChecking(sb);
    try {
      fillFirst.choosePlay(model, Player.RED);
    } catch (Exception ignored) {

    }
    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getCols(); j++) {
        for (int k = 0; k < model.getHand(Player.RED).size(); k++) {
          assertTrue(sb.toString().contains("Checked move: " + i + ", " + j + ", " + k));
        }
      }
    }
  }

  @Test
  public void testFillFirstReturnsNullWhenNoSpaces() {
    model = new MockBoardChecking(sb);
    assertThrows(IllegalStateException.class, () -> fillFirst.choosePlay(model, Player.RED));
  }

  @Test
  public void testFillFirstWorksDesignatedSpot() {
    // Mock set up to only show valid move at 1, 1, 1
    model = new MockBoardOneValid(sb);
    Move move = fillFirst.choosePlay(model, Player.RED);
    assertEquals(1, move.row);
    assertEquals(1, move.col);
    assertEquals(1, move.handIndex);
  }

  @Test
  public void testFillFirstStopsAfterFound() {
    model = new MockBoardOneValid(sb);
    fillFirst.choosePlay(model, Player.RED);
    assertTrue(sb.toString().contains("Checked move: 0, 2, 2"));
    assertTrue(sb.toString().contains("Checked move: 1, 1, 0"));
    assertTrue(sb.toString().contains("Checked move: 1, 1, 1"));
    assertFalse(sb.toString().contains("Checked move: 1, 1, 2"));
    assertFalse(sb.toString().contains("Checked move: 1, 2, 0"));
    assertFalse(sb.toString().contains("Checked move: 2, 0, 0"));
  }

  @Test
  public void testMaxRowScoreThrowsNoAnswer() {
    model = new MockBoardRowScore(sb);
    assertThrows(IllegalStateException.class, () -> maxRowScore.choosePlay(model, Player.BLUE));
  }

  @Test
  public void testMaxRowScoreChecksAllSpots() {
    model = new MockBoardOneValid(sb);
    try {
      maxRowScore.choosePlay(model, Player.RED);
    } catch (Exception ignored) {

    }
    for (int i = 0; i < model.getRows(); i++) {
      for (int j = 0; j < model.getCols(); j++) {
        for (int k = 0; k < model.getHand(Player.RED).size(); k++) {
          assertTrue(sb.toString().contains("Checked move: " + i + ", " + j + ", " + k));
        }
      }
    }
  }

  @Test
  public void testMaxRowScoreReturnsProperMove() {
    model = new MockBoardRowScore(sb);
    // Row 2 is only row in which playing a card could pass blue's score within mock
    // Second card in deck is only one which allows overtaking score
    // All moves are valid, so first available one is returned
    Move move = maxRowScore.choosePlay(model, Player.RED);
    assertEquals(2, move.row);
    assertEquals(0, move.col);
    assertEquals(1, move.handIndex);
  }

  @Test
  public void testMaxRowScoreStopsAfterFound() {
    model = new MockBoardRowScore(sb);
    maxRowScore.choosePlay(model, Player.RED);
    assertTrue(sb.toString().contains("Checked move: 0, 2, 2"));
    assertTrue(sb.toString().contains("Checked move: 1, 2, 2"));
    assertTrue(sb.toString().contains("Checked move: 2, 0, 0"));
    assertTrue(sb.toString().contains("Checked move: 2, 0, 1"));
    assertFalse(sb.toString().contains("Checked move: 2, 0, 2"));
    assertFalse(sb.toString().contains("Checked move: 2, 2, 0"));
  }
}
