package server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Connection extends Thread {

    private String nickname = null;
    private BufferedReader reader;
    private PrintWriter writer;

    private List<String> msgQue;

    public Connection(Socket socket, List<String> msgQue) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        this.msgQue = msgQue;
    }

    @Override
    public void run() {
        super.run();

        // 连接后询问nickname
        writer.println("Please your nickname: ");
        try {
            while (nickname == null) {
                nickname = reader.readLine();
            }
            nickname = nickname.strip();

            // 添加日期
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String sendMsg = "[" + formatter.format(new Date()) + "] " + nickname + " entered chat, welcome.";
            synchronized (msgQue) {
                msgQue.add(sendMsg);
                msgQue.notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 监听死循环
        while (true) {

            try {
                String received = reader.readLine();
                System.out.println("Server received from " + nickname + ", content: " + received);

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String sendMsg = "[" + formatter.format(new Date()) + "] " + nickname + ": " + received;

                synchronized (msgQue) {
                    msgQue.add(sendMsg);
                    msgQue.notifyAll();
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;      // exception, maybe closed by user
            }
        }
    }

    // 发送给这个连接到的客户端的方法
    public void sendMessage(String msg) {
        writer.println(msg);
    }
}
