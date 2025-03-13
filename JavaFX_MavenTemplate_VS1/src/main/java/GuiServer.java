import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GuiServer extends Application {

    private PokerServer server;
    private TextField portField;
    private Button startButton, stopButton, turnOnButton, turnOffButton;
    private ListView<String> serverLog;
    private Label clientCountLabel;

    private int clientCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Poker Server");

        // Welcome Screen for Port Input
        VBox welcomeLayout = new VBox(10);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));

        Label portLabel = new Label("Enter Port Number:");
        portField = new TextField("5555");
        startButton = new Button("Start Server");
        stopButton = new Button("Stop Server");
        stopButton.setDisable(true); // Initially disabled until the server starts

        HBox buttonBox = new HBox(10, startButton, stopButton);
        buttonBox.setAlignment(Pos.CENTER);

        welcomeLayout.getChildren().addAll(portLabel, portField, buttonBox);

        Scene welcomeScene = new Scene(welcomeLayout, 400, 200);

        // Main Server Scene
        BorderPane serverLayout = new BorderPane();
        serverLayout.setPadding(new Insets(20));

        serverLog = new ListView<>();
        clientCountLabel = new Label("Connected Clients: 0");

        VBox infoPane = new VBox(10, clientCountLabel, serverLog);
        infoPane.setPadding(new Insets(10));
        infoPane.setAlignment(Pos.CENTER_LEFT);

        turnOnButton = new Button("Turn On Listening");
        turnOffButton = new Button("Turn Off Listening");
        turnOffButton.setDisable(true); // Initially disabled until server is listening

        HBox listeningControls = new HBox(10, turnOnButton, turnOffButton);
        listeningControls.setAlignment(Pos.CENTER);
        listeningControls.setPadding(new Insets(10));

        serverLayout.setCenter(infoPane);
        serverLayout.setBottom(listeningControls);

        Scene serverScene = new Scene(serverLayout, 600, 400);

        // Button Actions
        startButton.setOnAction(e -> handleStartServer(primaryStage, serverScene));
        stopButton.setOnAction(e -> handleStopServer());
        turnOnButton.setOnAction(e -> handleTurnOnListening());
        turnOffButton.setOnAction(e -> handleTurnOffListening());

        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private void handleStartServer(Stage primaryStage, Scene serverScene) {
        try {
            int port = Integer.parseInt(portField.getText());
            server = new PokerServer(port, this::updateServerLog, this::updateClientCount);
            server.start();

            serverLog.getItems().add("Server initialized on port " + port);
            startButton.setDisable(true);
            stopButton.setDisable(false);
            turnOnButton.setDisable(false); // Enable listening controls

            primaryStage.setScene(serverScene);
        } catch (NumberFormatException e) {
            showError("Invalid port number.");
        }
    }

    private void handleStopServer() {
        if (server != null) {
            server.stopServer();
            serverLog.getItems().add("Server stopped.");
        }
        startButton.setDisable(false);
        stopButton.setDisable(true);
        turnOnButton.setDisable(true);
        turnOffButton.setDisable(true);
        clientCount = 0;
        clientCountLabel.setText("Connected Clients: 0");
    }

    private void handleTurnOnListening() {
        if (server != null) {
            server.setListening(true);
            serverLog.getItems().add("Server started listening for clients.");
            turnOnButton.setDisable(true);
            turnOffButton.setDisable(false);
        }
    }

    private void handleTurnOffListening() {
        if (server != null) {
            server.setListening(false);
            serverLog.getItems().add("Server stopped listening for clients.");
            turnOnButton.setDisable(false);
            turnOffButton.setDisable(true);
        }
    }

    private void updateServerLog(String message) {
        Platform.runLater(() -> serverLog.getItems().add(message));
    }

    private void updateClientCount(int countChange) {
        Platform.runLater(() -> {
            clientCount += countChange;
            clientCountLabel.setText("Connected Clients: " + clientCount);
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}


