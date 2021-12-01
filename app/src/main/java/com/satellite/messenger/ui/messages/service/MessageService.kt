package com.satellite.messenger.ui.messages.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.satellite.messenger.utils.FirebaseObserver
import com.satellite.messenger.utils.Worker
import com.satellite.messenger.utils.state.ServerMessage

class MessageService:Service(), Worker {

    private var notificationManager: NotificationManager? = null
    private var isStarted = false
    private var lastMessageInDatabase = ServerMessage()
    private var colMessage = 0

    private val builder by lazy {
        Notification.updateMessages(applicationContext, colMessage)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        when (intent.extras?.getString(COMMAND_ID) ?: INVALID) {
            COMMAND_START -> {
                if (!isStarted) {

                    lastMessageInDatabase = intent.extras?.get("key") as ServerMessage

                    // запустить слушатель и обновлять при получении
                    FirebaseObserver {
                        worker(it)
                    }

                    moveToStartedState()
                    startForegroundAndShowNotification()
                    isStarted = true
                }
            }
            COMMAND_STOP -> {
                colMessage = 0
                commandStop()
            }
            INVALID -> return START_NOT_STICKY
        }

        return START_NOT_STICKY
    }

    private fun commandStop() {

        if (!isStarted)
            return

        try {
            stopForeground(true)
            stopSelf()
        } finally {
            isStarted = false
        }

    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // была команда на старт
    private fun moveToStartedState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(Intent(this, MessageService::class.java))
        else
            startService(Intent(this, MessageService::class.java))
    }

    // создаем канал
    private fun startForegroundAndShowNotification() {
        createChannel()
        startForeground(NOTIFICATION_ID, builder.build())
    }

    // создание канала
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "SatMessenger"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    // статические переменные для использования в сервисе
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel"
        const val COMMAND_START = "COMMAND_START"
        const val COMMAND_STOP = "COMMAND_STOP"
        const val INVALID = "INVALID"
        const val COMMAND_ID = "COMMAND_ID"
    }

    override fun worker(temp:ServerMessage) {
        if (lastMessageInDatabase != temp && (lastMessageInDatabase.datetime <= temp.datetime)){
            lastMessageInDatabase = temp
            colMessage++
            val notification = Notification.updateMessages(applicationContext, colMessage).build()
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

}