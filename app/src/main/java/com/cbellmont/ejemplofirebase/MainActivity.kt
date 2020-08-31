package com.cbellmont.ejemplofirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private val TAG = MainActivity::class.java.name
    }
    private var userId = Random.nextInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_channel_id)
            val channelName = "Mis Notificaciones"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        buttonRegister.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, Bundle())

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fallo al obtener la instancia", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    token?.let { Log.d(TAG, "Token = $it") }
                    Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                })
        }

        buttonListenTopic.setOnClickListener {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.custom_topic))
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val msg = "Fracaso escuchando a ${getString(R.string.custom_topic)}"
                        Log.e(TAG, msg)
                    } else {
                        val msg = "Exito escuchando a ${getString(R.string.custom_topic)}"
                        Log.d(TAG, msg)
                    }
                }
        }
        buttonDoCrash.setOnClickListener {
            val nullString : String? = null
            nullString!!.length
        }
        buttonDoHandledCrash.setOnClickListener {
            val nullString : String? = null
            try {
                nullString!!.length
            } catch (exception : Exception) {
                FirebaseCrashlytics.getInstance().setCustomKey("TipoDeCrash", 1)
                FirebaseCrashlytics.getInstance().log("Crash capturado en un try catch.")
                FirebaseCrashlytics.getInstance().setUserId("$userId")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }
    }

}