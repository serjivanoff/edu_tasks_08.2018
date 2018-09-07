package my.education.ekkel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bender on 06.09.2018.
 */
public class MyFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            workers.add(new Thread(new MyJob(Thread.MIN_PRIORITY)));
        }
        workers.add(new Thread(new MyJob(Thread.MAX_PRIORITY)));
        workers.forEach(Thread::start);
        workers.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

class MyJob implements Runnable {
    private final int priority;

    MyJob(int priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        for (int i = 0; i < 1000_000; i++) {
            Math.sin(i);
            if(i%1000==0)Thread.yield();
        }
        System.out.println("thread with priority " + priority + " completed");
    }
}