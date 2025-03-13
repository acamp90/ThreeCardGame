public class Card {
    private char suit; // 'C' for Clubs, 'D' for Diamonds, 'S' for Spades, 'H' for Hearts
    private int value; // 2-10 for numbered cards, 11 for Jack, 12 for Queen, 13 for King, 14 for Ace

    // Constructor
    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // Getters
    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // Override toString method for serialization
    @Override
    public String toString() {
        return suit + ":" + value; // Format: "C:10", "H:14" (e.g., 10 of Clubs, Ace of Hearts)
    }

    // Static method to convert a String back to a Card
    public static Card fromString(String cardString) {
        try {
            String[] parts = cardString.split(":");
            char suit = parts[0].charAt(0);
            int value = Integer.parseInt(parts[1]);
            return new Card(suit, value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid card format: " + cardString);
        }
    }
}

