package my.education.multithreading;

/**
 * Created by bender on 04.09.2018.
 */
public class Work implements Runnable {
    private final int id;

    public Work(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Work %d completed\n", id);
    }
}
