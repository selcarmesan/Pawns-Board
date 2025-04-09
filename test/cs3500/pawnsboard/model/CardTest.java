package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Card.
 */
public class CardTest {

  private Card card;

  @Before
  public void setUp() {
    card = new PawnsCard("test", 1, 1, new boolean[5][5]);
  }

  @Test
  public void testCardConstructs() {
    boolean[][] grid = new boolean[5][5];
    grid[0][1] = true;
    card = new PawnsCard("test", 1, 1, grid);
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (row == 0 && col == 1) {
          assertTrue(card.getInfluence()[row][col]);
        } else {
          assertFalse(card.getInfluence()[row][col]);
        }
      }
    }
  }

  @Test
  public void testConstructThrowsNullNameInfluence() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard(null, 1, 1, new boolean[5][5]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, null));
  }

  @Test
  public void testConstructThrowsInvalidCost() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 0, 1, new boolean[5][5]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 5, 1, new boolean[5][5]));
  }

  @Test
  public void testConstructThrowsInvalidValue() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 0, new boolean[5][5]));
  }

  @Test
  public void testConstructThrowsInvalidInfluenceGrid() {
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, new boolean[3][3]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, new boolean[5][3]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, new boolean[3][5]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, new boolean[6][5]));
    assertThrows(IllegalArgumentException.class,
        () -> new PawnsCard("test", 1, 1, new boolean[5][6]));
  }

  @Test
  public void testConstructorCopiesInfluence() {
    boolean[][] influence = new boolean[5][5];
    card = new PawnsCard("test", 1, 1, influence);
    assertFalse(card.getInfluence()[0][0]);
    influence[0][0] = true;
    assertFalse(card.getInfluence()[0][0]);
  }

  @Test
  public void testGetName() {
    assertEquals("test", card.getName());
  }

  @Test
  public void testGetCost() {
    assertEquals(1, card.getCost());
  }

  @Test
  public void testGetValue() {
    assertEquals(1, card.getValue());
  }

  @Test
  public void testGetInfluence() {
    assertEquals(5, card.getInfluence().length);
    assertEquals(5, card.getInfluence()[0].length);
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        assertFalse(card.getInfluence()[i][j]);
      }
    }
  }

  @Test
  public void testGetInfluenceReturnsCopy() {
    boolean[][] grid = card.getInfluence();
    grid[0][0] = true;
    assertFalse(card.getInfluence()[0][0]);
  }

  @Test
  public void testEquals() {
    assertEquals(card, card);
    boolean[][] grid = new boolean[5][5];
    Card card2 = new PawnsCard("test", 1, 1, grid);
    assertEquals(card, card2);
    Card cardName = new PawnsCard("tes", 1, 1, grid);
    assertNotEquals(card, cardName);
    Card cardCost = new PawnsCard("test", 2, 1, grid);
    assertNotEquals(card, cardCost);
    Card cardValue =  new PawnsCard("test", 1, 2, grid);
    assertNotEquals(card, cardValue);
    grid[0][0] = true;
    Card cardInfluence = new PawnsCard("test", 1, 1, grid);
    assertNotEquals(cardInfluence, card);
    assertNotEquals(new BoardCell(), card);
  }

  @Test
  public void testHashCode() {
    assertEquals(card.hashCode(), card.hashCode());
    boolean[][] grid = new boolean[5][5];
    Card card2 = new PawnsCard("test", 1, 1, grid);
    assertEquals(card.hashCode(), card2.hashCode());
    Card cardName = new PawnsCard("tes", 1, 1, grid);
    assertNotEquals(card.hashCode(), cardName.hashCode());
    Card cardCost = new PawnsCard("test", 2, 1, grid);
    assertNotEquals(card.hashCode(), cardCost.hashCode());
    Card cardValue =  new PawnsCard("test", 1, 2, grid);
    assertNotEquals(card.hashCode(), cardValue.hashCode());
    grid[0][0] = true;
    Card cardInfluence = new PawnsCard("test", 1, 1, grid);
    assertNotEquals(card.hashCode(), cardInfluence.hashCode());
  }
}
