package TrafficSignalManagementSystem.thread;

import TrafficSignalManagementSystem.model.TrafficLight;
import TrafficSignalManagementSystem.model.Vehicle;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Queue;

public class VehicleProcessor extends Thread {
    private final Queue<Vehicle> normalQueue;
    private final PriorityQueue<Vehicle> emergencyQueue;
    private final TrafficLight light;
    private volatile boolean running = true;

    public VehicleProcessor(Queue<Vehicle> normalQueue, PriorityQueue<Vehicle> emergencyQueue, TrafficLight light) {
        this.normalQueue = normalQueue;
        this.emergencyQueue = emergencyQueue;
        this.light = light;
        setName("VehicleProcessor");
    }

    public void stopProcessor() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            while (running) {
                Vehicle vehicle = null;

                // First try to process emergency vehicles (always allowed)
                synchronized (emergencyQueue) {
                    vehicle = emergencyQueue.poll();
                }

                if (vehicle != null) {
                    // Emergency vehicle passed regardless of signal
                    System.out.println("[" + LocalTime.now().format(formatter) + "] Emergency vehicle passed: " + vehicle);
                } else {
                    // No emergency vehicles, process normal vehicles only on YELLOW or GREEN signals
                    TrafficLight.Signal currentSignal = light.getSignal();
                    if (currentSignal == TrafficLight.Signal.GREEN || currentSignal == TrafficLight.Signal.YELLOW) {
                        synchronized (normalQueue) {
                            vehicle = normalQueue.poll();
                        }
                        if (vehicle != null) {
                            System.out.println("[" + LocalTime.now().format(formatter) + "] Normal vehicle passed: " + vehicle);
                        }
                    } else {
                        // RED signal: normal vehicles must wait
                        // Optionally, you can print waiting status or just skip
                    }
                }

                Thread.sleep(1000);  // 1 second delay before next check
            }
        } catch (InterruptedException e) {
            System.out.println("VehicleProcessor thread interrupted.");
        }

        System.out.println("VehicleProcessor stopped.");
    }
}
