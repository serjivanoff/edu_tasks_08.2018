package my.education.ekkel.joiner;

/**
 * Created by bender on 06.09.2018.
 */
public class Joining {
    public static void main(String[] args) {
        Sleeper
                sleepy = new Sleeper("Sleeper", 1500),
                grumpy = new Sleeper("Grumpy", 1500);
        Joiner
                dopey = new Joiner("Dopey", sleepy),
                doc = new Joiner("Doc", grumpy);
        grumpy.interrupt();
    }
}

class Joiner extends Thread {
    private final Sleeper sleeper;

    Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println(getName() + " interrupted");
        }
        System.out.println(getName() + " completed");
    }

}

class Sleeper extends Thread {
    private final int duration;

    public Sleeper(String name, int duration) {
        super(name);
        this.duration = duration;
        start();
    }

    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " interrupted. " + isInterrupted());
            return;
        }
        System.out.println(getName() + " activated");
    }
}