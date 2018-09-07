package my.education.multithreading.semaphores;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bender on 05.09.2018.
 */
public class CDLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        for(int i = 0; i<3; i++){
            exec.submit(new Work(latch));
            latch.countDown();
            System.out.println("Latch count:" + latch.getCount());
        }
        exec.shutdown();
    }
}

class Work implements Runnable{
    private final CountDownLatch latch;

    Work(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public void run() {
        doWork();
    }
    public void doWork(){
        try {
            Thread.sleep(1000);
            System.out.printf("Thread %s awaiting for countdown...\n", Thread.currentThread().getName());
            latch.await();
            System.out.printf("Countdown latch released for thread %s...\n", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}