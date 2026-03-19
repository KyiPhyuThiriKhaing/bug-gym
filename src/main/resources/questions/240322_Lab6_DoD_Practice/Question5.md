## Question 5  
Giving the UML Class Diagram as below. Write a program according to the given class diagram.

![UML](library_UML.png)

Just like the real-world scenario, a library contains many publications. The class Library will keep publications in an ArrayList named publications. To add a publication, it uses add(Publication p) method to add publication. The logic of adding a publication is to check if the publication is already existed in the ArrayList. If a publication already exists, it will print "The item already exists.". Otherwise, the publication is added to ArrayList. Similarly, to remove a publication, the remove(Publication p) method will check if the ArrayList contains the publication. If it contains the publication, it will remove the publication directly. Otherwise, it will print "The item does not exist."

For the method listItem() of Library, it will list the title of all publications in the ArrayList order by index. Noted that you can use the loop to print each title from the index 0 until the end of the list. In addition, The getSize() method simply returns size of the ArrayList.

Noted that the driver class is provided in the preloaded answer box. Please do not modify the driver class.

### Starter Code

```java
import java.util.ArrayList;

public class LibraryTester {

    public static void main(String[] args) {
        Library kmuttLib = new Library("KMUTT Library");
        Book b1 = new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 439708184);
        Book b2 = new Book("The Lord of the Rings", "J.R.R. Tolkien", 544003415);
        Book b3 = new Book("Introduction to Java", "Y. Liang and Y. Daniel Liang", 244073613);
        Journal j11 = new Journal("nature", "Magdalena Skipper", 14764687, 575);
        Journal j12 = new Journal("nature", "Magdalena Skipper", 14764687, 576);
        Journal j13 = new Journal("nature", "Magdalena Skipper", 14764687, 577);
        Journal j21 = new Journal("Business Process Management Journal", "Majed Al-Mashari", 14637154, 25);
        Journal j22 = new Journal("Business Process Management Journal", "Majed Al-Mashari", 14637154, 26);
        Journal j23 = new Journal("Business Process Management Journal", "Majed Al-Mashari", 14637154, 27);
        kmuttLib.add(b1);
        kmuttLib.add(b2);
        kmuttLib.add(j11);
        kmuttLib.add(j12);
        kmuttLib.add(j13);
        kmuttLib.add(j21);
        kmuttLib.add(j22);
        kmuttLib.add(j23);
        kmuttLib.add(j23);
        kmuttLib.remove(j12);
        kmuttLib.remove(j22);
        kmuttLib.remove(b3);
        System.out.println("The KMUTT Library has " + kmuttLib.getSize() + ".");
        kmuttLib.listItem();
    }
}

```
