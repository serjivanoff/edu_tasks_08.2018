package my.education.multithreading.semaphores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by bender on 05.09.2018.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> workers = new ArrayList<>(10);

        CountDownLatch latch = new CountDownLatch(3);

        Connection conn = Connection.getConnection();
        long before = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(conn::doWork);
            t.start();
            workers.add(t);
//            conn.doWork();
        }
        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long after = System.currentTimeMillis();
        System.out.printf("Time utilized %d, ms \n", after - before);
        conn.showCounter();
    }
}

class Connection {
    private int count;
    private static Connection connection;
    private final Object lock = new Object();
    private Connection() {
    }

    public static Connection getConnection() {
        synchronized (Connection.class) {
            if (connection == null) connection = new Connection();
        }
        return connection;
    }

    public void doWork() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0; i< 100; i++){
                synchronized (lock){
                    count++;
                }
            }
//            System.out.printf("Unit %d of work completed\n", count++);
    }
    public void showCounter(){
        System.out.println(count);
    }
}