package com.example.timer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.timer.MainActivity


class TimerService  : Service() {

    private val handler: Handler = Handler()
    private var runnable: Runnable? = null
    private var secondsPassed = 0
    public override fun onStartCommand(
        intent: Intent?,
        flags: kotlin.Int,
        startId: kotlin.Int
    ): kotlin.Int {

        createNotificationChannel()
        val notification =
            buildForegroundNotification("Timer Service", "Timer is running in background.. ")
        startForeground(123, notification)

        if ("START_TIMER".equals(intent?.getAction())) {
            if(runnable!=null){
                handler.removeCallbacks(runnable!!)
            }
            else{
                secondsPassed = 0
                runnable = object : Runnable {
                    override fun run() {
                        val updateIntent = Intent("TimerUpdate")
                        updateIntent.putExtra("time", secondsPassed)
                        sendBroadcast(updateIntent)
                        secondsPassed++
                        handler.postDelayed(this, 1000)
                    }
                }
                handler.postDelayed(runnable!!, 1000)
            }


        }
        return START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "timer"
            val description ="this is timer notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("111", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification(title: String, text: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, "111")
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(com.example.timer.R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setTicker(text)
            .build()
    }


    companion object {
        const val TAG = "TimerForegroundService"

        fun start(context: Context) {
            val intent = Intent(context, TimerService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
    }
}