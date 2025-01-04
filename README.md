# Network client-server app

A simple network application containing two main parts: "Network Client" Android application and "Network Server" JVM application.

## Network Client
An Android application for sending TCP/UDP packets and obtaining network information.

### Features
- **Packet sender**: sends **TCP/UDP** packets to a designated **ip** address with **port**. Can send multiple packets over a certain **inter-arrival time**;
- **Network info**: shows various information about **connectivity**, **wifi network**, **mobile network**.

### Screenshots
![Packet sender](https://github.com/user-attachments/assets/2bc2c57c-4731-4ad5-a74b-e157cc0e6bae)
![Network info](https://github.com/user-attachments/assets/a5d32f97-7b78-4d6d-b776-65ebb136a5dc)

## Network Server
A JVM application for receiving packets on a certain port.

### Features
- **Packet receiver**: launches a TCP/UDP packet listener on a designated port and shows incoming packets.

### Screenshots
![Packet receiver](https://github.com/user-attachments/assets/3e6c959a-d319-4283-83e6-ded2a385a27c)
