package cs3500.pawnsboard.provider.model;

/**
 * Represents different influence types a card can have on the board.
 */
public enum InfluenceType {
  NONE('X'),      // No effect on the board
  INFLUENCE('I'), // Adds or converts pawns
  CENTER('C');    // Center position of the card

  private final char symbol;

  InfluenceType(char symbol) {
    this.symbol = symbol;
  }

  /**
   * Returns the influence type's symbol.
   * Either X, I, or C.
   * @return the influence type's symbol
   */
  public char getSymbol() {
    return symbol;
  }

  /**
   * Converts a character to its corresponding symbol representing an InfluenceType.
   * @param chr the character to be converted
   * @return the InfluenceType associated with the character
   * @throws IllegalArgumentException if the character does not match any InfluenceType symbols
   */
  public static InfluenceType convertCharToSymbol(char chr) {
    for (InfluenceType type : values()) {
      if (type.symbol == chr) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid influence type: " + chr);
  }
}
