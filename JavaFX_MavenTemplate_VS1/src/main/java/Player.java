import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Card> hand; // Player's hand
    private int anteBet;          // Ante bet placed by the player
    private int playBet;          // Play bet (must match ante bet if playing)
    private int pairPlusBet;      // Optional Pair Plus bet
    private int totalWinnings;    // Total winnings accumulated by the player

    // Constructor
    public Player() {
        hand = new ArrayList<>();
        anteBet = 0;
        playBet = 0;
        pairPlusBet = 0;
        totalWinnings = 0;
    }

    // Accessor methods
    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getAnteBet() {
        return anteBet;
    }

    public int getPlayBet() {
        return playBet;
    }

    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public int getTotalWinnings() {
        return totalWinnings;
    }

    // Mutator methods
    public void setAnteBet(int bet) {
        this.anteBet = bet;
    }

    public void setPairPlusBet(int bet) {
        this.pairPlusBet = bet;
    }

    public void setPlayBet() {
        this.playBet = this.anteBet; // Play bet matches ante bet when the player chooses to play
    }

    // Method to explicitly set total winnings
    public void setTotalWinnings(int winnings) {
        this.totalWinnings = winnings;
    }

    // Method to increment total winnings
    public void updateTotalWinnings(int winnings) {
        this.totalWinnings += winnings;
    }

    // Method to reset player state for a new game
    public void reset() {
        hand.clear();     // Clear the player's hand
        anteBet = 0;      // Reset the ante bet
        playBet = 0;      // Reset the play bet
        pairPlusBet = 0;  // Reset the Pair Plus bet
    }

    // Method to add a card to the player's hand
    public void addCard(Card card) {
        hand.add(card);
    }

    // Method to return a string representation of the player's state
    @Override
    public String toString() {
        return String.format(
            "Hand: %s\nAnte Bet: $%d\nPlay Bet: $%d\nPair Plus Bet: $%d\nTotal Winnings: $%d",
            hand, anteBet, playBet, pairPlusBet, totalWinnings
        );
    }
}


