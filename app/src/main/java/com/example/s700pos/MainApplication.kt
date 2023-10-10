package com.example.s700pos

import android.app.Application
import com.stripe.stripeterminal.TerminalApplicationDelegate

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TerminalApplicationDelegate.onCreate(this)
    }
}