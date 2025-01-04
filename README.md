# Network client-server app

A simple network application containing two main parts: "Network Client" Android application and "Network Server" JVM application.

### Network Client
An Android application for sending TCP/UDP packets and obtaining network information.

**Features**
- **Packet sender**: sends **TCP/UDP** packets to a designated **ip** address with **port**. Can send multiple packets over a certain **inter-arrival time**;
- **Network info**: shows various information about **connectivity**, **wifi network**, **mobile network**.

**Screenshots**

![Packet sender](https://github.com/user-attachments/assets/e13e6170-6f35-458c-a45f-f7ca1607a5f8)
![Network info](https://github.com/user-attachments/assets/3f789e96-a27c-4579-8da5-242621dc2fd7)

### Network Server
A JVM application for receiving packets on a certain port.

**Features**
- **Packet receiver**: launches a TCP/UDP packet listener on a designated port and shows incoming packets.

**Screenshots**

![Packet receiver](https://github.com/user-attachments/assets/3e6c959a-d319-4283-83e6-ded2a385a27c)
