package my.education.multithreading;

/**
 * Created by bender on 05.09.2018.
 */
public class Interrupting {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            for(int i=0; i<1000_000_000; i++){
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                Math.sin(i);
            }
        });
        long before = System.currentTimeMillis();
        t.start();
        System.out.println("Task started");
        t.interrupt();
        System.out.println("Task interrupted");
        t.join();
        long after = System.currentTimeMillis();
        System.out.printf("Task completed at %d milliseconds\n", after-before);
    }
}
