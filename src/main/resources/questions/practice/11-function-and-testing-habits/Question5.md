## Question 5
Complete `isStrictlyIncreasing(int[] arr)`.

Return `true` if every element is greater than the previous one, otherwise return `false`.

Read `N` then `N` integers. Print `true` or `false`.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|5 <br> 1 3 5 8 9|true|
|4 <br> 1 2 2 3|false|
|3 <br> 9 7 5|false|

### Starter Code
```java
import java.util.*;

public class Main {
    static boolean isStrictlyIncreasing(int[] arr) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();
        System.out.println(isStrictlyIncreasing(arr));
    }
}
```
