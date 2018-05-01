# eGuard
Android Application which uses a MetaWear as an automatic notification system, mainly as a fall detection and alert device for elderly people. To start using the application immediately, go to the [Quick Start Guide](#quick-start-guide)

## Table of Contents

[Executive Summary](#executive-summary) | [Project Details](#project-details) | [Application Requirements](#application-requirements) | [High Level Design](#high-level-design) | [Security Analysis](#security-analysis) | [Quick Start Guide](#quick-start-guide)

## Executive Summary

> In today's world, many elderly people have to manage themselves on their own and are alone for most part of the day at home. There are many elderly people who have difficulty in standing up or while walking. Such people with a medical condition, face a high risk of falling down, and with no one around, there might be no immediate help available and might prove to be fatal.
eGuard provides a practical solution to this issue. Although there are many medical alert devices available in the market, they are bulky and most of them require a trigger such as a button to be pressed. eGuard is a small wearable device which is lightweight and can be easily carried. Customers can wear eGuard like any fitness tracker device. The system is an android based application which sense when someone has suddenly fallen by determining the change in their velocity and in the direction they are moving. If the device determines that the values of these two variables fall in the danger category, it will automatically send out a call for help in the form of a text message or emergency alert. Even in the case of a slow fall, the accelerometer in the eGuard will be used to observe how smooth the acceleration was, and predict if a person has made a non-linear fall. If someone falls at home, and is unattended and unable to call for help or has lost consciousness, their life could be at risk. Current devices such as emergency buttons can help, but if a person is unconscious or unable to move they cannot make use of the device to push the button. eGuard does not require the person wearing it to do anything, it automatically calls for help, if it detects that a person has fallen.

## Project Details

### Goals

* Develop an android application which can be integrated with the MetaWear device

* Use the capabilities of the MetaWear device such as accelerometer, gyroscope to detect user motion and activity

* Design a logic to determine whether the values of velocity and acceleration of the wearer fall in the danger category

* Develop a intuitive and easy to use user interface for the android Application

* Ensure that the application maintains connectivity and syncs with the MetaWear devices

* Test the application for the functionality

* Identify and test the security vulnerabilities of the application


### Merits

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

## Quick Start Guide
This section gives a quick guide to get started with the application and the requirements associated with it

### Hardware Requirements
Below listed are the hardware components required to run the application

* Android device with Android 6 (Marshmellow) or higher
* Metawear device (any of the C or R versions). Go to the [Mbient Lab Store](https://mbientlab.com/store) and get your metawear device today
* Laptop or Desktop with a USB cable (Optional: if you are a developer and want to debug or develop new features)

### Software Requirements

* Android Studio IDE (Download from [here](https://developer.android.com/studio/))

### Installation
Below are the instructions for installation/development setup of the application

* If you are looking to use the application directly, download the ```apk``` file from here
* Install the downloaded apk file to your device 
    * By either using a USB cable to transfer the apk to your device
    * You can also open the ```apk link``` from your android device and download it directly to your device
* Next go to Getting Started section on how to use the application
* If you are a developer looking to extend or debug the code, clone the git repository from the command prompt, using the below command
```
git clone https://github.com/Narahari-Sundaragopalan/eGuard.git
```
* Otherwise, you can also download the zip version of the repository from GitHub and unzip it to get the contents
* Once the code is downloaded, open Android Studio on your system
* Go to ```File > Open``` option, and select the folder you cloned/downloaded in the step above
* The android project will be imported by Android Studio and synced
* At this point, you can connect your Android device to the system using a USB cable
* At the right top, click on the toolbar icon to build the project. This will build the necessary source files from the gradle file
* Once the project is successfully built, without any errors, click on the play icon at the right top, to run the application
* Android Studio will recognize the connected android device and list all the connected devices (if more than one is connected)
* Choose the appropriate device of your choice to run the application
* The application will be installed on your android device and then will be run on the device
* If you wish to make any code changes or insert new log/debug statements, make sure you build the project  and
run the application again for the changes to take effect

### Getting Started
Below are the instructions on how to use the application

> Make sure you have gone through the [Installation](#installation) section to download and install the application to your android device

* The application uses a Metawear device to measure and stream a user's acceleration and movements
* Make sure you have your phone and metawear device nearby
* The metawear device can either be pinned to your shoulder or worn like a pendant around your neck
* Open the application, and let it search and connect to the metawear device
* Once the device is connected an alert dialog will pop up to confirm the connection
* If there is an issue with connecting to the metawear device, try any of the following steps
  * Make sure your android device and metawear device are nearby with bluetooth on your android device turned on
  * Use the [Metabase](https://play.google.com/store/apps/details?id=com.mbientlab.metawear.metabase&hl=en) application (developed by MbientLab) to update the firmware on the metawear device and then      reconnect from the application
  * Use the Metabase application to reset the metawear device and then try to reconnect from the application
* Once connected, there are 3 buttons on the application, ```On```, ```Off``` and ```Reset```
  * The ```On``` button provides the user with the option on when they want to start tracking their movement and      acceleration
  * Once pressed, the ```On``` button starts tracking the user acceleration and streams acceleration to the           application
  * This can be observed in the graph representing the acceleration across the 3 axes of the accelerometer
  * The ```On``` button ensures the user movement is continuously tracked and checks if the movements are normal or fall scenarios
  * Based on the sudden increase/decrease in the user acceleration, the application determines if the user is safe or if the user has encountered a fall
  * In case of a fall scenario, the application sends a notification indicating the user has fallen
  * The application also creates an alert to indicate that the notification was sent out succesfully
  * For all other scenarios for user movements, the application continues to silently monitor the user acceleration   and movements
  * For more information on the various scenarios considered, please check [Data Sources](https://github.com/Narahari-Sundaragopalan/eGuard/tree/master/data-source)

  * The ```Off``` button provides the user an option to turn off the tracking and monitoring process
  * This option provides a way to save phone resources and metawear device resources
  * However, it is advised the user turns the feature off, only when he/she is in an appropriate environment,surrounded by people who can provide immediate help in case of an emergency

  * The ```Reset``` button provides a way to delete the data stored and trakced by the metawear device
  * If at any point of time, the user feels the need to reset the metawear device or requires a fresh setup, this setting could be used
  * It might also be applicable to reset the board when the device is changing hands and used by a different person than before

* User can exit the application, by simply clicking the ```Back``` button on their android device or removing the application from the recently used applications taskbar in android.