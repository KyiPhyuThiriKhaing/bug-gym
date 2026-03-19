## Question 2
Write a Java class named Box to represent a box. The class contains:

a.      A named constant ONE_UNIT with value 1.0 to denote the unit length default value.

b.      Three double data field (attributes) named width, height and depth that specify the width, height and depth of the box. The default values are ONE_UNIT for all width, height and depth.

c.       A no-arg (default) constructor that creates a default box.

d.      A constructor that creates a box with the specified width, height and depth.

e.      A method named getCapacity() that returns the volume (capacity) of this box.

f.        A method named getArea() that returns the surface area of this box.

Note that the driver class is provided in the preloaded answer box. Please do not modify the driver class otherwise your result may be incorrect.

### For example:
| **Input**     | **Result** |
|:--------------|:-----------|
|0| Default Box is Created <br> Capacity: 1.0 <br> Surface area: 6.0|
|2<br>5 8 2<br>2.5 4.5 5|Capacity: 80.0<br>Surface area: 132.0<br>Capacity: 56.25<br>Surface area: 92.5|

### Starter Code

```java
import java.util.Scanner;
public class BoxDriver {
    public static void main(String arg[]) {
        Scanner sc = new Scanner(System.in);
        int box = sc.nextInt();
        if(box!=0){
            for(int i=0;i<box;i++){
                double width = sc.nextDouble();
                double height = sc.nextDouble();
                double depth = sc.nextDouble();
                Box b = new Box(width, height, depth);
                System.out.println("Capacity: "+b.getCapacity());
                System.out.println("Surface area: "+b.getArea());
            }
        }
        else{
            Box b = new Box();
            System.out.println("Default Box is Created");
            System.out.println("Capacity: "+b.getCapacity());
            System.out.println("Surface area: "+b.getArea());
        }
    } 
}

class Box{
    
}
```
