package cs3500.pawnsboard.provider.controller;

import cs3500.pawnsboard.provider.model.CardCost;
import cs3500.pawnsboard.provider.model.PlayerColor;
import cs3500.pawnsboard.provider.model.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Reads deck configuration files and parses them into card objects for the game.
 * Ensures that the deck follows the required format.
 */
public class TextDeckReader {

  /**
   * Method creates decks from reading card configurations from files.
   * @param filePath the file path
   * @param deckOwner which player will own this deck
   * @return the player's deck
   * @throws FileNotFoundException if the file path is not found
   */
  public List<Card> readDeck(String filePath, PlayerColor deckOwner) throws FileNotFoundException {
    List<Card> deck = new ArrayList<>();
    Map<String, Integer> cardCounts = new HashMap<>();

    File file = new File(filePath);
    Scanner scanner = new Scanner(file);

    while (scanner.hasNextLine()) {
      // Read card name, cost, and value
      String name = scanner.next();
      int costValue = scanner.nextInt();
      CardCost cost = null;
      for (CardCost c : CardCost.values()) {
        if (c.getNumCost() == costValue) {
          cost = c;
          break;
        }
      }
      if (cost == null) {
        throw new IllegalArgumentException("Invalid card cost in deck file: " + name);
      }
      int valueScore = scanner.nextInt();
      scanner.nextLine(); // Move to the next line

      // Read the influence grid (5 lines)
      String[] influenceGrid = new String[5];
      for (int i = 0; i < 5; i++) {
        if (!scanner.hasNextLine()) {
          throw new IllegalArgumentException("Invalid deck file: missing influence grid.");
        }
        influenceGrid[i] = scanner.nextLine();
      }

      // Validate the influence grid
      Card.validateInfluenceGrid(influenceGrid);

      // Create the card and add it to the deck
      Card card = new Card(name, cost, valueScore, influenceGrid, deckOwner);
      cardCounts.put(name, cardCounts.getOrDefault(name, 0) + 1);
      if (cardCounts.get(name) > 2) {
        throw new IllegalArgumentException("Deck cannot contain more than 2 copies of any card.");
      }
      deck.add(card);
    }

    scanner.close();
    return deck;
  }
}