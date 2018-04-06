## Data Collection and Understanding

> In order to understand and calculate threshold values, so that the device can continuously listen to movements of an individual and judge what kind of movements are normal and differentiate them when an individual actually falls. This will ensure there are not too many false positives and the alarm is raised only for appropriate cases.

> In order to determine a fall and differentiate it with normal movements, a few scenarios were considered and data sample was collected using the Accelerometer on the MetaWear device. Each case has a pattern plotted for the X, Y and Z axes of the accelerometer. The cases with their graphs are shown below

### State Diagram
![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/EGuard_State_Diagram.png)

### Case 1 - User goes from a sitting position to a standing position

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case1.png)

### Case 2.1 - User goes from a standing position to a sitting position (and falls back on the couch)

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case2-1.png)

### Case 2.2 - User goes from a standing position to a sitting position (on the ground)

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case2-2.png)

### Case 3 - User goes from a standing position to walking

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case3.png)

### Case 4 - User goes from walking to standing position

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case4.png)

### Case 5 - User goes from a sitting position to sleeping position

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case5.png)

### Case 6 - User goes from a sleeping position to sitting position

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/Case6.png)

### Case 7 - User falls while walking

### Case 8 - User falls while standing
