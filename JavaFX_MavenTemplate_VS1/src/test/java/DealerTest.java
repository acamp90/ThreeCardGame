import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


public class DealerTest {
	
	private Dealer dealer;

    @BeforeEach
    void setUp() {
        dealer = new Dealer();
    }

    @Test
    void testDealHandSize() {
        ArrayList<Card> hand = dealer.dealHand();
        assertEquals(3, hand.size(), "The dealer's hand should contain exactly 3 cards after dealing");
    }

    @Test
    void testDeckReshufflingWhenLowCards() {
        for (int i = 0; i < 18; i++) { // 18 deals (18 * 3 cards) = 54 cards, requiring reshuffle
            dealer.dealHand();
        }
        ArrayList<Card> hand = dealer.dealHand();
        assertEquals(3, hand.size(), "Dealer should still deal 3 cards after reshuffling the deck");
    }

    @Test
    void testGetDealersHand() {
        dealer.dealHand();
        ArrayList<Card> hand = dealer.getDealersHand();
        assertEquals(3, hand.size(), "Dealer's hand should have 3 cards after dealing");
    }

    @Test
    void testDealersHandNotSameObject() {
        dealer.dealHand();
        ArrayList<Card> hand = dealer.getDealersHand();
        ArrayList<Card> anotherHand = dealer.getDealersHand();
        assertNotSame(hand, anotherHand, "Each call to getDealersHand should return a new ArrayList instance");
    }

}
