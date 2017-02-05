package com.twentyhours.nabijam

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber



/**
 * Created by soonhyung on 12/30/16.
 */
class NabijamApp : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()

    // Initialize Realm. Should only be done once when the application starts.
    Realm.init(this);
    val config = RealmConfiguration.Builder()
        .name("nabijam.realm")
        .build()
    // Use the config
    Realm.setDefaultConfiguration(config);

    // Timber
    Timber.plant(Timber.DebugTree())
  }

  public override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }
}