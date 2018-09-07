package my.education.ekkel.locks;

/**
 * Created by bender on 07.09.2018.
 */
public class CarWax {
    public static void main(String[] args) throws InterruptedException {
        Car car = new Car();
        Thread
                t1 = new Thread(() -> {
                    while (true)
                car.waxOn();
        }),
                t2 = new Thread(() -> {
                    while (true)
                        car.polish();
                });
        t1.start();
        t2.start();
        Thread.sleep(10000);
    }
}

class Car {
    private final Object lock1 = new Object(), lock2 = new Object();
//    boolean isWaxed;
    void waxOn() {
        synchronized (lock1){
            System.out.println("The first lock acquired, waxOn() method!");
            synchronized (lock2){
                System.out.println("Will never reached");
            }
        }
//       if(!isWaxed){
//           isWaxed = true;
//           System.out.println("Just waxed... ");
//           notify();
//       } else{
//           try {
//               wait();
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//       }
   }

    void polish() {
        synchronized (lock2){
            System.out.println("The second lock acquired, polish() method!");
            synchronized (lock1){
                System.out.println("Will never reached");
            }
        }
//       if(isWaxed){
//           isWaxed = false;
//           System.out.println("Just polished... ");
//           notify();
//       } else{
//           try {
//               wait();
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//       }
    }
}