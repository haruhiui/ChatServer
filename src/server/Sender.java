package server;

import java.util.List;

public class Sender extends Thread {

    private List<Connection> connections;
    private List<String> msgQue;

    public Sender(List<Connection> connections, List<String> msgQue) {
        this.connections = connections;
        this.msgQue = msgQue;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            String msg = null;
            synchronized (msgQue) {
                if (msgQue.isEmpty()) {
                    try {
                        msgQue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (!msgQue.isEmpty()) {
                    msg = msgQue.get(0);
                    msgQue.remove(0);
                }
            }

            // 在msgQue的同步区块之外遍历链接发送消息，尽快运行完synchronized块
            if (msg != null) {
                for (Connection conn : connections) {
                    conn.sendMessage(msg);
                }
            }
        }
    }
}