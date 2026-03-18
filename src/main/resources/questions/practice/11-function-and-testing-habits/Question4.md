## Question 4
Complete `average(int[] arr)` to return the average as a `double`.

Read `N` then `N` integers. Print the average with 2 decimal places.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|5 <br> 1 2 3 4 5|3.00|
|4 <br> 2 2 3 3|2.50|

### Starter Code
```java
import java.util.*;

public class Main {
    static double average(int[] arr) {
        // TODO
        return 0.0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
        System.out.printf("%.2f\n", average(arr));
    }
}
```
