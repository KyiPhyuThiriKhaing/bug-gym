## Question 5  
Write a Smart Home system as shown in the diagram below.

![UML](SmartHome_UML.png)

The Smart Home system consist of 4 classes.
SmartHome is a class contain an id, a host name, and the arraylist of smart devices. The get and set methods are the method that simply sets and get the SmartHome attributes. The method addDevice and removeDevice are responsible for adding and removing the device from the SmartHome. The method getDevicebyIndex will return the device from arraylist at the input index.

Device is an abstract class used as a template for evevery type of smart device. Ecah device must have an id and a name. The status attribute represents boolean status (true->on, false->off) which can be set by the method turnOn and turnOff. The abstract method printCurrentStatus() is outlined as a template.

Fan is a concrete class that is a subclass of the Device class. It represents a concrete smart device of type fan where color, swing, and level are stored as its attribute and can be set throught setter and getter methods.

Light is also a concrete class that is a subclass of the Device class. It represents a concrete smart device of type light where an auto is the only attribe that reflect if the light is an auto light.

The driver class is provide in the preload answer box as well as some part of these classes. Do not change the preload answer as it might cause an error in your output.

### Starter Code

```java
import java.util.Scanner;
import java.util.ArrayList;

public class SmartHomeDriver {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int home_id = sc.nextInt();
        String host_name = sc.next();
        SmartHome myhome = new SmartHome(home_id, host_name);
        int fan_num = sc.nextInt();
        for (int i = 0; i < fan_num; i++) {
            int fan_id = sc.nextInt();
            String fan_name = sc.next();
            String fan_color = sc.next();
            boolean fan_swing = sc.nextBoolean();
            int fan_level = sc.nextInt();
            Fan f = new Fan(fan_id, fan_name, fan_color);
            f.setSwing(fan_swing);
            f.setLevel(fan_level);
            myhome.addDevice(f);
        }
        int light_num = sc.nextInt();
        for (int i = 0; i < light_num; i++) {
            int light_id = sc.nextInt();
            String light_name = sc.next();
            boolean light_auto = sc.nextBoolean();
            Light l = new Light(light_id, light_name, light_auto);
            myhome.addDevice(l);
        }
        int remove_num = sc.nextInt();
        for (int i = 0; i < remove_num; i++) {
            int index = sc.nextInt();
            Device x = myhome.getDevicebyIndex(index);
            myhome.removeDevice(x);
        }

        myhome.reportStatus();
    }
}

class SmartHome {

    public void reportStatus() {
        int totalDevices = devices.size();
        System.out.println("Welcome " + getHostName() + " to your home: " + getId());
        System.out.println("Your total devices: " + totalDevices);
        for (int i = 0; i < totalDevices; i++) {
            Device d = devices.get(i);
            System.out.println("|---------------------|");
            System.out.println("Device id: " + d.getId());
            System.out.println("Device name: " + d.getDeviceName());
            System.out.println("Device status: ");
            d.printCurrentStatus();
        }
    }
}

abstract class Device {

    public abstract void printCurrentStatus();
}

class Fan {

    public void printCurrentStatus() {
        System.out.println("This fan is " + getColor());
        System.out.println("The current status is " + (getStatus() ? "on." : "off."));
        System.out.println("The swing is " + (getSwing() ? "on " : "off ") + "and level is " + getLevel());
    }
}

class Light {

    public void printCurrentStatus() {
        System.out.println("This light is " + (getAuto() ? "an auto light." : "a normal light."));
    }
}

```
