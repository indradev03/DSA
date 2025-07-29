package TrafficSignalManagementSystem.utils;

import TrafficSignalManagementSystem.model.Vehicle;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class VehicleGenerator {
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final Random rand = new Random();

    /**
     * Generates a new Vehicle with a unique ID and random type.
     * Ambulance and Fire Truck appear with 20% chance combined (each 10%),
     * normal vehicles appear 80% of the time.
     * 
     * @return a new Vehicle instance
     */
    public static Vehicle generateVehicle() {
        int idNum = count.incrementAndGet();
        int r = rand.nextInt(10);

        Vehicle.Type type = switch (r) {
            case 0 -> Vehicle.Type.AMBULANCE;
            case 1 -> Vehicle.Type.FIRE_TRUCK;
            default -> Vehicle.Type.NORMAL;
        };

        return new Vehicle("V" + idNum, type);
    }
}
