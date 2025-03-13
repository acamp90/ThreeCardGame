import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        newDeck();
    }

    public void newDeck() {
        cards.clear(); // Clears the internal list of cards
        char[] suits = {'C', 'D', 'S', 'H'};
        for (char suit : suits) {
            for (int value = 2; value <= 14; value++) {
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.isEmpty() ? null : cards.remove(0); // Accessing `remove` on `cards`
    }

    public int size() {
        return cards.size(); // Accessing `size` on `cards`
    }

    @Override
    public String toString() {
        return cards.toString(); // Accessing `toString` on `cards`
    }
}

