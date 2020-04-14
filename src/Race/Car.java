package Race;

import java.sql.SQLOutput;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static CountDownLatch startBarrier;
    private static CountDownLatch finishRaceLatch;
    private static CyclicBarrier nextStageBarrier;

    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CountDownLatch startBarrier , CountDownLatch finishRaceLatch) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.startBarrier = startBarrier;
        this.finishRaceLatch = finishRaceLatch;

    }
    @Override
    public void run() {
        this.nextStageBarrier = new CyclicBarrier(CARS_COUNT);
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            startBarrier.countDown();
            startBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            nextStageBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
            finishRaceLatch.countDown();
        }
        System.out.println(this.name + " ЗАВЕРШИЛ ГОНКУ !!!");
    }
}
