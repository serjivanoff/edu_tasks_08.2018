package my.education.multithreading.producerconsumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bender on 04.09.2018.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i=0; i<3; i++) {
            service.submit(new Processor(latch, i));
        }
        service.shutdown();
        for (int i=0; i<3; i++) {
            Thread.sleep(1000);
            latch.countDown();
        }
    }
}

class Processor implements Runnable {
    private final CountDownLatch latch;
    private final int id;

    public Processor(CountDownLatch latch, int id) {
        this.latch = latch;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Thread %d executed\n", id);
    }
}
