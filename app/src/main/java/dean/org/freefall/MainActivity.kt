package dean.org.freefall

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

class MainActivity: AppCompatActivity(), SensorEventListener {

    companion object {
        const val TAG = "MainActivity"
        const val ERROR_THRESHOLD = 1.0f
    }

    lateinit var sensorManager: SensorManager
    lateinit var gravitySensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //if null, the sensor doesn't exist, so we might as well crash at this point
        //with more time, we might show a warning to the user
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0].toDouble()
            val y = event.values[1].toDouble()
            val z = event.values[2].toDouble()

            val magnitude = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0))

            if (magnitude < ERROR_THRESHOLD) {
                Log.d(TAG, "Magnitude of gravity is ${magnitude}m/s^2")
            }

            val backgroundColor =
                if (magnitude < (ERROR_THRESHOLD)) {
                    R.color.panic
                } else android.R.color.white

            findViewById<View>(R.id.background).setBackgroundColor(resources.getColor(backgroundColor))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Not needed
    }
}