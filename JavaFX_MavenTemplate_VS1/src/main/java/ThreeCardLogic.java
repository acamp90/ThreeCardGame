import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {

    // Evaluate the type of hand
    public static int evalHand(ArrayList<Card> hand) {
        validateHand(hand);
        if (isStraightFlush(hand)) return 1; // Straight Flush
        if (isThreeOfAKind(hand)) return 2;  // Three of a Kind
        if (isStraight(hand)) return 3;      // Straight
        if (isFlush(hand)) return 4;         // Flush
        if (isPair(hand)) return 5;          // Pair
        return 0;                            // High Card
    }

    // Calculate Pair Plus winnings based on hand type
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        validateHand(hand);
        int handType = evalHand(hand);
        switch (handType) {
            case 1: return bet * 40; // Straight Flush pays 40 to 1
            case 2: return bet * 30; // Three of a Kind pays 30 to 1
            case 3: return bet * 6;  // Straight pays 6 to 1
            case 4: return bet * 3;  // Flush pays 3 to 1
            case 5: return bet * 1;  // Pair pays 1 to 1
            default: return 0;       // High Card does not pay
        }
    }

    // Compare the dealer's hand with the player's hand
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        validateHand(dealer);
        validateHand(player);

        int dealerHandValue = evalHand(dealer);
        int playerHandValue = evalHand(player);

        if (dealerHandValue < playerHandValue) return 2; // Player wins
        if (playerHandValue < dealerHandValue) return 1; // Dealer wins

        return compareHighCard(dealer, player); // Tie-breaking using high cards
    }

    // Check if the dealer qualifies
    public static boolean dealerQualifies(ArrayList<Card> dealerHand) {
        validateHand(dealerHand);
        ArrayList<Integer> values = getSortedValues(dealerHand);
        return values.get(2) >= 12; // Dealer qualifies with Queen high or better
    }

    // Private helper methods

    private static boolean isStraightFlush(ArrayList<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private static boolean isThreeOfAKind(ArrayList<Card> hand) {
        return hand.get(0).getValue() == hand.get(1).getValue() &&
               hand.get(1).getValue() == hand.get(2).getValue();
    }

    private static boolean isStraight(ArrayList<Card> hand) {
        ArrayList<Integer> values = getSortedValues(hand);
        if (values.get(2) - values.get(1) == 1 && values.get(1) - values.get(0) == 1) {
            return true;
        }
        return values.contains(14) && values.contains(2) && values.contains(3); // Ace-low straight
    }

    private static boolean isFlush(ArrayList<Card> hand) {
        return hand.get(0).getSuit() == hand.get(1).getSuit() &&
               hand.get(1).getSuit() == hand.get(2).getSuit();
    }

    private static boolean isPair(ArrayList<Card> hand) {
        return hand.get(0).getValue() == hand.get(1).getValue() ||
               hand.get(1).getValue() == hand.get(2).getValue() ||
               hand.get(0).getValue() == hand.get(2).getValue();
    }

    private static int compareHighCard(ArrayList<Card> dealer, ArrayList<Card> player) {
        ArrayList<Integer> dealerValues = getSortedValues(dealer);
        ArrayList<Integer> playerValues = getSortedValues(player);

        for (int i = 2; i >= 0; i--) {
            if (dealerValues.get(i) > playerValues.get(i)) return 1; // Dealer wins
            if (playerValues.get(i) > dealerValues.get(i)) return 2; // Player wins
        }
        return 0; // Tie
    }

    private static ArrayList<Integer> getSortedValues(ArrayList<Card> hand) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : hand) {
            values.add(card.getValue());
        }
        Collections.sort(values);
        return values;
    }

    private static void validateHand(ArrayList<Card> hand) {
        if (hand == null || hand.size() != 3) {
            throw new IllegalArgumentException("Hand must contain exactly 3 cards.");
        }
    }
}



