import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DeckTest {
	
	private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testDeckSize() {
        assertEquals(52, deck.size(), "Deck should start with 52 cards");
    }

    @Test
    void testDeckShuffling() {
        Deck anotherDeck = new Deck();
        assertNotEquals(deck.toString(), anotherDeck.toString(), "Two newly shuffled decks should not be identical");
    }

    @Test
    void testNewDeckResetsSize() {
        deck.drawCard(); // Remove one card
        assertEquals(51, deck.size(), "Deck size should decrease after drawing a card");
        deck.newDeck(); // Reset deck
        assertEquals(52, deck.size(), "New deck should reset size back to 52 cards");
    }

    @Test
    void testDrawCardReducesDeckSize() {
        int initialSize = deck.size();
        deck.drawCard();
        assertEquals(initialSize - 1, deck.size(), "Drawing a card should reduce deck size by 1");
    }

    @Test
    void testDrawCardReturnsNullWhenEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.drawCard(); // Draw all cards
        }
        assertNull(deck.drawCard(), "Drawing from an empty deck should return null");
    }

}
