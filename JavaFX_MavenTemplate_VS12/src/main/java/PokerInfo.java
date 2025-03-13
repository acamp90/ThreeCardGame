import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PokerInfo implements Serializable {
    private String requestType;
    private int anteBet;
    private int pairPlusBet;
    private int playBet;
    private int winnings;
    private List<String> playerHand; // Store cards as strings
    private List<String> dealerHand;
    private String message;

    public PokerInfo(String requestType) {
        this.requestType = requestType;
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
    }

    // Getters and Setters
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getAnteBet() {
        return anteBet;
    }

    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    public int getPairPlusBet() {
        return pairPlusBet;
    }

    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    public int getPlayBet() {
        return playBet;
    }

    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    public int getWinnings() {
        return winnings;
    }

    public void setWinnings(int winnings) {
        this.winnings = winnings;
    }

    public List<String> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(List<Card> cards) {
        playerHand.clear();
        for (Card card : cards) {
            playerHand.add(card.toString()); // Convert Card to String
        }
    }

    public ArrayList<Card> getPlayerHandAsCards() {
        ArrayList<Card> cards = new ArrayList<>();
        for (String cardString : playerHand) {
            cards.add(Card.fromString(cardString)); // Convert String to Card
        }
        return cards;
    }

    public List<String> getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(List<Card> cards) {
        dealerHand.clear();
        for (Card card : cards) {
            dealerHand.add(card.toString());
        }
    }

    public ArrayList<Card> getDealerHandAsCards() {
        ArrayList<Card> cards = new ArrayList<>();
        for (String cardString : dealerHand) {
            cards.add(Card.fromString(cardString)); // Convert String to Card
        }
        return cards;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}

