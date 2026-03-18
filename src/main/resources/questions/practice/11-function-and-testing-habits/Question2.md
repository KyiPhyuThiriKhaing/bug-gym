## Question 2
Complete `clamp(int value, int low, int high)`.

- Return `low` if value is below low
- Return `high` if value is above high
- Otherwise return `value`

Read `value low high` and print the result.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|5 1 10|5|
|-2 0 7|0|
|20 3 9|9|

### Starter Code
```java
import java.util.*;

public class Main {
    static int clamp(int value, int low, int high) {
        // TODO
        return value;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int value = sc.nextInt();
        int low = sc.nextInt();
        int high = sc.nextInt();
        System.out.println(clamp(value, low, high));
    }
}
```
