package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.timer.service.TimerService
import java.util.Locale


class TimerStartActivity : AppCompatActivity() {
    lateinit var timerButton : Button
    lateinit var timerText : TextView

    private val timerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val time = intent.getIntExtra("time", 0)
            // Update the TextView with the time
            timerText.setText(String.format(Locale.getDefault(), "%d seconds", time))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer_start)
        timerButton = findViewById(R.id.start_timer)
        timerText  = findViewById(R.id.timer_text)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timerButton.setOnClickListener {
            val serviceIntent = Intent(this, TimerService::class.java)
            serviceIntent.setAction("START_TIMER") // Add an action to start the timer

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }

    override fun onResume() {
        val filter = IntentFilter("TimerUpdate")
        registerReceiver(timerReceiver, filter)
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(timerReceiver)
    }
}