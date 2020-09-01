package com.cbellmont.ejemplofirebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MainService : FirebaseMessagingService(){

    companion object {
        private val TAG = MainService::class.java.name
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Gracias a super, mostramos la notificación
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Contenido del mensaje: ${remoteMessage.data}")
        }

        remoteMessage.notification?.body?.let {
            Log.d(TAG, "Cuerpo del mensaje: $it")
            // Si descomentas está línea, asegurate de quitar también super.onMessageReceived(remoteMessage)
            // ya que, en caso contrario, se lanzarían 2 notificaciones.
            // sendNotification(it)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "token --> $token")
    }

    // OPCIONAL : Utilizar solamente si quieres lanzar una notificaion personalizada.
    /* private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        val channelId = getString(R.string.default_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    } */

}