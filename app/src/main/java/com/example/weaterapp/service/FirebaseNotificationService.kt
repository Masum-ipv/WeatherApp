package com.example.weaterapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weaterapp.MainActivity
import com.example.weaterapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService() {

    //When app in foreground, this method will be called
    override fun onMessageReceived(message: RemoteMessage) {
        // Handle FCM message here
        Log.d("TAGY", "Notification From: ${message.from}")

        // Check if the Message contains data
        message.data.isNotEmpty().let {
            Log.d("TAGY", "Message data payload: ${message.data}")
        }

        // Check id the message contains notification payload
        message.notification?.let {
            Log.d("TAGY", "Message Notification Body: ${it.body}")
            val notificationTitle = message.notification!!.title
            val notificationBody = message.notification!!.body
            if (message.notification!!.body != null) {
                sendNotification(notificationTitle!!, notificationBody!!)
            }
        }
    }

    override fun onNewToken(token: String) {
        // Handle new or refreshed FCM registration token
        Log.d("TAGY", "Refreshed token: $token")
        // You may want to send this token to your server for further use
    }

    private fun sendNotification(notificationTitle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("NotificationTitle", notificationTitle)
        intent.putExtra("NotificationBody", messageBody)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, channelId
        )
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(notificationTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_id),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())
    }

}