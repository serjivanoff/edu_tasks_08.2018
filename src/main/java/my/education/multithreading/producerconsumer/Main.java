package my.education.multithreading.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by bender on 04.09.2018.
 */
public class Main {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        Thread t1 = new Thread(pc::produce);
        Thread t2 = new Thread(pc::consume);
        t1.start();
        t2.start();
    }
}

class ProducerConsumer {
    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() {
        int value = 0;
        while (true) {
            synchronized (lock) {
                while (queue.size() >= LIMIT) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.offer(value++);
                lock.notify();
            }
        }
    }

    public void consume() {
        while (true) {
            synchronized (lock) {
                while (queue.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(queue.poll());
                System.out.println(queue.size());
                lock.notify();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
