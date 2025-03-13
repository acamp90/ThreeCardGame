import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class ThreeCardLogicTest {

    // Helper method to create a flush or straight flush (all cards have the same suit)
    private ArrayList<Card> createFlushHand(int... values) {
        return new ArrayList<>(Arrays.asList(new Card('H', values[0]), new Card('H', values[1]), new Card('H', values[2])));
    }

    // Helper method to create a straight with different suits
    private ArrayList<Card> createNonFlushStraightHand(int... values) {
        return new ArrayList<>(Arrays.asList(new Card('C', values[0]), new Card('D', values[1]), new Card('S', values[2])));
    }

    @Test
    void testEvalHandStraightFlush() {
        ArrayList<Card> hand = createFlushHand(10, 11, 12); // 10, J, Q of Hearts (Straight Flush)
        assertEquals(1, ThreeCardLogic.evalHand(hand), "Should be a Straight Flush");
    }

    @Test
    void testEvalHandThreeOfAKind() {
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card('D', 9), new Card('H', 9), new Card('S', 9))); // 9, 9, 9
        assertEquals(2, ThreeCardLogic.evalHand(hand), "Should be Three of a Kind");
    }

    @Test
    void testEvalHandStraight() {
        ArrayList<Card> hand = createNonFlushStraightHand(5, 6, 7); // 5, 6, 7 with different suits
        assertEquals(3, ThreeCardLogic.evalHand(hand), "Should be a Straight");
    }

    @Test
    void testEvalHandFlush() {
        ArrayList<Card> hand = createFlushHand(2, 5, 10); // 2, 5, 10 of Hearts (Flush, not a straight)
        assertEquals(4, ThreeCardLogic.evalHand(hand), "Should be a Flush");
    }

    @Test
    void testEvalHandPair() {
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card('C', 7), new Card('D', 7), new Card('H', 5))); // 7, 7, 5
        assertEquals(5, ThreeCardLogic.evalHand(hand), "Should be a Pair");
    }

    @Test
    void testEvalHandHighCard() {
        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card('C', 2), new Card('D', 5), new Card('H', 9))); // 2, 5, 9
        assertEquals(0, ThreeCardLogic.evalHand(hand), "Should be a High Card hand");
    }

    @Test
    void testEvalPPWinnings() {
        ArrayList<Card> hand = createFlushHand(10, 11, 12); // Straight Flush
        assertEquals(200, ThreeCardLogic.evalPPWinnings(hand, 5), "Straight Flush should pay 40 to 1 on Pair Plus");

        hand = new ArrayList<>(Arrays.asList(new Card('D', 9), new Card('H', 9), new Card('S', 9))); // Three of a Kind
        assertEquals(150, ThreeCardLogic.evalPPWinnings(hand, 5), "Three of a Kind should pay 30 to 1 on Pair Plus");

        hand = createNonFlushStraightHand(5, 6, 7); // Straight
        assertEquals(30, ThreeCardLogic.evalPPWinnings(hand, 5), "Straight should pay 6 to 1 on Pair Plus");
    }

    @Test
    void testCompareHandsPlayerWins() {
        ArrayList<Card> dealerHand = createNonFlushStraightHand(5, 6, 7); // Straight
        ArrayList<Card> playerHand = createFlushHand(10, 11, 12); // Straight Flush
        assertEquals(2, ThreeCardLogic.compareHands(dealerHand, playerHand), "Player should win with a Straight Flush over a Straight");
    }

    @Test
    void testCompareHandsDealerWins() {
        ArrayList<Card> dealerHand = new ArrayList<>(Arrays.asList(new Card('C', 9), new Card('D', 9), new Card('H', 9))); // Three of a Kind
        ArrayList<Card> playerHand = createNonFlushStraightHand(5, 6, 7); // Straight
        assertEquals(1, ThreeCardLogic.compareHands(dealerHand, playerHand), "Dealer should win with Three of a Kind over a Straight");
    }

    @Test
    void testCompareHandsTie() {
        ArrayList<Card> dealerHand = createFlushHand(10, 11, 12); // Straight Flush
        ArrayList<Card> playerHand = createFlushHand(10, 11, 12); // Straight Flush
        assertEquals(0, ThreeCardLogic.compareHands(dealerHand, playerHand), "Should be a tie with identical Straight Flush hands");
    }
}

