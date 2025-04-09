package cs3500.pawnsboard.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for Cell.
 */
public class CellTest {

  private Cell cell;
  private Card card;

  @Before
  public void setUp() {
    cell = new BoardCell();
    card = new PawnsCard("test", 1, 1, new boolean[5][5]);
  }

  @Test
  public void testCellConstructsProperly() {
    cell = new BoardCell();
    assertEquals(0, cell.getPawns());
  }

  @Test
  public void testGetAndChangeOwner() {
    assertNull(cell.getOwner());
    cell.addPawn(Player.RED);
    cell.changeOwner(Player.BLUE);
    assertEquals(Player.BLUE, cell.getOwner());
    cell.changeOwner(Player.RED);
    assertEquals(Player.RED, cell.getOwner());
  }

  @Test
  public void testGetAndAddPawns() {
    assertEquals(0, cell.getPawns());
    cell.addPawn(Player.RED);
    assertEquals(1, cell.getPawns());
    cell.addPawn();
    assertEquals(2, cell.getPawns());
    cell.addPawn();
    assertEquals(3, cell.getPawns());
    cell.addPawn();
    assertEquals(3, cell.getPawns());
  }

  @Test
  public void testAddPawnOnlyWorksWithCorrectCall() {
    assertThrows(IllegalArgumentException.class, () -> cell.addPawn());
    cell.addPawn(Player.RED);
    assertThrows(IllegalArgumentException.class, () -> cell.addPawn(Player.RED));
  }

  @Test
  public void testAddPawnThrowsWhenCardPlayed() {
    cell.playCard(card, Player.BLUE);
    assertThrows(IllegalStateException.class, () -> cell.addPawn(Player.RED));
    cell = new BoardCell();
    cell.addPawn(Player.RED);
    cell.playCard(card, Player.BLUE);
    assertThrows(IllegalStateException.class, () -> cell.addPawn());
  }

  @Test
  public void testAddPawnThrowsWithNullPlayer() {
    assertThrows(IllegalArgumentException.class, () -> cell.addPawn(null));
  }

  @Test
  public void testChangeOwnerThrowsWhenCellIsEmpty() {
    assertThrows(IllegalArgumentException.class, () -> cell.changeOwner(Player.BLUE));
  }

  @Test
  public void testChangeOwnerThrowsWhenPlayerIsNull() {
    cell.addPawn(Player.RED);
    assertThrows(IllegalArgumentException.class, () -> cell.changeOwner(null));
  }

  @Test
  public void testChangeOwnerThrowsAfterCardIsPlayed() {
    cell.playCard(card, Player.BLUE);
    assertThrows(IllegalStateException.class, () -> cell.changeOwner(Player.BLUE));
  }

  @Test
  public void testPlayAndGetCard() {
    cell.playCard(card, Player.BLUE);
    assertEquals(card, cell.getCard());
  }

  @Test
  public void testReturnedCardIsCopy() {
    cell.playCard(card, Player.BLUE);
    Card card2 = cell.getCard();
    assertEquals(card2, card);
    assertNotSame(card2, card);
  }

  @Test
  public void testPlayCardThrowsIfCardPlayedAlready() {
    cell.playCard(card, Player.BLUE);
    assertThrows(IllegalStateException.class, () -> cell.playCard(card, Player.BLUE));
  }

  @Test
  public void testPlayCardThrowsWhenGivenNullCard() {
    assertThrows(IllegalArgumentException.class, () -> cell.playCard(null, Player.BLUE));
  }

  @Test
  public void testPlayCardThrowsWhenGivenNullPlayer() {
    assertThrows(IllegalArgumentException.class, () -> cell.playCard(card, null));
  }
}
