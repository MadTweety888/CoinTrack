package com.example.cointrack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CoinTrackApplication: Application() {

    override fun onCreate() {

        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}