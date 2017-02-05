@file:JvmName("ExtensionsUtils")

package com.twentyhours.nabijam.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by soonhyung on 12/31/16.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}