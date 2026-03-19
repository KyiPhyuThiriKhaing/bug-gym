## Question 4
Create a bank system using Java according to the following UML diagram.

![BankUML](BankUML.jpg)


The Bank class contains:
- Attribute name (String) and accounts (ArrayList<Account>)

- A constructor with an input of name. Once the constructor is called, it will create an object of a Bank and assign the name as the given input.

- A method getName() which returns name of the bank.

- A method addAccount(Account ac) which adds an input account in the accounts list.

- A method removeAccount(Account ac) which removes specific account from the accounts list.

- A method getAccount() which returns list of all accounts in the bank



The Account class contains:

- Attrubute id (int), name (String), balance (double), and transactions (ArrayList<Transaction>)

- A constructor with two inputs of id and name and a constructor with three inputs of id name and balance. These constructors will set value of id name and balanced when an object of Account is created. Noted that if no balanced is defined, a default value of 0 will be assigned to the balance.

- A method getId() getName() getBalance() which return account id, name and balance respectively.

- A method getTransaction() which returns list of transactions.

- A method addTransaction(Transaction transaction) which adds a transaction to transactions list. When a transaction is added the balance of the account must be deducted by the amount of the added transaction.

- A method withdraw(double amount) deducts balance from the given amount.

- A method deposite(double amount) adds amount to the balance.

- A method transfer(Account ac, double amount) transfers the amount of money from the account to the input account. Noted that the balance will be deducted but the balance of the input account will be added with the input amount.



The Transaction class contains:

- Attribute transactionId (String), description (String), amount (double)

- A constructor Transaction(String transactionId, String description, double amount) which set transactionId, description and amount when an object of Transaction is created.

- A method getTransactionId(), getDescription() and getAmount() which returns transactionId, description, and amount respectively.

### For example:
| **Input**     | **Result** |
|:--------------|:-----------|
|1 1 3 4 2 2 4 4<br>3<br>1 2 500<br>3 4 100<br>1 3 200|---KrungThai---<br>Account ID: 100002<br>Account Name: John Wick<br>Account Balance: 10400.0<br>Transaction ID: T05<br>Transaction Description: Central Online Shopping<br>Transaction Amount: 100.0<br>Transaction ID: T06<br>Transaction Description: Debitcard<br>Transaction Amount: 500.0<br>Account ID: 100004<br>Account Name: Bean Hilton<br>Account Balance: 7700.0<br>Transaction ID: T04<br>Transaction Description: Hotel De Lunar<br>Transaction Amount: 700.0<br>Transaction ID: T07<br>Transaction Description: Major Movie&Ceneplex<br>Transaction Amount: 1000.0<br>Transaction ID: T08<br>Transaction Description: Peach Beach Resort & Restaurant<br>Transaction Amount: 700.0<br>---Kasikorn---<br>Account ID: 100003<br>Account Name: Mary Pink<br>Account Balance: 7000.0<br>Transaction ID: T03<br>Transaction Description: Medical Treatment<br>Transaction Amount: 1000.0<br>Account ID: 100001<br>Account Name: Ann Doe<br>Account Balance: 3400.0<br>Transaction ID: T01<br>Transaction Description: Shopping at Siam Center<br>Transaction Amount: 100.0<br>Transaction ID: T02<br>Transaction Description: Creditcard<br>Transaction Amount: 500.0|

A driver class BankSystemDriver is provide in the predefined answer box. DO NOT change anything in the driver class otherwise your result may be incorrect.

### Starter Code

```java
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
public class BankSystemDriver {
    public static void main(String[] args){
        Bank krungthai = new Bank("KrungThai");
        Bank kasikorn = new Bank("Kasikorn");
        Account a1 = new Account(100001, "Ann Doe");
        Account a2 = new Account(100002, "John Wick", 1000);
        Account a3 = new Account(100003, "Mary Pink");
        Account a4 = new Account(100004, "Bean Hilton", 2000);

        HashMap<Integer, Account> accs = new HashMap<Integer, Account>();
        accs.put(1,a1);
        accs.put(2,a2);
        accs.put(3,a3);
        accs.put(4,a4);

        krungthai.addAccount(a1);
        krungthai.addAccount(a2);
        krungthai.addAccount(a4);
        kasikorn.addAccount(a3);
        krungthai.removeAccount(a1);
        kasikorn.addAccount(a1);
        a1.deposit(5000);
        a2.deposit(10000);
        a3.deposit(8000);
        a4.deposit(9000);
        a1.withdraw(300);
        a2.withdraw(500);
        a3.withdraw(100);
        a4.withdraw(1000);

        Transaction t1 = new Transaction("T01", "Shopping at Siam Center", 100);
        Transaction t2 = new Transaction("T02", "Creditcard", 500);
        Transaction t3 = new Transaction("T03", "Medical Treatment", 1000);
        Transaction t4 = new Transaction("T04", "Hotel De Lunar", 700);
        Transaction t5 = new Transaction("T05", "Central Online Shopping", 100);
        Transaction t6 = new Transaction("T06", "Debitcard", 500);
        Transaction t7 = new Transaction("T07", "Major Movie&Ceneplex", 1000);
        Transaction t8 = new Transaction("T08", "Peach Beach Resort & Restaurant", 700);
        
        HashMap<Integer, Transaction> trans = new HashMap<Integer, Transaction>();
        trans.put(1,t1);
        trans.put(2,t2);
        trans.put(3,t3);
        trans.put(4,t4);
        trans.put(5,t5);
        trans.put(6,t6);
        trans.put(7,t7);
        trans.put(8,t8);

        Scanner sc = new Scanner(System.in);
        for(int i=1;i<=8;i++){
            int temp = sc.nextInt();
            accs.get(temp).addTransaction(trans.get(i));
        }

        int num = sc.nextInt();
        for(int i=0;i<num;i++){
            int ac1 = sc.nextInt();
            int ac2 = sc.nextInt();
            double amt = sc.nextDouble();
            accs.get(ac1).transfer(accs.get(ac2), amt);
        }

        for(int i = 0;i<2;i++){
            ArrayList<Account> accounts;
            if(i==0){
                accounts = krungthai.getAccounts();
                System.out.println("---"+krungthai.getName()+"---");
            }
            else{
                accounts = kasikorn.getAccounts();
                System.out.println("---"+kasikorn.getName()+"---");
            }
            Iterator<Account> itacc = accounts.iterator();
            while(itacc.hasNext()){
                Account ac = itacc.next();
                System.out.println("Account ID: "+ac.getId());
                System.out.println("Account Name: "+ac.getName());
                System.out.println("Account Balance: "+ac.getBalance());
                ArrayList<Transaction> tr = ac.getTransactions();
                Iterator<Transaction> ittr = tr.iterator();
                while(ittr.hasNext()){
                    Transaction t = ittr.next();
                    System.out.println("Transaction ID: "+t.getTransactionId());
                    System.out.println("Transaction Description: "+t.getDescription());
                    System.out.println("Transaction Amount: "+t.getAmount());
                }
            }
            
        }
    }
}

class Bank{
    
}

class Account{
    
}

class Transaction{
    
}
```
