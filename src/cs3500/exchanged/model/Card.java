package cs3500.exchanged.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a card in the game. Each card has a name, a cost (in pawns),
 * a value score, and a 5x5 influence grid that affects the board when placed.
 */
public class Card {
  private final String name;
  private final CardCost cost;
  private final int valueScore;
  private final InfluenceType[][] influenceGrid; // 5x5 grid represented as 5 strings
  private final PlayerColor owner;

  /**
   * Constructor for card.
   *
   * @param name          the name of the card.
   * @param cost          the cost of a card.
   * @param value    the score.
   * @param influenceGrid grid description as 5 strings.
   * @throws IllegalArgumentException if any parameters are invalid.
   */
  public Card(String name, CardCost cost, int value, String[] influenceGrid, PlayerColor owner) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Card name cannot be null or empty");
    }
    if (value <= 0) {
      throw new IllegalArgumentException("Card value score cannot be negative");
    }
    validateInfluenceGrid(influenceGrid);

    this.name = name;
    this.cost = cost;
    this.valueScore = value;
    this.influenceGrid = parseInfluenceGrid(influenceGrid);
    this.owner = owner;
  }

  /**
   * Copy of original Card class constructor.
   * Allows for deep copying of Card objects to prevent mutations.
   * @param other the original card to be copied
   */
  public Card(Card other) {
    this.name = other.name;
    this.cost = other.cost;
    this.valueScore = other.valueScore;
    this.influenceGrid = other.influenceGrid.clone(); // Copy the grid
    this.owner = other.owner;
  }


  // Checks the grid's validity and converts it from a string to InfluenceType.
  private InfluenceType[][] parseInfluenceGrid(String[] grid) {
    validateInfluenceGrid(grid);
    InfluenceType[][] parsedGrid = new InfluenceType[5][5];

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        parsedGrid[i][j] = InfluenceType.convertCharToSymbol(grid[i].charAt(j));
      }
    }
    return parsedGrid;
  }

  /**
   * Returns the card's name.
   * @return the card's name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the card's cost in pawns.
   * @return the card's cost
   */
  public CardCost getCost() {
    return cost;
  }

  /**
   * Returns the card's value score.
   * @return the card's value score
   */
  public int getValueScore() {
    return valueScore;
  }

  /**
   * Returns the card's 5x5 influence grid description.
   * @return the card's influence grid
   */
  public InfluenceType[][] getInfluenceGrid() {
    return influenceGrid;
  }

  /**
   * Returns the card's owner, meaning the player who has it.
   * @return the player who owns the card - RED or BLUE
   */
  public PlayerColor getOwner() {
    return owner;
  }

  /**
   * Validates the influence grid of a card before assigning it.
   * Ensures that it has exactly 5 lines, 5 characters, and C in the middle row.
   * @param influenceGrid the influence grid to check
   */
  public static void validateInfluenceGrid(String[] influenceGrid) {
    // Check that the grid has exactly 5 lines
    if (influenceGrid.length != 5) {
      throw new IllegalArgumentException("Influence grid must have exactly 5 lines.");
    }

    // Check that each line has exactly 5 characters
    for (String line : influenceGrid) {
      if (line.length() != 5) {
        throw new IllegalArgumentException(
              "Each line in the influence grid must have exactly 5 characters.");
      }
    }

    // Check that the grid contains exactly one 'C' in the middle (row 2, column 2)
    if (influenceGrid[2].charAt(2) != 'C') {
      throw new IllegalArgumentException(
            "The influence grid must have a 'C' in the middle (row 2, column 2).");
    }
  }

  /**
   * Two cards are the same if they have the same name,
   * cost, value score, and influence grid.
   * @param obj the other object to be compared with this object
   * @return true if both cards have the same name, cost, score, and influence grid,
   *         false if otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Card)) {
      return false;
    }
    Card card = (Card) obj;
    return cost == card.cost && valueScore == card.valueScore
          && name.equals(card.name) && Arrays.deepEquals(influenceGrid, card.influenceGrid);
  }

  /**
   * Returns this Card's hashcode.
   * Consider the card's name, cost, value score, and influence grid.
   * @return the card's hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(name, cost, valueScore, Arrays.deepHashCode(influenceGrid));
  }
}