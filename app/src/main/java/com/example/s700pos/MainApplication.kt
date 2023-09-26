package com.example.s700pos

import android.app.Application
import android.util.Log
import com.stripe.stripeterminal.TerminalApplicationDelegate

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("BENJI", "STARTING THE APP")
        TerminalApplicationDelegate.onCreate(this)
    }
}