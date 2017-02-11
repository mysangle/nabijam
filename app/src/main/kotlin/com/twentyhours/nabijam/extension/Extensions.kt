@file:JvmName("ExtensionsUtils")

package com.twentyhours.nabijam.extension

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

/**
 * Created by soonhyung on 12/31/16.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Activity.hideKeyboard() {
  val focusedView = currentFocus
  if (focusedView != null) {
    focusedView.clearFocus()

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
  }
}