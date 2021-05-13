package client;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Producer producer = new Producer(list);
        Consumer consumer = new Consumer(list);
        producer.start();
        consumer.start();
    }

}

class Producer extends Thread {
    private List<String> list;

    public Producer(List<String> list) {
        this.list = list;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (list) {
                if (!list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {

                    }
                }
                list.add(Integer.toString(i));
                System.out.println("produce: " + Integer.toString(i));
                list.notify();
            }
        }
    }
}

class Consumer extends Thread {
    private List<String> list;

    public Consumer(List<String> list) {
        this.list = list;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (list) {
                if (list.isEmpty()) {
                    try {
                        list.wait();
                    } catch (InterruptedException e) {

                    }
                }
                String s = list.remove(0);
                System.out.println("consume: " + s);
                list.notify();
            }
        }
    }
}