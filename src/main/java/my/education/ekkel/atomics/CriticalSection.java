package my.education.ekkel.atomics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bender on 06.09.2018.
 */
public class CriticalSection {
    static void testApproaches(PairManager pairManager1, PairManager pairManager2) {
        ExecutorService service = Executors.newCachedThreadPool();
        PairManipulator
                pm1 = new PairManipulator(pairManager1),
                pm2 = new PairManipulator(pairManager2);
        PairChecker
                pch1 = new PairChecker(pairManager1),
                pch2 = new PairChecker(pairManager2);
        service.execute(pm2);
        service.execute(pm1);
        service.execute(pch2);
        service.execute(pch1);

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
        System.exit(1);
    }

    public static void main(String[] args) {
        testApproaches(new PairManager1(), new PairManager2());
    }
}

class Pair {
    int x, y;

    public class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair values not equals " + Pair.this);
        }
    }

    @Override
    public String toString() {
        return "x:" + x + " y:" + y;
    }

    public void checkState() {
        if (x != y) throw new PairValuesNotEqualException();
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new LinkedList<>());

    public synchronized Pair getPair() {
        return new Pair(p.getX(), p.getY());
    }

    protected void store(Pair p) {
        storage.add(p);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void increment();
}

class PairManager1 extends PairManager {
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

class PairManager2 extends PairManager {
    @Override
    public synchronized void increment() {
        Pair temp;
            p.incrementX();
            p.incrementY();
            temp = getPair();
        store(temp);
    }
}

class PairManipulator implements Runnable {
    private final PairManager pm;

    PairManipulator(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true) {
            pm.increment();
        }
    }

    @Override
    public String toString() {
        return "Pair: " + pm.getPair() + ", checkCounter = " + pm.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private final PairManager pm;
    private int counter;
    public PairChecker(PairManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {
        while (true) {
            Math.sin(counter++);
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}