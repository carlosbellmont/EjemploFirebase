package com.cbellmont.ejemplofirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var userId = Random.nextInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        buttonRegister.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, Bundle())
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