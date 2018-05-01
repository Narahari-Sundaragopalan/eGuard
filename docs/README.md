# Project Details

## Table of Contents

[Goals](#goals) | [Merits](#merits) [Application Requirements](#application-requirements) | [High Level Design](#high-level-design) | [Security Analysis](#security-analysis)

## Goals

* Develop an android application which can be integrated with the MetaWear device

* Use the capabilities of the MetaWear device such as accelerometer, gyroscope to detect user motion and activity

* Design a logic to determine whether the values of velocity and acceleration of the wearer fall in the danger category

* Develop a intuitive and easy to use user interface for the android Application

* Ensure that the application maintains connectivity and syncs with the MetaWear devices

* Test the application for the functionality

* Identify and test the security vulnerabilities of the application


## Merits

* eGuard is fall alert medical device, which will automatically call for help in case of an emergency

* eGuard tracks and notifies of a fall immediately, and provides better functionality and reliability

* eGuard ensures elderly people can be independent without the constant worry of seeking help in case of an emergency

* eGuard is an open source android based application and can be easily piloted

* eGuard is user friendly, portable and cost effective


## Application Requirements

### User Stories

**User Story 1**
* As a user, I want to detect the different user movements of a user during the course of a day, such as Sitting (Straight & Fallback), Standing, Sleeping (Straight & Sideways)

**Acceptance Criteria**
* When a person is walking, standing or sleeping, the application should monitor these normal movements continuously

**User Story 2**  
* As a user, I want to be able detect transitions in movement patterns of a user such as from Sitting to Standing; Standing to Walking; Sitting to Sleeping; etc.

**Acceptance Criteria**
* When a person makes a transition from type of movement to another, the application should monitor and check for the change in acceleration of the user continuously, and differentiate between normal transitions and fall transitions

**User Story 3**
* As a user, I want to know if the user's transition indicates a fall and the application should make an emergency alert.

**Acceptance Criteria**
* Application should monitor the user acceleration and correctly indicate, if the user has fallen, based on sudden increase in acceleration by checking a threshold value


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
#### ProcessData
     This service will collect the incoming data and process it to check for any emergency case, if it has to send an alert message in case the value of accelerometer data falls in the danger category
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
| MetaWear Device | Denial of Service | This component continuously looks for devices to connect with BLE. There is a high risk that unauthorized users or attackers might try to connect to the MetaWear device | Connect the device only in secure networks and be undiscoverable by default. Also ensure that the device has only enough power to connect within a specific perimeter which reduces the possibility of something like a DOS attack, and avoid unavailability of service |
| MetaWear API | Privacy Leakage | This component transmits data from the device to the application and allows the possibility of man in the middle attacks where malicious users may steal personal information | Secure and end to end encryption must be used to ensure that data is not easily accessed and stolen and maintain data integrity. Also ensure no sensitive information is being sent and information is sent and received from trusted devices only. |

[Go to Top](#project-details)