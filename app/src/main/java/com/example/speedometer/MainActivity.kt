package com.example.speedometer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedometer = findViewById<SpeedometerView>(R.id.speedometer)
        speedometer.setSpeed(78f)
        speedometer.setSpeedLimit(50)
        speedometer.setGear("D")
    }
}
