## Question 6
Complete `mainDiagonalSum(int[][] matrix)` to return the sum of the main diagonal.

Read `R C` then an `R x C` matrix. You may assume `R == C`.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|3 3 <br> 1 2 3 <br> 4 5 6 <br> 7 8 9|15|
|2 2 <br> 10 1 <br> -3 4|14|

### Starter Code
```java
import java.util.*;

public class Main {
    static int mainDiagonalSum(int[][] matrix) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt();
        int c = sc.nextInt();
        int[][] matrix = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }
        System.out.println(mainDiagonalSum(matrix));
    }
}
```
