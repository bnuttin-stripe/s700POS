package com.bnuttin.s700pos

import android.app.Application
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.stripe.stripeterminal.TerminalApplicationDelegate

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TerminalApplicationDelegate.onCreate(this)
        AppPreferences.setup(applicationContext)
    }
}