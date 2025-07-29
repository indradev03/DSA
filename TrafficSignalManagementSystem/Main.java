package TrafficSignalManagementSystem;

import javax.swing.SwingUtilities;
import TrafficSignalManagementSystem.gui.TrafficGUI;

/**
 * Main class to launch the Traffic Signal Management System application.
 * 
 * This system simulates a traffic intersection managing two types of vehicle queues:
 * - Normal vehicles (cars, etc.)
 * - Emergency vehicles (ambulances, fire trucks)
 * 
 * Core features include:
 * - Traffic light cycling through RED, GREEN, and YELLOW signals.
 * - Priority handling for emergency vehicles which get immediate green signal.
 * - Normal vehicles follow standard traffic rules: stop on RED, prepare on YELLOW, go on GREEN.
 * - A GUI that displays traffic light status, vehicle queues, and controls for adding vehicles and toggling modes.
 * 
 * System components overview:
 * 
 * 1. GUI (TrafficSignalManagementSystem.gui.TrafficGUI)
 *    - Swing-based user interface displaying signal status, vehicle queues, and control buttons.
 *    - Starts background threads and updates UI periodically.
 * 
 * 2. Model (TrafficSignalManagementSystem.model)
 *    - TrafficLight: Maintains current signal state and handles signal cycling.
 *    - Vehicle: Represents vehicles with a unique ID and type (NORMAL, AMBULANCE, FIRE_TRUCK).
 * 
 * 3. Threads (TrafficSignalManagementSystem.thread)
 *    - TrafficLightController: Cycles traffic light signals on a timed schedule.
 *    - VehicleProcessor: Processes vehicles passing the intersection, respecting signal rules.
 *        - Emergency vehicles pass immediately regardless of signal.
 *        - Normal vehicles pass only on GREEN or YELLOW signals.
 *    - EmergencyHandler: Overrides signal to GREEN when emergency mode is active and emergency vehicles are waiting.
 * 
 * 4. Utilities (TrafficSignalManagementSystem.utils)
 *    - VehicleGenerator: Generates vehicles with unique IDs and randomized types to simulate traffic flow.
 * 
 * 5. Thread Safety & Synchronization:
 *    - All queue and traffic light state accesses are synchronized to avoid concurrency issues.
 * 
 * 6. User Interaction:
 *    - Users can add normal or emergency vehicles.
 *    - Users can manually toggle signals (except when emergency mode is active).
 *    - Emergency mode can be toggled to prioritize emergency traffic automatically.
 * 
 * Entry point:
 * - The main() method initializes and displays the GUI safely on the Swing Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the TrafficGUI window on the Swing event dispatch thread to ensure thread safety
        SwingUtilities.invokeLater(() -> new TrafficGUI());
    }
}
