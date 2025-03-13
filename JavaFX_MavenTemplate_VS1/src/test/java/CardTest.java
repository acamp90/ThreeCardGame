import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
	
	@Test
    void testCardInitialization() {
        Card card = new Card('H', 12); // Queen of Hearts
        assertEquals('H', card.getSuit());
        assertEquals(12, card.getValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card('D', 10); // 10 of Diamonds
        assertEquals("Card{suit=D, value=10}", card.toString());
    }

}
