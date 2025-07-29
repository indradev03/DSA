
package TrafficSignalManagementSystem.model;


public class Vehicle implements Comparable<Vehicle> {
    public enum Type { NORMAL, AMBULANCE, FIRE_TRUCK }

    private final String id;
    private final Type type;

    public Vehicle(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public Type getType() { return type; }
    public String getId() { return id; }

    @Override
    public int compareTo(Vehicle other) {
        // Emergency vehicles get higher priority
        return this.type.ordinal() - other.type.ordinal();
    }

    @Override
    public String toString() {
        return "[" + type + " " + id + "]";
    }
}
