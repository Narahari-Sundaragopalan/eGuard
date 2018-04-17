## Data Collection and Understanding

> In order to understand and calculate threshold values, so that the device can continuously listen to movements of an individual and judge what kind of movements are normal and differentiate them when an individual actually falls. This will ensure there are not too many false positives and the alarm is raised only for appropriate cases.

> In order to determine a fall and differentiate it with normal movements, a few scenarios were considered and data sample was collected using the Accelerometer on the MetaWear device. Each case has a pattern plotted for the X, Y and Z axes of the accelerometer. The different scenarios with their graphs are shown below

> The first column shows the different possible transition states as noted in the state diagram. The second column specifies the type of transition. The third column notes the change in the magnitude of the acceleration when the transition occurs from position A to position B.

The magnitude of acceleration is calculated by taking the square root of the sum of the squares of the value in each of the axes of the accelerometer.
```
Acceleration = { Square Root of (X^2 + Y^2 +Z^2) }

  X - Reading in X-Axis of Accelerometer
  Y - Reading in Y-Axis of Accelerometer
  Z - Reading in Z-Axis of Accelerometer
```

| Transition  | Transition Type | Threshold Value (Change in Acceleration Magnitude during Transition) |
|----------------|---------------------------|-------------------|
| T12 | Standing to Walking | 0.9 to 1.24 |
| T13 | Standing to Sitting(Straight) | 0.92 to 1.17 |
| T14 | Standing to Sitting(Fallback) | 0.92 to 1.18 |
| T17 | Standing to Fall(Front) | 0.95 to 3.8 |
| T17 | Standing to Fall(Back) | 0.95 to 2.2 |
| T21 | Walking to Standing | 1.2 to 0.93 |
| T27 | Walking to Fall | 1.1 to 4.5 |
| T31 | Sitting(Straight) to Standing | 0.91 to 1.3 |
| T34 | Sitting(Straight) to Sitting(Fallback) | 0.92 to 0.84 |
| T35 | Sitting(Straight) to Lie Down(Straight) | 0.91 to 0.96 |
| T36 | Sitting(Straight) to Lie Down(Sideways) | 0.92 to 2.2 |
| T37 | Sitting(Straight) to Fall | 0 |
| T43 | Sitting(Fallback) to Sitting(Straight) | 0.97 to 0.99 |
| T45 | Sitting(Fallback) to Lie Down(Straight) | 0 |
| T46 | Sitting(Fallback) to Lie Down(Sideways) | 0 |
| T47 | Sitting(Fallback) to Fall | 0 |
| T53 | Lie Down(Straight) to Sitting(Straight) | 0.99 to 1.09 |
| T56 | Lie Down(Straight) to Lie Down(Sideways) | 0.98 to 1.89 |
| T63 | Lie Down(Sideways) to Sitting(Straight) | 0.97 to 1.1 |
| T65 | Lie Down(Sideways) to Lie Down(Straight) | 1 to 1.09 |

### State Diagram
![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/eGuard-State-Transition-Diagram.png)

### Transition - T12

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T12.png)

### Transition - T13

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T13.png)

### Transition - T14

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T14.png)

### Transition - T17

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T17.png)

### Transition - T17 (Fall On Back)

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T17_B.png)

### Transition - T21

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T21.png)

### Transition - T27

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T27.png)

### Transition - T31

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T31.png)

### Transition - T34

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T34.png)

### Transition - T35

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T35.png)

### Transition - T36

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T36.png)

### Transition - T37

### Transition - T43

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T43.png)

### Transition - T45

### Transition - T46

### Transition - T47

### Transition - T53

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T53.png)

### Transition - T56

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T56.png)

### Transition - T63

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T63.png)

### Transition - T65

![Tooltip for visually disabled](https://github.com/Narahari-Sundaragopalan/eGuard/blob/master/data-source/graphs/T65.png)
