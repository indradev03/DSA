package TrafficSignalManagementSystem.thread;

import TrafficSignalManagementSystem.model.TrafficLight;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrafficLightController extends Thread {
    private final TrafficLight light;
    private volatile boolean running = true;

    public TrafficLightController(TrafficLight light) {
        this.light = light;
        setName("TrafficLightController"); // Naming the thread
    }

    // Allow stopping the thread gracefully
    public void stopController() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            while (running) {
                // Sleep duration depends on current signal
                int sleepMillis;
                switch (light.getSignal()) {
                    case RED -> sleepMillis = 5000;      // Red for 5 seconds
                    case GREEN -> sleepMillis = 5000;    // Green for 5 seconds
                    case YELLOW -> sleepMillis = 2000;   // Yellow for 2 seconds
                    default -> sleepMillis = 5000;
                }

                Thread.sleep(sleepMillis);

                synchronized (light) {
                    light.nextSignal();
                    System.out.println("[" + LocalTime.now().format(formatter) + "] Signal changed to: " + light.getSignal());
                }
            }
        } catch (InterruptedException e) {
            System.out.println("TrafficLightController thread interrupted.");
        }

        System.out.println("TrafficLightController stopped.");
    }
}
