package Race;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MainClass {
    public static final int CARS_COUNT = 4;
    private static CountDownLatch startBarrier = new CountDownLatch (CARS_COUNT);
    private static CountDownLatch finishRaceLatch;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        finishRaceLatch = new CountDownLatch(CARS_COUNT * race.getStages().size());
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startBarrier, finishRaceLatch);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        startBarrier.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        finishRaceLatch.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}