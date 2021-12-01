package com.satellite.messenger.ui.messages.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.satellite.messenger.MainActivity
import com.satellite.messenger.R

object Notification {

    fun updateMessages(app: Context, col:Int) = NotificationCompat.Builder(app,
        MessageService.CHANNEL_ID
    )
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setSmallIcon(R.drawable.ic_baseline_satellite_alt_24)
        .setContentTitle("$col cообщений от сервера")
        .setContentIntent(getPendingIntent(app))
        .setOngoing(true)
        .setStyle(NotificationCompat.InboxStyle())
        .setPriority(NotificationCompat.PRIORITY_LOW)

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(app: Context): PendingIntent? {
        return PendingIntent.getActivity(
            app,
            MessageService.NOTIFICATION_ID,
            Intent(app, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}