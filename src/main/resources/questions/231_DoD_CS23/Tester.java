import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Car[] cars = new Car[n];

        for (int i = 0; i < n; i++) {
            String ownerId = sc.next();
            String firstName = sc.next();
            String lastName = sc.next();
            String gender = sc.next();

            int carId = sc.nextInt();
            String brand = sc.next();
            String color = sc.next();

            Owner owner = new Owner(ownerId, firstName, lastName, gender);
            cars[i] = new Car(carId, brand, color, owner);
        }

        System.out.println("--- Car Owner List ---");
        for (Car c : cars) {
            System.out.println("id: " + c.getId()
                    + " brand: " + c.getBrand()
                    + " color: " + c.getColor()
                    + " owner: " + c.getOwner().getFirstName() + " " + c.getOwner().getLastName());
        }

        sc.close();
    }
}

abstract class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;

    public Person(String id, String firstName, String lastName, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

class Owner extends Person {
    public Owner(String id, String firstName, String lastName, String gender) {
        super(id, firstName, lastName, gender);
    }
}

class Car {
    private int id;
    private String brand;
    private String color;
    private Owner owner;

    public Car(int id, String brand, String color, Owner owner) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
