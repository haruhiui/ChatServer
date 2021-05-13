package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    public static final int PORT = 1862;
    private ServerSocket serverSocket;

    private List<Connection> connections;

    private Thread connectorThread;
    private Thread senderThread;

    private List<String> msgQue;

    public ChatServer() throws IOException {

        serverSocket = new ServerSocket(PORT);
        connections = new ArrayList<>();
        msgQue = new ArrayList<>();

        connectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        System.out.println("Server Connected: " + socket);

                        Connection conn = new Connection(socket, msgQue);
                        connections.add(conn);
                        conn.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        senderThread = new Sender(connections, msgQue);
    }

    public void start() {
        connectorThread.start();    // start listen for connection
        senderThread.start();       // start sender for all connection
        System.out.println("Server started " + serverSocket);
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }
}
