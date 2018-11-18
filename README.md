# FreeFall
An application particularly afraid of heights.

## Initial Plan
### Goal:
The goal of this application is to detect free fall of the device on which the app is installed. Development of this app will be severely time-constrained.

### App structure:
This application will consist of a plain activity whose background will change to red when it detects free fall.

### Detecting Free Fall:
The Android sensors API provides a useful "software sensor" for this task - the gravity sensor - which provides a three dimensional vector indicating the direction and magnitude of gravity. A software sensor is one that aggregates information from one or more hardware sensors but provides readings using the same API. The documentation suggests that the system uses the devices gyroscope and accelerometer to provide readings for the gravity sensor, and so using this sensor we shouldn't have to factor in other causes of acceleration like the user shaking the device. It should be enough to simply detect when this gravity vector has 0 magnitude (or close to 0 with some error threshold) and report to the main activity. 

## Sources/Reading
* [Android motion sensors documentation](https://developer.android.com/guide/topics/sensors/sensors_motion)

## Review
