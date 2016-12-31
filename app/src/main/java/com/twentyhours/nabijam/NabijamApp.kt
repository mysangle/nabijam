package com.twentyhours.nabijam

import android.app.Application
import timber.log.Timber

/**
 * Created by soonhyung on 12/30/16.
 */
class NabijamApp : Application() {
  override fun onCreate() {
    super.onCreate()

    // Timber
    Timber.plant(Timber.DebugTree())
  }
}