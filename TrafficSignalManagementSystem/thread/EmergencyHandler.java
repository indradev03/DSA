package TrafficSignalManagementSystem.thread;

import TrafficSignalManagementSystem.model.TrafficLight;
import TrafficSignalManagementSystem.model.Vehicle;
import TrafficSignalManagementSystem.gui.TrafficGUI;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;

public class EmergencyHandler extends Thread {
    private final PriorityQueue<Vehicle> emergencyQueue;
    private final TrafficLight trafficLight;
    private final TrafficGUI gui;
    private volatile boolean emergencyModeEnabled = false;
    private volatile boolean running = true;

    public EmergencyHandler(PriorityQueue<Vehicle> emergencyQueue, TrafficLight trafficLight, TrafficGUI gui) {
        this.emergencyQueue = emergencyQueue;
        this.trafficLight = trafficLight;
        this.gui = gui;
        setName("EmergencyHandler");
    }

    public void enableEmergencyMode() {
        emergencyModeEnabled = true;
        System.out.println("[EMERGENCY MODE] Enabled.");
    }

    public void disableEmergencyMode() {
        emergencyModeEnabled = false;
        System.out.println("[EMERGENCY MODE] Disabled.");
    }

    public void stopHandler() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            while (running) {
                if (emergencyModeEnabled && !emergencyQueue.isEmpty()) {
                    synchronized (trafficLight) {
                        // Force signal to GREEN if not green
                        if (trafficLight.getSignal() != TrafficLight.Signal.GREEN) {
                            System.out.println("[" + LocalTime.now().format(formatter) + "] [EMERGENCY] Setting signal to GREEN.");
                            trafficLight.setSignal(TrafficLight.Signal.GREEN);
                            javax.swing.SwingUtilities.invokeLater(gui::updateSignalLabel);
                        }
                    }

                    Vehicle emergencyVehicle;
                    synchronized (emergencyQueue) {
                        emergencyVehicle = emergencyQueue.poll();
                    }

                    if (emergencyVehicle != null) {
                        System.out.println("[" + LocalTime.now().format(formatter) + "] [EMERGENCY] Processed: " + emergencyVehicle);
                    }

                    Thread.sleep(1500); // delay between emergency vehicle processing
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("EmergencyHandler thread interrupted.");
        }

        System.out.println("EmergencyHandler stopped.");
    }
}
