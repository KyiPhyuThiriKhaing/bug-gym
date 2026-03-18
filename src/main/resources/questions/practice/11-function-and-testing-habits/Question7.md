## Question 7
Complete `normalizeSpaces(String s)`.

- Remove leading and trailing spaces
- Replace multiple inner spaces with a single space

Read one full line and print the normalized result.

### For example:
| **Input** | **Result** |
|:--------- |:-----------|
|  hello   java   world  |hello java world|
|a    b|a b|
|single|single|

### Starter Code
```java
import java.util.*;

public class Main {
    static String normalizeSpaces(String s) {
        // TODO
        return s;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(normalizeSpaces(s));
    }
}
```
