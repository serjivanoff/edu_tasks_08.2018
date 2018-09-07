package my.education.multithreading.semaphores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bender on 05.09.2018.
 */
public class RELock {
    public int counter;
    final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        RELock reLock = new RELock();
        List<Thread> workers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Worker(reLock));
            t.start();
            workers.add(t);
        }
        workers.forEach(w -> {
            try {
                w.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(reLock.counter);
    }
}

class Worker implements Runnable {
    private RELock reLock;

    public Worker(RELock reLock) {
        this.reLock = reLock;
    }

    private void increment1() {
        for (int i = 0; i < 10000; i++) {
            reLock.lock.lock();
            reLock.counter++;
            reLock.lock.unlock();
        }
    }
    @Override
    public void run() {
        increment1();
        System.out.println(Thread.currentThread().getName());
    }
}
