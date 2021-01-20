package com.yoji.motivation.application

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun appContext(): Context = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}