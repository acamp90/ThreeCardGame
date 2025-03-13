import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private String serverIP;
    private int serverPort;
    private Consumer<Object> callback;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Thread clientThread;

    public Client(Consumer<Object> callback) {
        this.callback = callback;
    }

    public void startConnection(String serverIP, int serverPort) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        socket = new Socket(serverIP, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        clientThread = new Thread(this::runClient);
        clientThread.start();
    }

    private void runClient() {
        try {
            while (true) {
                Object received = in.readObject();
                callback.accept(received);
            }
        } catch (IOException | ClassNotFoundException e) {
            callback.accept("Connection closed or error occurred: " + e.getMessage());
            stopConnection();
        }
    }

    public void send(Object data) {
        try {
            if (out != null) {
                out.writeObject(data);
                out.flush();
            }
        } catch (IOException e) {
            callback.accept("Error sending data: " + e.getMessage());
        }
    }

    public void stopConnection() {
        try {
            if (socket != null) socket.close();
            if (clientThread != null) clientThread.interrupt();
        } catch (IOException e) {
            callback.accept("Error closing connection: " + e.getMessage());
        }
    }
}

