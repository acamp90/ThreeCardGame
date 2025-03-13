import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiClient extends Application {

    private Client clientConnection;

    // GUI components
    private TextField ipField, portField, anteField, pairPlusField;
    private Button connectButton, placeBetButton, playButton, foldButton;
    private Label dealerHandLabel, playerHandLabel, totalWinningsLabel;
    private ListView<String> gameLog;

    // Scene management
    private Scene welcomeScene, gameScene;

    private PokerInfo currentGameState;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("3 Card Poker Client");

        // Welcome Screen
        VBox welcomeLayout = createWelcomeScreen(primaryStage);
        welcomeScene = new Scene(welcomeLayout, 800, 600);

        // Game Play Screen
        gameScene = createGameScene(primaryStage);

        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private VBox createWelcomeScreen(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to 3 Card Poker!");
        Label ipLabel = new Label("Server IP:");
        Label portLabel = new Label("Server Port:");

        ipField = new TextField("127.0.0.1");
        portField = new TextField("5555");

        connectButton = new Button("Connect");
        connectButton.setOnAction(e -> handleConnect(primaryStage));

        layout.getChildren().addAll(welcomeLabel, ipLabel, ipField, portLabel, portField, connectButton);
        return layout;
    }

    private Scene createGameScene(Stage primaryStage) {
        BorderPane layout = new BorderPane();

        // Top section: Game Info and Log
        VBox gameInfo = new VBox(10);
        gameInfo.setAlignment(Pos.CENTER);

        dealerHandLabel = new Label("Dealer's Hand: [Hidden]");
        playerHandLabel = new Label("Player's Hand: [Waiting for deal]");
        totalWinningsLabel = new Label("Total Winnings: $0");

        gameLog = new ListView<>();

        gameInfo.getChildren().addAll(dealerHandLabel, playerHandLabel, totalWinningsLabel, gameLog);
        layout.setTop(gameInfo);

        // Center section: Bet Fields and Controls
        GridPane betControls = new GridPane();
        betControls.setHgap(10);
        betControls.setVgap(10);
        betControls.setAlignment(Pos.CENTER);

        anteField = new TextField();
        anteField.setPromptText("Ante Bet ($5-$25)");

        pairPlusField = new TextField();
        pairPlusField.setPromptText("Pair Plus Bet ($5-$25)");

        placeBetButton = new Button("Place Bet");
        placeBetButton.setOnAction(e -> handlePlaceBet());

        betControls.add(new Label("Ante Bet:"), 0, 0);
        betControls.add(anteField, 1, 0);
        betControls.add(new Label("Pair Plus Bet:"), 0, 1);
        betControls.add(pairPlusField, 1, 1);
        betControls.add(placeBetButton, 0, 2, 2, 1);

        layout.setCenter(betControls);

        // Bottom section: Play/Fold Buttons
        HBox decisionControls = new HBox(20);
        decisionControls.setAlignment(Pos.CENTER);

        playButton = new Button("Play");
        playButton.setDisable(true);
        playButton.setOnAction(e -> handlePlay());

        foldButton = new Button("Fold");
        foldButton.setDisable(true);
        foldButton.setOnAction(e -> handleFold(primaryStage));

        decisionControls.getChildren().addAll(playButton, foldButton);
        layout.setBottom(decisionControls);

        return new Scene(layout, 800, 600);
    }

    private void handleConnect(Stage primaryStage) {
        String ip = ipField.getText();
        int port;

        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            showError("Invalid port number.");
            return;
        }

        clientConnection = new Client(data -> Platform.runLater(() -> {
            if (data instanceof PokerInfo) {
                updateGameState((PokerInfo) data);
            } else {
                gameLog.getItems().add(data.toString());
            }
        }));

        try {
            clientConnection.startConnection(ip, port);
            primaryStage.setScene(gameScene);
        } catch (IOException e) {
            showError("Failed to connect to the server.");
        }
    }

    private void handlePlaceBet() {
        try {
            int ante = Integer.parseInt(anteField.getText());
            int pairPlus = pairPlusField.getText().isEmpty() ? 0 : Integer.parseInt(pairPlusField.getText());

            if (ante < 5 || ante > 25 || pairPlus < 0 || pairPlus > 25) {
                showError("Bets must be within valid ranges.");
                return;
            }

            PokerInfo info = new PokerInfo("PLACE_BETS");
            info.setAnteBet(ante);
            info.setPairPlusBet(pairPlus);
            clientConnection.send(info);

            placeBetButton.setDisable(true);
            playButton.setDisable(false);
            foldButton.setDisable(false);
        } catch (NumberFormatException e) {
            showError("Invalid bet amounts.");
        }
    }

    private void handlePlay() {
        PokerInfo info = new PokerInfo("PLAY");
        clientConnection.send(info);

        playButton.setDisable(true);
        foldButton.setDisable(true);
        placeBetButton.setDisable(false); // Allow for new bets
    }

    private void handleFold(Stage primaryStage) {
        PokerInfo info = new PokerInfo("FOLD");
        clientConnection.send(info);

        playButton.setDisable(true);
        foldButton.setDisable(true);
        placeBetButton.setDisable(false); // Allow for new bets

        showResultScreen(primaryStage, "You folded. Try again!");
    }

    private void updateGameState(PokerInfo info) {
        dealerHandLabel.setText("Dealer's Hand: " + info.getDealerHand());
        playerHandLabel.setText("Player's Hand: " + info.getPlayerHand());
        totalWinningsLabel.setText("Total Winnings: $" + info.getWinnings());
        if (info.getMessage() != null) {
            gameLog.getItems().add(info.getMessage());
        }
    }

    private void showResultScreen(Stage primaryStage, String message) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label resultLabel = new Label(message);
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e -> primaryStage.setScene(gameScene));

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        layout.getChildren().addAll(resultLabel, playAgainButton, exitButton);
        Scene resultScene = new Scene(layout, 800, 600);

        primaryStage.setScene(resultScene);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}

