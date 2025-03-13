import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class PokerServer {

    private int port;
    private Consumer<String> logCallback;
    private Consumer<Integer> clientCountCallback;
    private ExecutorService pool;
    private ServerSocket serverSocket;
    private volatile boolean listening; // Controls whether the server is listening for clients

    public PokerServer(int port, Consumer<String> logCallback, Consumer<Integer> clientCountCallback) {
        this.port = port;
        this.logCallback = logCallback;
        this.clientCountCallback = clientCountCallback;
        this.pool = Executors.newCachedThreadPool();
        this.listening = true; // Start in a listening state by default
    }

    public void start() {
        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                //logCallback.accept("Server initialized on port " + port);
                
                while (!serverSocket.isClosed()) {
                    if (listening) {
                        Socket clientSocket = serverSocket.accept();
                        //logCallback.accept("Client connected: " + clientSocket.getInetAddress());
                        clientCountCallback.accept(1);

                        ClientHandler handler = new ClientHandler(clientSocket);
                        pool.execute(handler);
                    } else {
                        Thread.sleep(100); // Sleep briefly to avoid busy-waiting
                    }
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    //logCallback.accept("Server stopped or error occurred: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                logCallback.accept("Server thread interrupted.");
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logCallback.accept("Server stopped.");
            }
            pool.shutdown();
        } catch (IOException e) {
            logCallback.accept("Error stopping server: " + e.getMessage());
        }
    }

    public void setListening(boolean listening) {
        this.listening = listening;
        logCallback.accept(listening ? "Server started listening for clients." : "Server stopped listening for clients.");
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                logCallback.accept("Handler initialized for client: " + socket.getInetAddress());

                while (true) {
                    Object received = in.readObject();
                    if (received instanceof PokerInfo) {
                        PokerInfo info = (PokerInfo) received;
                        processRequest(info);
                        out.writeObject(info); // Send updated info back to the client
                        out.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                logCallback.accept("Client disconnected: " + e.getMessage());
                clientCountCallback.accept(-1);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logCallback.accept("Error closing client socket: " + e.getMessage());
                }
            }
        }

        private void processRequest(PokerInfo info) {
            switch (info.getRequestType()) {
                case "PLACE_BETS":
                    handlePlaceBets(info);
                    break;
                case "PLAY":
                    handlePlay(info);
                    break;
                case "FOLD":
                    handleFold(info);
                    break;
                default:
                    logCallback.accept("Unknown request type: " + info.getRequestType());
                    info.setMessage("Error: Unknown request.");
            }
        }

        private void handlePlaceBets(PokerInfo info) {
            logCallback.accept("Player placed bets - Ante: " + info.getAnteBet() +
                               ", Pair Plus: " + (info.getPairPlusBet() > 0 ? info.getPairPlusBet() : "No Pair Plus bet"));
            info.setMessage("Bets accepted.");
        }

        private void handlePlay(PokerInfo info) {
            logCallback.accept("Player chose to PLAY.");
            evaluateGame(info);
        }

        private void handleFold(PokerInfo info) {
            logCallback.accept("Player chose to FOLD.");
            int loss = info.getAnteBet() + info.getPairPlusBet();
            info.setWinnings(info.getWinnings() - loss);
            info.setMessage("Player folded. Lost Ante and Pair Plus bets.");
            logCallback.accept("Player folded. Total loss: " + loss);
        }

        private void evaluateGame(PokerInfo info) {
            // Deal hands
            ArrayList<Card> playerHand = new Dealer().dealHand();
            ArrayList<Card> dealerHand = new Dealer().dealHand();

            info.setPlayerHand(playerHand);
            info.setDealerHand(dealerHand);

            // Determine if dealer qualifies
            boolean dealerQualifies = ThreeCardLogic.dealerQualifies(dealerHand);

            if (!dealerQualifies) {
                info.setMessage("Dealer does not qualify. Ante bet returned.");
                info.setWinnings(info.getAnteBet()); // Return Ante bet
            } else {
                int result = ThreeCardLogic.compareHands(dealerHand, playerHand);
                if (result == 2) { // Player wins
                    int winnings = info.getAnteBet() * 2 + info.getPlayBet() * 2;
                    info.setWinnings(info.getWinnings() + winnings);
                    info.setMessage("Player wins against dealer!");
                } else { // Dealer wins
                    info.setMessage("Dealer wins. Player loses Ante and Play bets.");
                }
            }

            // Pair Plus evaluation
            if (info.getPairPlusBet() > 0) {
                int pairPlusWinnings = ThreeCardLogic.evalPPWinnings(playerHand, info.getPairPlusBet());
                info.setWinnings(info.getWinnings() + pairPlusWinnings);
                logCallback.accept("Pair Plus winnings: " + pairPlusWinnings);
            }

            logCallback.accept("Game evaluation complete. Sending results to client.");
        }
    }
}

