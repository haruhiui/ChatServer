package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ChatClient {

    Scanner scanner = new Scanner(System.in);

    private BufferedReader reader;
    private PrintWriter writer;

    public ChatClient(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    public void start() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String received = reader.readLine();
                        System.out.println(received);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String input = scanner.nextLine();
                    writer.println(input);
                }
            }
        }).start();

    }

    public static void main(String[] args) throws IOException {

        InetAddress addr = InetAddress.getByName(null);
        System.out.println("addr: " + addr);
        Socket socket = new Socket(addr, 1862);
        System.out.println("socket connected");

        ChatClient client = new ChatClient(socket);
        client.start();
    }

}
