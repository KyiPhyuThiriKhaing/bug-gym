## Question 8
Complete `gradeFromScore(int score)` to return a letter grade:

- `A` for 90-100
- `B` for 80-89
- `C` for 70-79
- `D` for 60-69
- `F` for below 60

Read one integer and print the returned grade.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|95|A|
|82|B|
|59|F|

### Starter Code
```java
import java.util.*;

public class Main {
    static char gradeFromScore(int score) {
        // TODO
        return 'F';
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int score = sc.nextInt();
        System.out.println(gradeFromScore(score));
    }
}
```
