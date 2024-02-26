package com.harsh.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationPusher(private val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun push(title: String, body: String) {
        val channelName = context.getString(R.string.channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createPNChannel()
        }
        val builder = NotificationCompat.Builder(context, channelName)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPNChannel() {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(name, name, importance).apply {
            description = descriptionText
            enableVibration(true)
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}
