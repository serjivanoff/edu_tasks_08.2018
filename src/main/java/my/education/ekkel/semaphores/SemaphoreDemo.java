package my.education.ekkel.semaphores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by bender on 07.09.2018.
 */
public class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws InterruptedException {
        final Pool<Fat> pool = new Pool<>(Fat.class, SIZE);
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=0; i<SIZE; i++) executor.execute(new CheckOutTask<>(pool));

        System.out.println("All CheckOut Tasks created.");
        List<Fat> list = new ArrayList<>();
        for(int i=0; i<SIZE; i++){
            Fat f = pool.checkOut();
            System.out.println(i + "main() thread checked out");
            f.operation();
            list.add(f);
        }
        Future<?>blocked = executor.submit(()->{
            try {
                pool.checkOut();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("Checking in objects in " + list);
        for(Fat f:list) pool.checkIn(f);
        for(Fat f:list) pool.checkIn(f);
        executor.shutdown();
    }
}
