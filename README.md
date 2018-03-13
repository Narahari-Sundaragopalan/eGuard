# eGuard
Android Application which uses a MetaWear as an automatic notification system, mainly as a fall detection and alert device for elderly people.

## Executive Summary

> In today's world, many elderly people have to manage themselves on their own and are alone for most part of the day at home. There are many elderly people who have difficulty in standing up or while walking. Such people with a medical condition, face a high risk of falling down, and with no one around, there might be no immediate help available and might prove to be fatal.
eGuard provides a practical solution to this issue. Although there are many medical alert devices available in the market, they are bulky and most of them require a trigger such as a button to be pressed. eGuard is a small wearable device which is lightweight and can be easily carried. Customers can wear eGuard like any fitness tracker device. The system is an android based application which sense when someone has suddenly fallen by determining the change in their velocity and in the direction they are moving. If the device determines that the values of these two variables fall in the danger category, it will automatically send out a call for help in the form of a text message or emergency alert. Even in the case of a slow fall, the accelerometer in the eGuard will be used to observe how smooth the acceleration was, and predict if a person has made a non-linear fall. If someone falls at home, and is unattended and unable to call for help or has lost consciousness, their life could be at risk. Current devices such as emergency buttons can help, but if a person is unconscious or unable to move they cannot make use of the device to push the button. eGuard does not require the person wearing it to do anything, it automatically calls for help, if it detects that a person has fallen.

### Project Goals

* Develop an android application which can be integrated with the MetaWear device

* Use the capabilities of the MetaWear device such as accelerometer, gyroscope to detect user motion and activity

* Design a logic to determine whether the values of velocity and acceleration of the wearer fall in the danger category

* Develop a intuitive and easy to use user interface for the android Application

* Ensure that the application maintains connectivity and syncs with the MetaWear devices

* Test the application for the functionality

* Identify and test the security vulnerabilities of the application


### Project Merits

* eGuard is fall alert medical device, which will automatically call for help in case of an emergency

* eGuard tracks and notifies of a fall immediately, and provides better functionality and reliability

* eGuard ensures elderly people can be independent without the constant worry of seeking help in case of an emergency

* eGuard is an open source android based application and can be easily piloted

* eGuard is user friendly, portable and cost effective


## Application Requirements

### User Stories

**User Story 1**
* As a user, in face of an emergency, I want to send an alert message to my emergency contacts to get immediate help.

**Acceptance Criteria**
* When a person has fallen, the application should detect the fall and check if it falls in the danger range.
* If the application has detected a fall, an alert message should be sent to emergency contacts.

**User Story 2**  
* As a user, in need of immediate help, I want the device to make a call to my emergency contact.

**Acceptance Criteria**
* When a person pushes a button on the MetaWear device, a call should be made to the emergency contact

**User Story 3**
* As a user, I want to receive reminders on the MetaWear device so that I can remember to take my medicine or prescription.

**Acceptance Criteria**
* LEDs on the MetaWear device should blink at a stipulated time to serve as a reminder for the user.


### Misuser Stories

**Misuser Story 1**  
* As a misuser, I want to launch a denial of service attack on the MetaWear device which would limit or eliminate the device connectivity and availability.

**Mitigations**
* Bluetooth should be configured to be undiscoverable by default and connect to only authorized devices through pairing.
* The BLE on the MetaWear device should be set to power levels only necessary to connect within a permissible perimeter of the location of the wearer.

**Misuser Story 2**  
* As a misuser, I want to eavesdrop or steal information so that I can launch a man in the middle attack, and steal or corrupt user data, which could lead to false alarms.

**Mitigations**
* Use a secure form of data transmission such as end to end encryption.
* Make sure the device is not easily discoverable and connected in only secure home networks.

## High Level Design

**Architecture Diagram**

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/images/eGuard-ArchitectureDiagram.png)

## Component List
### eGuard Application
    Android application running on a mobile phone. It has the ability to search and connect to MetaWear device. It can read data sent from the device and take corrective action based on the data.
#### SearchDevice
     This component will search for an available MetaWear device within the permissible range and makes connection with the device.
#### ContactLookUp
     This component will search for contacts such as marked emergency contacts based on user preferences from the phone.
#### SendAlert
     This component will send an alert message to the emergency contacts when the user requires help

### MetaWear API
    This component connects the MetaWear device to the eGuard application through an android phone.

### MetaWear device
    This device collects and sends accelerometer and gyroscope data to the MetaWear API, which in turn sends the information to the eGuard application on the android phone.

## Security Analysis
The high level architecture diagram depicts the eGuard application connecting with the MetaWear device through the MetaWear API. In this process, the application will continuously poll for data and request information from the MetaWear device. Due to connection to the external network, there is a risk of data being stolen or lost, if proper security measures are not taken while developing the application.

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/images/eGuard-ArchitectureDiagram.png)

| Component name | Category of vulnerability | Issue Description | Mitigation |
|----------------|---------------------------|-------------------|------------|
| eGuard Application | Privilege Escalation | This component exposes an interface to external network that might allow malicious attackers to access, read and modify user data on the component and corrupt it. | Enforce proper authentication and access permissions only for legitimate users of the application to prevent privilege escalation |
| MetaWear Device | Unavailability of Service | This component continuously looks for devices to connect with BLE. There is a high risk that unauthorized users or attackers might try to connect to the MetaWear device | Connect the device only in secure networks and be undiscoverable by default. Also ensure that the device has only enough power to connect within a specific perimeter which reduces the possibility of something like a DOS attack, and avoid unavailability of service |
| MetaWear API | Data Integrity | This component transmits data from the device to the application and allows the possibility of man in the middle attacks where malicious users may steal personal information | Secure and end to end encryption must be used to ensure that data is not easily accessed and stolen and maintain data integrity. Also ensure no sensitive information is being sent and information is sent and received from trusted devices only. |
