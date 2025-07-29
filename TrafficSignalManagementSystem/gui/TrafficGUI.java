package TrafficSignalManagementSystem.gui;

import TrafficSignalManagementSystem.model.*;
import TrafficSignalManagementSystem.thread.*;
import TrafficSignalManagementSystem.utils.VehicleGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class TrafficGUI extends JFrame {
    private final Queue<Vehicle> normalQueue = new LinkedList<>();
    private final PriorityQueue<Vehicle> emergencyQueue = new PriorityQueue<>();
    private final TrafficLight trafficLight = new TrafficLight();

    private final JTextArea normalQueueArea = new JTextArea();
    private final JTextArea emergencyQueueArea = new JTextArea();
    private final JLabel signalLabel = new JLabel("Signal: RED", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("System running...", SwingConstants.LEFT);

    private final TrafficLightController lightController = new TrafficLightController(trafficLight);
    private final VehicleProcessor vehicleProcessor = new VehicleProcessor(normalQueue, emergencyQueue, trafficLight);
    private final EmergencyHandler emergencyHandler = new EmergencyHandler(emergencyQueue, trafficLight, this); // pass this

    private final Random random = new Random();
    private boolean emergencyModeActive = false;

    public TrafficGUI() {
        setTitle("🚦 Traffic Signal Management System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // ===== Top panel: Signal Label =====
        signalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        signalLabel.setOpaque(true);
        signalLabel.setBackground(Color.RED);
        signalLabel.setForeground(Color.WHITE);
        signalLabel.setPreferredSize(new Dimension(getWidth(), 50));
        add(signalLabel, BorderLayout.NORTH);

        // ===== Center panel: Queues =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 10, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Normal Vehicles panel
        normalQueueArea.setEditable(false);
        normalQueueArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane normalScroll = new JScrollPane(normalQueueArea);
        JPanel normalPanel = new JPanel(new BorderLayout());
        normalPanel.setBorder(BorderFactory.createTitledBorder("🚗 Normal Vehicle Queue"));
        normalPanel.add(normalScroll, BorderLayout.CENTER);
        centerPanel.add(normalPanel);

        // Emergency Vehicles panel
        emergencyQueueArea.setEditable(false);
        emergencyQueueArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        emergencyQueueArea.setForeground(Color.RED.darker());
        JScrollPane emergencyScroll = new JScrollPane(emergencyQueueArea);
        JPanel emergencyPanel = new JPanel(new BorderLayout());
        emergencyPanel.setBorder(BorderFactory.createTitledBorder("🚑 Emergency Vehicle Queue"));
        emergencyPanel.add(emergencyScroll, BorderLayout.CENTER);
        centerPanel.add(emergencyPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Bottom panel: Controls and status =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10, 10));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton addVehicleBtn = new JButton("➕ Add Vehicle");
        JButton toggleSignalBtn = new JButton("🔄 Toggle Signal");
        JButton addEmergencyBtn = new JButton("🚨 Add Emergency");
        JButton toggleEmergencyModeBtn = new JButton("⚠️ Enable Emergency Mode");

        addVehicleBtn.setToolTipText("Add a random vehicle (normal or emergency)");
        toggleSignalBtn.setToolTipText("Manually toggle traffic signal");
        addEmergencyBtn.setToolTipText("Add emergency vehicle (ambulance)");
        toggleEmergencyModeBtn.setToolTipText("Enable or disable emergency mode");

        buttonPanel.add(addVehicleBtn);
        buttonPanel.add(toggleSignalBtn);
        buttonPanel.add(addEmergencyBtn);
        buttonPanel.add(toggleEmergencyModeBtn);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // Status bar
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Button actions =====
        addVehicleBtn.addActionListener(e -> {
            addVehicle();
            updateStatus("Added a vehicle.");
        });

        toggleSignalBtn.addActionListener(e -> {
            if (emergencyModeActive) {
                updateStatus("Cannot toggle signal while Emergency Mode is active.");
                return;
            }
            synchronized (trafficLight) {
                trafficLight.nextSignal();  // 3-color cycle
            }
            updateSignalLabel();
            updateStatus("Signal toggled manually.");
        });

        addEmergencyBtn.addActionListener(e -> {
            Vehicle emergency = new Vehicle("E" + random.nextInt(1000), Vehicle.Type.AMBULANCE);
            synchronized (emergencyQueue) {
                emergencyQueue.add(emergency);
            }
            updateStatus("Emergency vehicle added.");
        });

        toggleEmergencyModeBtn.addActionListener(e -> {
            emergencyModeActive = !emergencyModeActive;
            if (emergencyModeActive) {
                emergencyHandler.enableEmergencyMode();
                toggleEmergencyModeBtn.setText("⚠️ Disable Emergency Mode");
                updateStatus("Emergency mode enabled.");
            } else {
                emergencyHandler.disableEmergencyMode();
                toggleEmergencyModeBtn.setText("⚠️ Enable Emergency Mode");
                updateStatus("Emergency mode disabled.");
            }
            updateSignalLabel(); // refresh label (status text)
        });

        // ===== Start background threads =====
        lightController.start();
        vehicleProcessor.start();
        emergencyHandler.start();

        // ===== Periodic UI update timer =====
        new Timer(1000, e -> SwingUtilities.invokeLater(() -> {
            updateDisplay();
            updateSignalLabel();
        })).start();

        // Initial UI update
        updateSignalLabel();
        updateDisplay();

        setVisible(true);
    }

    private void addVehicle() {
        Vehicle v = VehicleGenerator.generateVehicle();
        if (v.getType() == Vehicle.Type.NORMAL) {
            synchronized (normalQueue) {
                normalQueue.add(v);
            }
        } else {
            synchronized (emergencyQueue) {
                emergencyQueue.add(v);
            }
        }
    }

    public void updateSignalLabel() {
        TrafficLight.Signal sig = trafficLight.getSignal();
        signalLabel.setText("Signal: " + sig);
        switch (sig) {
            case GREEN -> {
                signalLabel.setBackground(new Color(0, 153, 0));  // Dark green
                signalLabel.setForeground(Color.WHITE);
            }
            case YELLOW -> {
                signalLabel.setBackground(new Color(255, 204, 0)); // Bright yellow
                signalLabel.setForeground(Color.BLACK);
            }
            case RED -> {
                signalLabel.setBackground(new Color(204, 0, 0));  // Dark red
                signalLabel.setForeground(Color.WHITE);
            }
        }

        // Show emergency mode info in status label
        if (emergencyModeActive) {
            statusLabel.setText("Status: Emergency Mode ENABLED - Emergency vehicles always pass");
        } else {
            statusLabel.setText("Status: Normal mode - Vehicles obey signals");
        }
    }

    private void updateDisplay() {
        StringBuilder normalSb = new StringBuilder();
        synchronized (normalQueue) {
            for (Vehicle v : normalQueue) {
                normalSb.append(v).append("\n");
            }
        }
        normalQueueArea.setText(normalSb.toString());

        StringBuilder emergencySb = new StringBuilder();
        synchronized (emergencyQueue) {
            for (Vehicle v : emergencyQueue) {
                emergencySb.append(v).append("\n");
            }
        }
        emergencyQueueArea.setText(emergencySb.toString());
    }

    private void updateStatus(String msg) {
        statusLabel.setText("Status: " + msg);
    }
}
