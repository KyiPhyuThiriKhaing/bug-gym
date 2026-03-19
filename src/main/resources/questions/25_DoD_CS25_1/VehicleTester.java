import java.util.ArrayList;
import java.util.Scanner;

public class VehicleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String ownerName = sc.next();

        int carId = sc.nextInt();
        String carBrand = sc.next();
        String carModel = sc.next();
        String carColor = sc.next();
        double consumptionRate = sc.nextDouble();

        String bicycleBrand = sc.next();
        String bicycleModel = sc.next();
        String bicycleColor = sc.next();

        double carSpeed = sc.nextDouble();
        double carDistance = sc.nextDouble();
        double bicycleSpeed = sc.nextDouble();
        double bicycleDistance = sc.nextDouble();

        Owner owner = new Owner(ownerName);
        Car car = new Car(carId, carBrand, carModel, carColor, consumptionRate);
        Bicycle bicycle = new Bicycle(bicycleBrand, bicycleModel, bicycleColor);

        owner.addVehicle(car);
        owner.addVehicle(bicycle);

        owner.move(car, carSpeed, carDistance);
        owner.move(bicycle, bicycleSpeed, bicycleDistance);

        System.out.println("Owner: " + owner.getName());
        car.printInfo();
        bicycle.printInfo();

        sc.close();
    }
}

class Owner {
    private String name;
    private ArrayList<Vehicle> vehicles;

    public Owner(String name) {
        this.name = name;
        this.vehicles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle v) {
        if (!vehicles.contains(v)) {
            vehicles.add(v);
        }
    }

    public void move(Vehicle v, double speed, double distance) {
        if (vehicles.contains(v)) {
            v.move(speed, distance);
        }
    }
}

abstract class Vehicle {
    private String brand;
    private String type;
    private String model;
    private String color;
    private double totalDistance;

    public Vehicle(String brand, String type, String model, String color) {
        this.brand = brand;
        this.type = type;
        this.model = model;
        this.color = color;
        this.totalDistance = 0;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void printInfo() {
        System.out.println("Type: " + type
                + " Brand: " + brand
                + " Model: " + model
                + " Color: " + color
                + " Total Distance: " + totalDistance);
    }

    public abstract void move(double speed, double distance);
}

class Car extends Vehicle {
    private int id;
    private double consumptionRate;
    private double powerConsumption;

    public Car(int id, String brand, String model, String color, double consumptionRate) {
        super(brand, "Car", model, color);
        this.id = id;
        this.consumptionRate = consumptionRate;
        this.powerConsumption = 0;
    }

    public int getId() {
        return id;
    }

    public double getConsumptionRate() {
        return consumptionRate;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public void move(double speed, double distance) {
        if (speed == 0) {
            return;
        }
        setTotalDistance(getTotalDistance() + distance);
        powerConsumption += distance / consumptionRate;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Power Consumption: " + powerConsumption);
    }
}

class Bicycle extends Vehicle {
    public Bicycle(String brand, String model, String color) {
        super(brand, "Bicycle", model, color);
    }

    @Override
    public void move(double speed, double distance) {
        if (speed == 0) {
            return;
        }
        setTotalDistance(getTotalDistance() + distance);
    }
}
