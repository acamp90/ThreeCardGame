import java.util.ArrayList;

public class Dealer {
    private Deck theDeck;
    private ArrayList<Card> dealersHand;

    public Dealer() {
        theDeck = new Deck();
        dealersHand = new ArrayList<>();
    }

    public ArrayList<Card> dealHand() {
        if (theDeck.size() <= 34) { // Reshuffle if the deck has 34 or fewer cards left
            theDeck.newDeck();
        }
        dealersHand.clear();
        for (int i = 0; i < 3; i++) {
            dealersHand.add(theDeck.drawCard()); // Use drawCard() to get cards from Deck
        }
        return new ArrayList<>(dealersHand); // Return a copy to prevent external modification
    }

    public ArrayList<Card> getDealersHand() {
        return new ArrayList<>(dealersHand); // Return a copy to preserve encapsulation
    }
}
