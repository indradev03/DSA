package TrafficSignalManagementSystem.model;

public class TrafficLight {
    public enum Signal { RED, GREEN, YELLOW }

    private Signal currentSignal = Signal.RED;

    // Advances the signal in cycle: RED -> GREEN -> YELLOW -> RED
    public synchronized void nextSignal() {
        switch (currentSignal) {
            case RED -> currentSignal = Signal.GREEN;
            case GREEN -> currentSignal = Signal.YELLOW;
            case YELLOW -> currentSignal = Signal.RED;
        }
    }

    public synchronized Signal getSignal() {
        return currentSignal;
    }

    // Directly set signal (used for emergency overrides)
    public synchronized void setSignal(Signal signal) {
        currentSignal = signal;
    }

    // Optional toggle between RED and GREEN only (ignores YELLOW)
    public synchronized void toggleSignal() {
        if (currentSignal == Signal.RED) {
            currentSignal = Signal.GREEN;
        } else if (currentSignal == Signal.GREEN) {
            currentSignal = Signal.RED;
        }
        // if currentSignal is YELLOW, do nothing
    }
}
