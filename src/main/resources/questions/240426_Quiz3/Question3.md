## Question 3
Write a class representing a square based pyramid as illustrated below.

![SquarePyramid](SquarePyramid.jpg)

|Pyramid|
|:------|
|- height: double <br> - slantheight: double <br> - basewidth: double|
|+ Pyramid(height:double, slantheight:double, basewidth:double) <br> + getVolumn(): double <br> + getArea(): double|

The Pyramid class contains three main atrributes. They are height, slant height and base width as for the base's side length. The constructor Pyramid will initiate the object with the input values.

The getVolumn() method is used to calculate volumn of a pyramid which is returned as double.
The fomula for calculating volumn of pyramid is: base area × height / 3. In this case the base area is the square area which can be obtained by: basewidth × basewidth.

Similary, the method getAreay() is used to calculate area of pyramid's surface which is returned as double.
The formular for calculating area of pyramid's surface is: base area + sum of the areas of its lateral faces. Each lateral face area can be obtained by: (basewidth × slantheight) / 2. 

Noted that the driver class is provided in the preloaded answer box. Do not change the driver class otherwise your result may be incorrect.
### For example:
| **Input**     | **Result** |
|:--------------|:-----------|
|12 13 10|Volumn: 400.0<br>Surface area: 360.0|
|36 39 30|Volumn: 10800.0<br>Surface area: 3240.0|
|19.615 21 15|Volumn: 1471.125<br>Surface area: 855.0|
|24 25 14|Volumn: 1568.0<br>Surface area: 896.0|
|40 41 18|Volumn: 4320.0<br>Surface area: 1800.0|

### Starter Code

```java
import java.util.Scanner;

public class PyramidDriver {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    double height = sc.nextDouble();
    double slantheight = sc.nextDouble();
    double basewidth = sc.nextDouble();
    Pyramid p = new Pyramid(height, slantheight, basewidth);
    System.out.println("Volumn: " + p.getVolumn());
    System.out.println("Surface area: " + p.getArea());
  }
}

class Pyramid {
  //put your code here
}
```
