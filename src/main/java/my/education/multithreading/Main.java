package my.education.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bender on 04.09.2018.
 */
public class Main {
    public static void main(String[] args) {
        Worker w = new Worker();
        w.main();
    }
}

class Worker {
    private final Object lock1 = new Object(), lock2 = new Object();

    private Random r = new Random();
    private List<Integer> ints1 = new ArrayList<>();
    private List<Integer> ints2 = new ArrayList<>();

    private void addToList1() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock1) {
            ints1.add(r.nextInt(100));
        }
    }

    private void addToList2() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock2){
            ints2.add(r.nextInt(100));
        }
    }

    private void doWork() {
        for (int i = 0; i < 1000; i++) {
            addToList1();
            addToList2();
        }
    }

    public void main() {
        long before = System.currentTimeMillis();
        Thread t1 = new Thread(this::doWork), t2 = new Thread(this::doWork);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long after = System.currentTimeMillis();
        System.out.printf("Execution took: %d, ms \n", after - before);
        System.out.println(ints1.size());
        System.out.println(ints2.size());
    }
}
