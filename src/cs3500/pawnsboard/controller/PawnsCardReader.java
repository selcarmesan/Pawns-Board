package cs3500.pawnsboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsCard;
import cs3500.pawnsboard.model.Player;

/**
 * Reads from a config file, and processes its contents into a list of cards based on a
 * particular format.  A card is formatted as 6 separate lines in a text file.  The first
 * specifies name, cost, and value.  The next five each consist of five symbols to denote affected
 * squares with an "I", unaffected with an "X", and the card itself as "C", which must always be
 * in the center of the influence radius.
 */
public class PawnsCardReader {

  /**
   * Returns a deck of cards for the corresponding player, read from the given file.
   * The deck for the blue player has its area of influence mirrored horizontally, but contains the
   * same deck otherwise.
   *
   * @param playerDeck the player whose deck is constructed
   * @param file the file source to read from
   * @return the completed deck
   */
  public static List<Card> readCards(Player playerDeck, File file) {
    if (playerDeck == null || file == null) {
      throw new IllegalArgumentException("Cannot construct deck for null player or file");
    }
    return formatCards(playerDeck, file);
  }

  private static List<Card> formatCards(Player playerDeck, File file) {
    try {
      Scanner scanner = new Scanner(file);
      List<Card> cards = new ArrayList<>();
      while (scanner.hasNextLine()) {
        String name = scanner.next();
        int cost = scanner.nextInt();
        int value = scanner.nextInt();
        scanner.nextLine();
        boolean[][] influence = new boolean[5][5];
        for (int i = 0; i < 5; i++) {
          String line = scanner.nextLine();
          if (line.length() != 5) {
            throw new IllegalArgumentException("Config file formatted incorrectly");
          }
          for (int j = 0; j < 5; j++) {
            int j2 = j;
            if (playerDeck == Player.BLUE) {
              j2 = 4 - j;
            }
            char c = line.charAt(j2);
            if (c != 'I' && c != 'X' && c != 'C') {
              throw new IllegalArgumentException("Config file formatted incorrectly");
            }
            influence[i][j] = c == 'I' || c == 'C';
            if (i == 2 && c != 'C' && j2 == 2) {
              throw new IllegalStateException("Config file formatted incorrectly");
            }
          }
        }
        cards.add(new PawnsCard(name, cost, value, influence));
      }
      return cards;
    } catch (NoSuchElementException | IOException e) {
      throw new IllegalArgumentException("Error reading config file, fix format and try again");
    }
  }
}
