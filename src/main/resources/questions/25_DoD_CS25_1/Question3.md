## Question 3

Write a program to swap elements in the given list of number such that odd numbers and even numbers are sorted.

**For example, the given list of number is:**
`9 7 6 4 5 2`

| 9 | 7 | 6 | 4 | 5 | 2 |
| --- | --- | --- | --- | --- | --- |
| Odd | Odd | Even | Even | Odd | Even |

* Odd numbers are: 9 7 5 --(sort)--> 5 7 9
* Even numbers are: 6 4 2 --(sort)--> 2 4 6

**Put the numbers back to the odd and even position with the sorted order as follow:**

| 5 | 7 | 2 | 4 | 9 | 6 |
| --- | --- | --- | --- | --- | --- |
| Odd | Odd | Even | Even | Odd | Even |

**The final swap list is:** `5 7 2 4 9 6`

### For example:

| **Input** | **Result** |
| --- | --- |
| 6 <br> 9 7 6 4 5 2 | 5 7 2 4 9 6 |