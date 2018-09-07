package my.education.ekkel.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bender on 06.09.2018.
 */
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            if (captured) lock.unlock();
        }
    }

    public void timed() {
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("lock.tryLock(2, TimeUnit.SECONDS): " + captured);
        } finally {
            if (captured) lock.unlock();
        }
    }

    public static void main(String[] args) {
        AttemptLocking al = new AttemptLocking();
        al.untimed();
        al.timed();
        new Thread(() -> {
            al.lock.lock();
            System.out.println("acquired");
        }).start();
//        Thread.yield();
        al.untimed();
        al.timed();
    }
}
