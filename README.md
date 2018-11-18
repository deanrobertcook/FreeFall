# FreeFall
An application particularly afraid of heights.

## Initial Plan
### Goal:
The goal of this application is to detect free fall of the device on which the app is installed. Development of this app will be severely time-constrained.

### App structure:
This application will consist of a plain activity whose background will change to red when it detects free fall.

### Detecting Free Fall:
The [Android sensors API](https://developer.android.com/guide/topics/sensors/sensors_motion) provides a useful "software sensor" for this task - the gravity sensor - which provides a three dimensional vector indicating the direction and magnitude of gravity. A software sensor is one that aggregates information from one or more hardware sensors but provides readings using the same API. The documentation suggests that the system uses the devices gyroscope and accelerometer to provide readings for the gravity sensor, and so using this sensor we shouldn't have to factor in other causes of acceleration like the user shaking the device. It should be enough to simply detect when this gravity vector has 0 magnitude (or close to 0 with some error threshold) and report to the main activity. 

----

## Review
Upon initially setting up the gravity sensor, it appears I misunderstood how it works from the docs. It appears to always factor in the device's acceleration so as to always produce the correct gravity vector (that is, even if the device is falling, it still measures the correct, absolute direction and magnitude of acceleratin due to gravity). This, however, was no problem. All that had to be done was use the raw accelerometer sensor instead, which does not factor in the acceleration due to gravity. This, however, left the problem of device orientation.

After some more thought and experimentation, however, it appears that we shouldn't need to factor in device orientation. If the accelerometer reads 0 because there is no force from a surface keeping the device from falling, then orientation isn't important (0m/s/s is 0m/s/s in every direction). This is also supported by the fact that it's impossible to imagine the user shaking or moving the device in such a way that would mean a 0 reading from the accelerometer unless they mimic the acceleration due to gravity, which would be impossible to distinguish from a free-fall anyway. 

What was interesting was that the accelerometer data seemed to be quite inaccurate or poorly-calibrated, showing a reading of about 10m/s/s instead of the standard 9.81m/s/s due to gravity while at rest on a surface. This meant that my initial error threshold of something close to 0 (in my case, I picked 0.01) didn't work. After some experimentation, settling on very large error threshold of 1.00m/s/s seemed to provide desired results, however this may only work on my devices and more testing on other devices would be required. A more complete solution might keep track of this error value over time to provide a calibrated acceleration vector could then be compared against a more reasonable ERROR_THRESHOLD that would be more consistent across different devices.



