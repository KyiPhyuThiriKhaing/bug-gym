## Question 5: Person, Owner, and Car Implementation

Write Java code according to the given description and UML diagram shown below.

![q5](q5_oop.png)

* The **Person** is an abstract class which provides a template for person information.
* The class **Owner** inherits properties of Person.
* The class **Car** represents a car having id, brand, and color. It is associated with an owner.

The driver class `Tester` is provided in the answer box for testing the system. Do not modify the driver class.

*Hint: Only the `Tester` class can be a public class.*

### Example:

| **Input** | **Result** |
|:----------|:-----------|
| 2 <br> 111 John Wick male <br> 11112015 Mercedes-AMG-GT Yellow <br> 007 James Bond male <br> 15012016 Aston-Martin Gray | --- Car Owner List --- <br> id: 11112015 brand: Mercedes-AMG-GT color: Yellow owner: John Wick <br> id: 15012016 brand: Aston-Martin color: Gray owner: James Bond |

### Starter Code

```java
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
			System.out.println(
					"id: " + c.getId()
							+ " brand: " + c.getBrand()
							+ " color: " + c.getColor()
							+ " owner: " + c.getOwner().getFirstName() + " " + c.getOwner().getLastName());
		}
	}
}

```