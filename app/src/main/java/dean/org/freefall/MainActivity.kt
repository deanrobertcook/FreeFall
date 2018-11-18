package dean.org.freefall

import android.animation.ValueAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

fun MainActivity.color(res: Int) =
        resources.getColor(res)

class MainActivity: AppCompatActivity(), SensorEventListener, ValueAnimator.AnimatorUpdateListener {

    companion object {
        const val ERROR_THRESHOLD = 0.25f
        const val ANIMATION_DURATION = 700L
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var gravitySensor: Sensor

    lateinit var background: View
    private lateinit var colorAnimator: ValueAnimator

    private var freeFalling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //if null, the sensor doesn't exist, so we might as well crash at this point
        //with more time, we might show a warning to the user
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        background = findViewById<View>(R.id.background)

        colorAnimator = ValueAnimator.ofArgb(color(R.color.panic), color(android.R.color.white))
        colorAnimator.duration = ANIMATION_DURATION

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST)
        colorAnimator.addUpdateListener(this)
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        colorAnimator.removeAllUpdateListeners()
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0].toDouble()
            val y = event.values[1].toDouble()
            val z = event.values[2].toDouble()

            val magnitude = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0))

            if (magnitude < ERROR_THRESHOLD && !freeFalling) {
                freeFalling = true
                background.setBackgroundColor(color(R.color.panic))

            } else if (magnitude >= ERROR_THRESHOLD && freeFalling) {
                colorAnimator.start()
                freeFalling = false
            }
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        background.setBackgroundColor(animation.animatedValue as Int)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Not needed
    }
}