package com.example.android.nextreminder.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Use the hardware sensor to detect if the user shook the device.
 * Accelerometer: motion detection, measures the acceleration force in m/s2 that is applied to a
 * device on all three physical axes (x, y, and z), including the force of gravity.
 *
 * https://developer.android.com/guide/topics/sensors/sensors_overview
 * https://developer.android.com/reference/android/hardware/SensorManager.html
 *
 * I mixed a bit of all these 3 tutorials to create this helper class:
 * https://jasonmcreynolds.com/?p=388
 * https://www.geeksforgeeks.org/how-to-detect-shake-event-in-android/
 * https://medium.com/@enzoftware/lets-play-with-the-android-accelerometer-kotlin-%EF%B8%8F-ed92981b0a6c
 */
class ShakeDetector(val listener: ShakeListener) : SensorEventListener {

    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    override fun onSensorChanged(sensor: SensorEvent?) {
        sensor ?: return
        // Fetch the 3 physical axes
        val x = sensor.values[0]
        val y = sensor.values[1]
        val z = sensor.values[2]
        lastAcceleration = currentAcceleration

        // Get the current accelerations with the help of fetched x,y,z values
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta: Float = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta

        // If the acceleration value is over 12, the user shook the device
        if (acceleration > 12) {
            listener.onShake().invoke()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {}
}

class ShakeListener(val listener: () -> Unit) {
    fun onShake() = listener
}